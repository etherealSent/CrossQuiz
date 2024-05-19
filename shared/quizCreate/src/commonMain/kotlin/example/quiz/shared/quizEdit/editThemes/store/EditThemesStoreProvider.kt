package example.quiz.shared.quizEdit.editThemes.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.ThemeListItem

class EditThemesStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
) {

    fun provide(): EditThemesStore = object : EditThemesStore,
        Store<EditThemesStore.Intent, EditThemesStore.State, Nothing> by storeFactory.create(
            name = "EditThemesStore",
            initialState = EditThemesStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<ThemeListItem>) : Msg()
        data class PickedItemsLoaded(val items: List<ThemeListItem>) : Msg()
        object ShowDialog : Msg()
        object CloseDialog : Msg()
        data class SearchChanged(val search: String) : Msg()
        data class ThemeChecked(val id: Long, val temporary: Boolean) : Msg()
        object TemporaryThemeAdded : Msg()
        data class ThemeItemsLoaded(val themes: List<Long>) : Msg()

    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<EditThemesStore.Intent, Unit, EditThemesStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit) {
             database
                 .updates
                 .observeOn(mainScheduler)
                 .map(Msg::ItemsLoaded)
                 .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: EditThemesStore.Intent) {
            when (intent) {
                is EditThemesStore.Intent.SetSearch -> dispatch(Msg.SearchChanged(intent.search))
                is EditThemesStore.Intent.AddTheme -> dispatch(Msg.ThemeChecked(intent.themeId, intent.temporary))
                is EditThemesStore.Intent.AddTemporaryTheme -> dispatch(Msg.TemporaryThemeAdded)
                is EditThemesStore.Intent.ShowDialog -> dispatch(Msg.ShowDialog)
                is EditThemesStore.Intent.CloseDialog -> dispatch(Msg.CloseDialog)
                is EditThemesStore.Intent.CreateTheme -> addTheme()
                is EditThemesStore.Intent.GetThemeItems -> dispatch(Msg.ThemeItemsLoaded(intent.themeIds.map { it.toLong() }))
            }
        }


        private fun addTheme() {
            val state = state()
            if (state.temporaryThemeItems.isNotEmpty()) {
                state.temporaryThemeItems.forEach {
                    database.add(it.title).subscribeScoped()
                }
            }
        }
    }

    private object ReducerImpl : Reducer<EditThemesStore.State, Msg> {
        override fun EditThemesStore.State.reduce(msg: Msg): EditThemesStore.State = when (msg) {
            is Msg.ItemsLoaded -> copy(themeItems = msg.items)
            is Msg.PickedItemsLoaded -> copy(checkedThemeItems = msg.items)
            is Msg.ShowDialog -> copy(showDialog = true)
            is Msg.CloseDialog -> copy(
                showDialog = false,
                search = ""
            )
            is Msg.ThemeChecked -> {
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
            is Msg.TemporaryThemeAdded-> {
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
            is Msg.SearchChanged -> copy(search = msg.search)
            is Msg.ThemeItemsLoaded -> copy(checkedThemeItems = themeItems.filter { it.id in msg.themes })
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

    }

    interface Database {
        val updates: Observable<List<ThemeListItem>>
        fun add(title: String): Completable
    }
}