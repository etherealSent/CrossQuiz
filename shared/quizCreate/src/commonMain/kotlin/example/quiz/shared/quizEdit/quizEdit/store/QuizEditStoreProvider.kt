package example.quiz.shared.quizEdit.quizEdit.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup


class QuizEditStoreProvider(
    private val storeFactory: StoreFactory,
    private val quizDatabase: QuizDatabase,
    private val itemId: Long
) {

    fun provide(): QuizEditStore = object : QuizEditStore,
        Store<QuizEditStore.Intent, QuizEditStore.State, QuizEditStore.Label> by storeFactory.create(
            name = "QuizCreateStore",
            initialState = QuizEditStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemLoaded(val item: QuizListItem) : Msg()
        data class TitleChanged(val title: String) : Msg()
        data class DescriptionChanged(val description: String) : Msg()
        object ShowMoreVertMenu : Msg()
        data class ThemesChanged(val themeList: List<String>) : Msg()
    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<QuizEditStore.Intent, Unit, QuizEditStore.State, Msg, QuizEditStore.Label>() {
        override fun executeAction(action: Unit) {
            quizDatabase
                .load(id = itemId)
                .map(Msg::ItemLoaded)
                .observeOn(mainScheduler)
                .subscribeScoped(onSuccess = ::dispatch)
        }

        override fun executeIntent(intent: QuizEditStore.Intent) {
            when (intent) {
                is QuizEditStore.Intent.SetTitle -> setTitle(intent.title)
                is QuizEditStore.Intent.SetDescription -> setDescription(intent.description)
                is QuizEditStore.Intent.ShowMoreVertMenu -> dispatch(Msg.ShowMoreVertMenu)
                is QuizEditStore.Intent.UpdateThemes -> setThemes(intent.themeList)
            }
        }

        private fun setTitle(title: String) {
            dispatch(Msg.TitleChanged(title))
            publish(
                QuizEditStore.Label.Changed(
                    QuizListItem(
                        title = title,
                        themeList = state().themeList,
                        description = state().description
                    )
                )
            )
            quizDatabase.setTitle(itemId, title).subscribeScoped()
        }

        private fun setThemes(themeList: List<String>) {
            dispatch(Msg.ThemesChanged(themeList))
            publish(
                QuizEditStore.Label.Changed(
                    QuizListItem(
                        title = state().title,
                        themeList = themeList,
                        description = state().description
                    )
                )
            )
            quizDatabase.setThemes(itemId, themeList).subscribeScoped()
        }

        private fun setDescription(description: String) {
            dispatch(Msg.DescriptionChanged(description))
            publish(
                QuizEditStore.Label.Changed(
                    QuizListItem(
                        title = state().title,
                        themeList = state().themeList,
                        description = description
                    )
                )
            )
            quizDatabase.setDescription(itemId, description).subscribeScoped()
        }

    }

    private object ReducerImpl : Reducer<QuizEditStore.State, Msg> {
        override fun QuizEditStore.State.reduce(msg: Msg): QuizEditStore.State = when (msg) {
            is Msg.ItemLoaded -> copy(
                id = msg.item.id,
                orderNum = msg.item.orderNum,
                title = msg.item.title,
                description = msg.item.description,
                themeList = msg.item.themeList,
                setup = Setup.Default
            )

            is Msg.TitleChanged -> copy(title = msg.title)
            is Msg.DescriptionChanged -> copy(description = msg.description)
            is Msg.ShowMoreVertMenu -> copy(showMoreVertMenu = !showMoreVertMenu)
            is Msg.ThemesChanged -> copy(themeList = msg.themeList)
        }
    }

    interface QuizDatabase {
        fun load(id: Long): Maybe<QuizListItem>
        fun setTitle(id: Long, title: String): Completable
        fun setDescription(id: Long, description: String): Completable
        fun setThemes(id: Long, themeList: List<String>): Completable
    }

}