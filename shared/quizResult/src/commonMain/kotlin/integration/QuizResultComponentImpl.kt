package integration

import QuizResultState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import quizResult.QuizResultRepository
import store.QuizResultStore
import store.QuizResultStoreProvider

class QuizResultComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    repository: QuizResultRepository,
    id: String,
) : QuizResultComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            QuizResultStoreProvider(
                storeFactory = storeFactory,
                repository = repository,
            ).provide()
        }

    init {
        store.accept(QuizResultStore.Intent.Load(id))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<QuizResultState> = store.stateFlow

    override fun onFinishClick() {
        store.accept(QuizResultStore.Intent.FinishQuiz)
    }

    override fun onReloadClick() {
       // store.accept()
    }
}