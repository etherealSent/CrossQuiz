package integration

import Answer
import QuizSolveMapper
import QuizSolveState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import quizSolve.QuizSolveRepository
import store.QuizSolveStore
import store.QuizSolveStoreProvider

class QuizSolveComponentImpl(
    private val quizId: Int,
    private val repository: QuizSolveRepository,
    private val mapper: QuizSolveMapper,
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
): QuizSolveComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            QuizSolveStoreProvider(
                storeFactory = storeFactory,
                quizSolveRepository = repository,
                mapper = mapper,
            ).provide(QuizSolveState.Loading)
        }

    init {
        store.accept(QuizSolveStore.Intent.LoadQuiz(quizId))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onAnswerClick(answer: Answer) {
        store.accept(QuizSolveStore.Intent.QuizSolveIntent.AnswerClick(answer))
    }

    override fun onNextClick() {
        store.accept(QuizSolveStore.Intent.QuizSolveIntent.NextQuestion)
    }

    override fun onReloadClick() {
        store.accept(QuizSolveStore.Intent.LoadQuiz(quizId))
    }

    override fun onQuizStart() {
        store.accept(QuizSolveStore.Intent.StartQuiz)
    }
}