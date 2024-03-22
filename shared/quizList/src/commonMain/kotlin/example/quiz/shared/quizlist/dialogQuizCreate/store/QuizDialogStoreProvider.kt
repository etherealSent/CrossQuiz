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

class QuizDialogStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): QuizDialogStore =
        object : QuizDialogStore, Store<QuizDialogStore.Intent, QuizDialogStore.State, Nothing> by storeFactory.create(
            name = "QuizDialogStore",
            initialState = QuizDialogStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class QuizDialogLoaded(val quiz: QuizListItem) : Msg()
        data class QuizSetupChanged(val setup: Setup) : Msg()
        data class QuizTitleChanged(val title: String) : Msg()
        data class QuizThemePicked(val id: Long, val theme: String) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<QuizDialogStore.Intent, Unit, QuizDialogStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit) {
           database
               .updates
               .observeOn(mainScheduler)
               .map(Msg::QuizDialogLoaded)
               .subscribeScoped (onNext = ::dispatch)
        }

        override fun executeIntent(intent: QuizDialogStore.Intent) {
           when(intent) {
               is QuizDialogStore.Intent.SetupQuiz -> dispatch(Msg.QuizSetupChanged(intent.setup))
               is QuizDialogStore.Intent.SetTitle -> dispatch(Msg.QuizTitleChanged(intent.title))
               is QuizDialogStore.Intent.PickTheme -> dispatch(
                   Msg.QuizThemePicked(
                       intent.id,
                       intent.theme
                   )
               )
               is QuizDialogStore.Intent.CreateQuiz ->addQuiz()
           }
        }

        private fun addQuiz() {
            val state = state()
            if (state.title.isNotEmpty()) {
                dispatch(Msg.QuizTitleChanged(title = ""))
                database.add(title = state.title, setup = state.setup, listThemes = state.themeList).subscribeScoped()
            }
        }
    }

    private object ReducerImpl : Reducer<QuizDialogStore.State, Msg> {
        override fun QuizDialogStore.State.reduce(msg: Msg): QuizDialogStore.State =
            when(msg) {
                is Msg.QuizDialogLoaded -> copy(title = "", setup = Setup.Default)
                is Msg.QuizSetupChanged -> copy(setup = msg.setup)
                is Msg.QuizTitleChanged -> copy(title = msg.title)
                is Msg.QuizThemePicked -> update(id = msg.id) { copy(themeList = this.themeList + msg.theme)}
            }
        private inline fun QuizDialogStore.State.update(id: Long, func: QuizListItem.() -> QuizListItem): QuizDialogStore.State {
            val quizListItem = quizList.find { it.id == id } ?: return this

            return put(quizListItem.func())
        }

        private fun QuizDialogStore.State.put(quiz: QuizListItem): QuizDialogStore.State {
            val oldQuizes = quizList.associateByTo(mutableMapOf(), QuizListItem::id)
            val oldQuiz: QuizListItem? = oldQuizes.put(quiz.id, quiz)

            return copy(quizList = if (oldQuiz?.orderNum == quiz.orderNum) oldQuizes.values.toList() else oldQuizes.values.sorted())
        }

        private fun Iterable<QuizListItem>.sorted(): List<QuizListItem> = sortedByDescending(QuizListItem::orderNum)
    }

    interface Database {
        val updates: Observable<QuizListItem>

        fun add(title: String, setup: Setup, listThemes: List<String>): Completable
    }
}