package store

import Answer
import ApiResponse
import Question
import QuizModel
import QuizSolveMapper
import QuizSolveState
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import quizSolve.QuizSolveRepository

// TODO сделать таймер потом
internal class QuizSolveStoreProvider(
    private val storeFactory: StoreFactory,
    private val quizSolveRepository: QuizSolveRepository,
    private val mapper: QuizSolveMapper,
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
        data class AnswersChanged(val answers: Answer) : Msg()
        data class QuestionChanged(val question: Question, val answer: Answer) : Msg()
        data class QuizLoaded(val quiz: QuizModel?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<QuizSolveStore.Intent, Unit, QuizSolveState, Msg, Nothing>() {
        override fun executeIntent(intent: QuizSolveStore.Intent) {
            when (intent) {
                is QuizSolveStore.Intent.QuizSolveIntent -> {
                    val solveState = state() as? QuizSolveState.QuizSolve ?: return
                    val currentQuestion = solveState.currentQuestion
                    when (intent) {
                        is QuizSolveStore.Intent.QuizSolveIntent.AnswerClick -> {
//                            val currentChosenAnswers = solveState.currentChosenAnswer
//                            val answers = if (currentQuestion is Question.Single) {
//                                if (currentChosenAnswers.contains(intent.answer)) {
//                                    listOf()
//                                } else {
//                                    listOf(intent.answer)
//                                }
//                            } else {
//                                if (currentChosenAnswers.contains(intent.answer)) {
//                                    currentChosenAnswers.filter { it == intent.answer }
//                                } else {
//                                    currentChosenAnswers + intent.answer
//                                }
//                            }
                            dispatch(Msg.AnswersChanged(intent.answer))
                        }

                        is QuizSolveStore.Intent.QuizSolveIntent.NextQuestion -> {
                            if (currentQuestion == solveState.quiz.questions.last()) {
                                // Open next screen + send data to backend
                            } else {
//                                dispatch(Msg.QuestionChanged(
//                                    intent.answer))
//                                val currentQuestionId =
//                                    solveState.quiz.questions.indexOf(currentQuestion)
//                                dispatch(Msg.QuestionChanged(solveState.quiz.questions[currentQuestionId + 1]))
                                // save question in map
                            }
                        }

                    }
                }

                // +
                is QuizSolveStore.Intent.LoadQuiz -> {
                    scope.launch {
                        val response = withContext(Dispatchers.Default) {
                            quizSolveRepository.getQuizById(intent.id)
                        }
                        val result = when (response) {
                            is ApiResponse.Success -> response.body
                            is ApiResponse.Error -> null
                        }
                        val quiz = mapper.mapToQuiz(result)
                        dispatch(Msg.QuizLoaded(quiz))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<QuizSolveState, Msg> {

        override fun QuizSolveState.reduce(msg: Msg): QuizSolveState {
            return when (msg) {
                is Msg.AnswersChanged -> {
                    when (this) {
                        is QuizSolveState.QuizSolve -> copy(currentChosenAnswer = msg.answers)
                        is QuizSolveState.Loading -> this
                        is QuizSolveState.Error -> this
                    }
                }
                is Msg.QuestionChanged -> {
                    when (this) {
                        is QuizSolveState.QuizSolve -> copy(currentQuestion = msg.question)
                        is QuizSolveState.Loading -> this
                        is QuizSolveState.Error -> this
                    }
                }
                is Msg.QuizLoaded -> {
                    val quiz = msg.quiz
                    if (quiz != null) {
                        QuizSolveState.QuizSolve(
                            quiz = quiz,
                            currentQuestion = quiz.questions.first(),
                            currentChosenAnswer = null,
                            chosenAnswers = hashMapOf(),
                        )
                    } else {
                        QuizSolveState.Error
                    }
                }
            }
        }
    }
}