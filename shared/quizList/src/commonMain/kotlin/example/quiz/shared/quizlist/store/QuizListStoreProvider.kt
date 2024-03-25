package example.quiz.shared.quizlist.store

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
import dialog.store.QuizDialogStoreProvider
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup

class QuizListStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): QuizListStore =
        object : QuizListStore, Store<QuizListStore.Intent, QuizListStore.State, Nothing> by storeFactory.create(
            name = "QuizListStore",
            initialState = QuizListStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<QuizListItem>) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<QuizListStore.Intent, Unit, QuizListStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Msg::ItemsLoaded)
                .subscribeScoped (onNext = ::dispatch)
        }

        override fun executeIntent(intent: QuizListStore.Intent) {
            when(intent) {
                is QuizListStore.Intent.AddItem-> addItem()
            }
        }

        private fun addItem() {
//            val state = state()
            database.add(title = "", listThemes = listOf("").toThemeString()).subscribeScoped()
            }

        private fun List<String>.toThemeString() =
            this.joinToString(separator = ",")
        }
    private object ReducerImpl : Reducer<QuizListStore.State, Msg> {
        override fun QuizListStore.State.reduce(msg: Msg): QuizListStore.State =
            when(msg) {
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
            }
        private fun Iterable<QuizListItem>.sorted(): List<QuizListItem> = sortedByDescending(
            QuizListItem::orderNum)
    }

    interface Database {
        val updates: Observable<List<QuizListItem>>

        fun add(title: String, listThemes: String): Completable
    }
}