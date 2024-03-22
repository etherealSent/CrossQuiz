package store

import Answer
import Question
import QuizSolveState
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class QuizSolveStoreProvider(
    private val storeFactory: StoreFactory,
) {

    fun provide(initialState: QuizSolveState): QuizSolveStore =
        object : QuizSolveStore,
            Store<QuizSolveStore.Intent, QuizSolveState, Nothing> by storeFactory.create(
                name = "QuizSolveStore",
                initialState = initialState,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        // TODO use database?
        data class AnswersChanged(val answers: List<Answer>) : Msg()
        data class QuestionChanged(val question: Question) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<QuizSolveStore.Intent, Unit, QuizSolveState, Msg, Nothing>() {
        override fun executeIntent(intent: QuizSolveStore.Intent) {
            val currentQuestion = state().currentQuestion
            return when (intent) {
                is QuizSolveStore.Intent.AnswerClick -> {
                    val currentChosenAnswers = state().currentChosenAnswers
                    val answers = if (currentQuestion is Question.Single) {
                        if (currentChosenAnswers.contains(intent.answer)) {
                            listOf()
                        } else {
                            listOf(intent.answer)
                        }
                    } else {
                        if (currentChosenAnswers.contains(intent.answer)) {
                            currentChosenAnswers.filter { it == intent.answer }
                        } else {
                            currentChosenAnswers + intent.answer
                        }
                    }
                    dispatch(Msg.AnswersChanged(answers))
                }

                is QuizSolveStore.Intent.NextQuestion -> {
                    if (currentQuestion == state().quiz.questions.last()) {
                        // Open next screen + send data to backend
                    } else {
                        val currentQuestionId =
                            state().quiz.questions.indexOf(currentQuestion)
                        dispatch(Msg.QuestionChanged(state().quiz.questions[currentQuestionId + 1]))
                        // save question in map
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<QuizSolveState, Msg> {

        override fun QuizSolveState.reduce(msg: Msg): QuizSolveState {
            return when (msg) {
                is Msg.AnswersChanged -> copy(currentChosenAnswers = msg.answers)
                is Msg.QuestionChanged -> {
                    copy(currentQuestion = msg.question)
                }
            }
        }
    }
}