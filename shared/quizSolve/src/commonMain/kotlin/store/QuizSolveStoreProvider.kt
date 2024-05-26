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
        data object QuestionChanged : Msg()
        data class QuizLoaded(val quiz: QuizModel?) : Msg()
        data object QuizStarted : Msg()
        data object QuizFinished : Msg()
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
                                dispatch(Msg.QuizFinished)
                            } else {
                                dispatch(Msg.QuestionChanged)
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

                is QuizSolveStore.Intent.StartQuiz -> {
                    dispatch(Msg.QuizStarted)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<QuizSolveState, Msg> {

        override fun QuizSolveState.reduce(msg: Msg): QuizSolveState {
            return when (msg) {
                is Msg.AnswersChanged -> {
                    when (this) {
                        is QuizSolveState.QuizSolve -> copy(
                            currentChosenAnswer = msg.answers,
                            canMoveForward = true,
                        )

                        is QuizSolveState.Loading -> this
                        is QuizSolveState.Error -> this
                    }
                }

                is Msg.QuestionChanged -> {
                    when (this) {
                        is QuizSolveState.QuizSolve -> {
                            val nextQuestionIndex = this.questionIndex + 1
                            val currentQuestion = currentQuestion
                            val chosenAnswers = chosenAnswers
                            val nextQuestion = quiz.questions[nextQuestionIndex]
                            chosenAnswers[currentQuestion] = currentChosenAnswer!!
                            copy(
                                currentQuestion = nextQuestion,
                                canMoveForward = false,
                                questionIndex = nextQuestionIndex,
                                chosenAnswers = chosenAnswers,
                            )
                        }

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
                            isStarted = false,
                        )
                    } else {
                        QuizSolveState.Error
                    }
                }

                is Msg.QuizStarted -> {
                    when (this) {
                        is QuizSolveState.QuizSolve -> copy(isStarted = true)
                        is QuizSolveState.Loading -> this
                        is QuizSolveState.Error -> this
                    }
                }

                // TODO
                is Msg.QuizFinished -> {this}
            }
        }
    }
}