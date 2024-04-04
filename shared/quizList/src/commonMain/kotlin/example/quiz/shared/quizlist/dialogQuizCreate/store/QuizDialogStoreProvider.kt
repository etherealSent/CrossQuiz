package dialog.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.quiz.dialog.store.QuizDialogStore
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup
import example.quiz.shared.quizlist.ThemeListItem
import example.quiz.shared.quizlist.dialogQuizCreate.SIZE_OF_DIALOG_ITEMS

class QuizDialogStoreProvider(
    private val storeFactory: StoreFactory,
    private val quizDatabase: QuizDatabase,
    private val themeDatabase: ThemeDatabase
) {

    fun provide(): QuizDialogStore = object : QuizDialogStore,
        Store<QuizDialogStore.Intent, QuizDialogStore.State, Nothing> by storeFactory.create(
            name = "QuizDialogStore",
            initialState = QuizDialogStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<ThemeListItem>) : Msg()
        data class QuizSetupChanged(val setup: Setup) : Msg()
        data class QuizTitleChanged(val title: String) : Msg()
        data class QuizSearchChanged(val search: String) : Msg()
        data class OnCheckedTheme(val id: Long, val temporary: Boolean) : Msg()
        object AddTemporaryTheme : Msg()

        object ShowDialog : Msg()
        object CloseDialog : Msg()
        object OnNextClicked : Msg()
        object OnBackClicked : Msg()

    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<QuizDialogStore.Intent, Unit, QuizDialogStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit) {
            themeDatabase.updates.observeOn(mainScheduler).map(Msg::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: QuizDialogStore.Intent) {
            when (intent) {
                is QuizDialogStore.Intent.SetupQuiz -> dispatch(Msg.QuizSetupChanged(intent.setup))
                is QuizDialogStore.Intent.SetTitle -> dispatch(Msg.QuizTitleChanged(intent.title))
                is QuizDialogStore.Intent.CreateQuiz -> addQuiz()
                is QuizDialogStore.Intent.ShowDialog -> dispatch(Msg.ShowDialog)
                is QuizDialogStore.Intent.OnNextClicked -> dispatch(Msg.OnNextClicked)
                is QuizDialogStore.Intent.OnBackClicked -> dispatch(Msg.OnBackClicked)
                is QuizDialogStore.Intent.CreateTheme -> addTheme()
                is QuizDialogStore.Intent.SetSearch -> dispatch(Msg.QuizSearchChanged(intent.search))
                is QuizDialogStore.Intent.CloseDialog -> dispatch(Msg.CloseDialog)
                is QuizDialogStore.Intent.PickTheme -> dispatch(
                    Msg.OnCheckedTheme(
                        intent.themeId, intent.temporary
                    )
                )

                is QuizDialogStore.Intent.AddTemporaryTheme -> dispatch(Msg.AddTemporaryTheme)
            }
        }

        private fun addQuiz() {
            val state = state()
            if (state.title.isNotEmpty()) {
                quizDatabase.add(
                    title = state.title, listThemes = state.checkedThemeItems.toThemeString()
                ).subscribeScoped()
            }
        }

        private fun addTheme() {
            val state = state()
            if (state.temporaryThemeItems.isNotEmpty()) {
                state.temporaryThemeItems.forEach {
                    themeDatabase.add(it.title).subscribeScoped()
                }
            }
        }

        private fun List<ThemeListItem>.toThemeString(): String {
            val themeIds = mutableListOf<Long>()
            this.forEach {
                themeIds.add(it.id)
            }
            return themeIds.toList().joinToString(",")
        }

    }

    private object ReducerImpl : Reducer<QuizDialogStore.State, Msg> {
        override fun QuizDialogStore.State.reduce(msg: Msg): QuizDialogStore.State = when (msg) {
            is Msg.ItemsLoaded -> copy(themeItems = msg.items)
            is Msg.QuizSetupChanged -> copy(setup = msg.setup)
            is Msg.QuizTitleChanged -> copy(title = msg.title)
            is Msg.ShowDialog -> copy(showDialog = !showDialog)
            is Msg.CloseDialog -> copy(
                showDialog = !showDialog,
                currentDialogScreenId = 0L,
                title = "",
                search = "",
                setup = Setup.Default,
                checkedThemeItems = emptyList(),
                temporaryThemeItems = emptyList()
            )

            is Msg.OnNextClicked -> copy(currentDialogScreenId = canGoNext(currentDialogScreenId + 1))
            is Msg.OnBackClicked -> copy(currentDialogScreenId = canGoBack(currentDialogScreenId - 1))
            is Msg.QuizSearchChanged -> copy(search = msg.search)
            is Msg.OnCheckedTheme -> {
                val onCheckedTheme = onChecked(
                    checkedThemes = checkedThemeItems,
                    themes = themeItems,
                    temporaryThemes = temporaryThemeItems,
                    temporary = msg.temporary,
                    id = msg.id
                )
                copy(
                    checkedThemeItems = onCheckedTheme.first,
                    temporaryThemeItems = onCheckedTheme.second
                )
            }

            is Msg.AddTemporaryTheme -> {
                copy(
                    temporaryThemeItems = temporaryThemeItems + ThemeListItem(
                        themeItems.size + temporaryThemeItems.size + 1L,
                        themeItems.size + temporaryThemeItems.size + 1L,
                        search,
                        true
                    ), search = "", checkedThemeItems = checkedThemeItems + ThemeListItem(
                        themeItems.size + temporaryThemeItems.size + 1L,
                        themeItems.size + temporaryThemeItems.size + 1L,
                        search,
                        true
                    )
                )

            }
        }

        private fun canGoBack(newId: Long): Long {
            return if (newId >= 0) {
                newId
            } else {
                0
            }
        }

        private fun canGoNext(newId: Long): Long {
            return if (newId <= SIZE_OF_DIALOG_ITEMS - 1) {
                newId
            } else {
                (SIZE_OF_DIALOG_ITEMS - 1).toLong()
            }
        }

        private fun onChecked(
            checkedThemes: List<ThemeListItem>,
            themes: List<ThemeListItem>,
            temporaryThemes: List<ThemeListItem>,
            temporary: Boolean,
            id: Long
        ): Pair<List<ThemeListItem>, List<ThemeListItem>> {
            if (temporary) {
                val tempTheme = temporaryThemes.find { it.id == id }

                if (tempTheme in checkedThemes) {
                    return Pair(
                        checkedThemes - tempTheme, temporaryThemes - tempTheme
                    ) as Pair<List<ThemeListItem>, List<ThemeListItem>>
                } else {
                    return Pair(
                        checkedThemes + tempTheme, temporaryThemes
                    ) as Pair<List<ThemeListItem>, List<ThemeListItem>>
                }
            } else {
                val theme = themes.find { it.id == id }
                if (theme in checkedThemes) {
                    return Pair(
                        checkedThemes - theme, temporaryThemes
                    ) as Pair<List<ThemeListItem>, List<ThemeListItem>>
                } else {
                    return Pair(
                        checkedThemes + theme, temporaryThemes
                    ) as Pair<List<ThemeListItem>, List<ThemeListItem>>
                }
            }
        }


//        private inline fun QuizDialogStore.State.update(id: Long, func: QuizListItem.() -> QuizListItem): QuizDialogStore.State {
//            val quizListItem = quizItem.find { it.id == id } ?: return this
//
//            return put(quizListItem.func())
//        }
//
//        private fun QuizDialogStore.State.put(quiz: QuizListItem): QuizDialogStore.State {
//            val oldQuizes = quizItem.associateByTo(mutableMapOf(), QuizListItem::id)
//            val oldQuiz: QuizListItem? = oldQuizes.put(quiz.id, quiz)
//
//            return copy(quizItem = if (oldQuiz?.orderNum == quiz.orderNum) oldQuizes.values.toList() else oldQuizes.values.sorted())
//        }

        private fun Iterable<ThemeListItem>.sorted(): List<ThemeListItem> =
            sortedByDescending(ThemeListItem::orderNum)
    }

    interface QuizDatabase {
        val updates: Observable<List<QuizListItem>>

        fun add(title: String, listThemes: String): Completable
    }

    interface ThemeDatabase {
        val updates: Observable<List<ThemeListItem>>

        fun add(title: String): Completable
    }
}