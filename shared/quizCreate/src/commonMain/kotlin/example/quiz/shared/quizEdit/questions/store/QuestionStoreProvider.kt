package example.quiz.shared.quizEdit.questions.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import example.quiz.shared.quizlist.MultiplyOptionQuestion
import example.quiz.shared.quizlist.OptionAnswer
import example.quiz.shared.quizlist.OptionQuestion
import example.quiz.shared.quizlist.Question
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.TextQuestion

data class QuestionStoreProvider(
    private val storeFactory: StoreFactory
) {
    fun provide(): QuestionStore = object : QuestionStore,
        Store<QuestionStore.Intent, QuestionStore.State, Nothing> by storeFactory.create(
            name = "QuestionStore",
            initialState = QuestionStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class CreateQuestion(val questionType: QuestionType) : Msg()
        data class QuestionSelected(val id: Long) : Msg()
        data class TextQuestionChanged(val question: String) : Msg()
        data object CreateAnswer : Msg()
        data class AnswerPicked(val id: Long) : Msg()
        data class AnswerDeleted(val id: Long) : Msg()
        data class AnswerTextChanged(val id: Long, val answer: String) : Msg()
    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<QuestionStore.Intent, Unit, QuestionStore.State, Msg, Nothing>() {

        override fun executeIntent(intent: QuestionStore.Intent) {
            when (intent) {
                is QuestionStore.Intent.CreateQuestion -> dispatch(Msg.CreateQuestion(intent.questionType))
                is QuestionStore.Intent.SetQuestion -> dispatch(Msg.TextQuestionChanged(intent.question))
                is QuestionStore.Intent.CreateAnswer -> dispatch(Msg.CreateAnswer)
                is QuestionStore.Intent.PickAnswer -> dispatch(Msg.AnswerPicked(intent.answerId))
                is QuestionStore.Intent.DeleteAnswer -> dispatch(Msg.AnswerDeleted(intent.id))
                is QuestionStore.Intent.EditAnswer -> dispatch(Msg.AnswerTextChanged(id = intent.id, answer = intent.answer))
                is QuestionStore.Intent.SelectQuestion -> dispatch(Msg.QuestionSelected(intent.id))
            }
        }
    }

    private object ReducerImpl : Reducer<QuestionStore.State, Msg> {
        override fun QuestionStore.State.reduce(msg: Msg): QuestionStore.State = when (msg) {
            is Msg.CreateQuestion -> {
                copy(
                    questions = questions + addQuestion(
                        msg.questionType,
                        id = (questions.size + 1).toLong()
                    )
                )
            }

            is Msg.QuestionSelected -> copy(selectedQuestionId = msg.id)
            is Msg.TextQuestionChanged -> copy(
                questions = textQuestionChanged(
                    questions,
                    selectedQuestionId,
                    msg.question
                )
            )

            is Msg.CreateAnswer -> copy(questions = createAnswer(questions, selectedQuestionId))
            is Msg.AnswerPicked -> copy(
                questions = pickAnswer(
                    questions,
                    selectedQuestionId,
                    msg.id
                )
            )

            is Msg.AnswerDeleted -> copy(
                questions = deleteAnswer(
                    questions,
                    selectedQuestionId,
                    msg.id
                )
            )

            is Msg.AnswerTextChanged -> copy(questions = editAnswer(questions, selectedQuestionId, msg.id, msg.answer))
//            is Msg.AnswerPicked -> copy(rightAnswers = answers.filter { it.id == msg.id })
//            is Msg.AnswerDeleted -> copy(answers = answers.filter { it.id != msg.id })
//            is Msg.AnswerTextChanged -> copy(answers = listOf())
        }

        fun textQuestionChanged(
            questions: List<Question>,
            questionId: Long,
            newText: String
        ): List<Question> {
            return questions.map { question ->
                when {
                    question.id == questionId && question is OptionQuestion -> question.copy(
                        question = newText
                    )

                    question.id == questionId && question is MultiplyOptionQuestion -> question.copy(
                        question = newText
                    )

                    question.id == questionId && question is TextQuestion -> question.copy(question = newText)
                    else -> question
                }
            }
        }

        fun createAnswer(questions: List<Question>, questionId: Long): List<Question> {
            return questions.map { question ->
                when {
                    question.id == questionId && question is OptionQuestion -> question.copy(
                        options = question.options + OptionAnswer(
                            id = (question.options.size + 1).toLong(),
                            orderNum = (question.options.size + 1).toLong(),
                            text = "Вариант ${question.options.size + 1}"
                        )
                    )

                    question.id == questionId && question is MultiplyOptionQuestion -> question.copy(
                        options = question.options + OptionAnswer(
                            id = (question.options.size + 1).toLong(),
                            orderNum = (question.options.size + 1).toLong(),
                            text = "Вариант ${question.options.size + 1}"
                        )
                    )

                    question.id == questionId && question is TextQuestion -> question
                    else -> question
                }
            }
        }

        fun editAnswer(
            questions: List<Question>,
            questionId: Long,
            answerId: Long,
            newText: String
        ): List<Question> {
            return questions.map { question ->
                when {
                    question.id == questionId && question is OptionQuestion -> question.copy(options = question.options.map { option ->
                        if (option.id == answerId) {
                            option.copy(text = newText)
                        } else {
                            option
                        }
                    })

                    question.id == questionId && question is MultiplyOptionQuestion -> question.copy(
                        options = question.options.map { option ->
                            if (option.id == answerId) {
                                option.copy(text = newText)
                            } else {
                                option
                            }
                        })

                    question.id == questionId && question is TextQuestion -> question
                    else -> question
                }
            }
        }

        fun pickAnswer(
            questions: List<Question>,
            questionId: Long,
            answerId: Long
        ): List<Question> {
            return questions.map { question ->
                when {
                    question.id == questionId && question is OptionQuestion -> question.copy(
                        rightAnswer = question.options.find { it.id == answerId })

                    question.id == questionId && question is MultiplyOptionQuestion -> question.copy(
                        rightAnswer = question.rightAnswer + question.options.find { it.id == answerId })

                    question.id == questionId && question is TextQuestion -> question
                    else -> question
                }
            }
        }

        fun deleteAnswer(
            questions: List<Question>,
            questionId: Long,
            answerId: Long
        ): List<Question> {
            return questions.map { question ->
                when {
                    question.id == questionId && question is OptionQuestion -> question.copy(
                            options = if (question.options.size != 1) question.options.filter { it.id != answerId } else question.options
                    )

                    question.id == questionId && question is MultiplyOptionQuestion -> question.copy(
                        options = if (question.options.size != 1) question.options.filter { it.id != answerId } else question.options
                    )

                    question.id == questionId && question is TextQuestion -> question
                    else -> question
                }
            }
        }

        fun addQuestion(questionType: QuestionType, id: Long): Question {
            return when (questionType) {
                QuestionType.OneOptionQuestion -> OptionQuestion(
                    id = id,
                    options = listOf(OptionAnswer(id = 1, orderNum = 1, text = "Вариант 1"))
                )

                QuestionType.MultiplyOptionQuestion -> MultiplyOptionQuestion(id = id)
                QuestionType.TextQuestion -> TextQuestion(id = id)
            }
        }

//        private fun answerTextChanged(id: Long, text: String, answers: List<Answer>): List<Answer> {
//            return answers.map {
//                if (it.id == id) {
//                    it.copy(text = text)
//                } else {
//                    it
//                }
//            }
//        }
    }
}