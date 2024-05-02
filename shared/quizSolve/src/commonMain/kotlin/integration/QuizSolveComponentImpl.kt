package integration

import Answer
import QuizSolveState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import store.QuizSolveStore
import store.QuizSolveStoreProvider

class QuizSolveComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
): QuizSolveComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            QuizSolveStoreProvider(
                storeFactory = storeFactory,
            ).provide(QuizSolveState.Initial)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow


    override fun onAnswerClick(answer: Answer) {
        store.accept(QuizSolveStore.Intent.AnswerClick(answer))
    }

    override fun onNextClick() {
        store.accept(QuizSolveStore.Intent.NextQuestion)
    }
}