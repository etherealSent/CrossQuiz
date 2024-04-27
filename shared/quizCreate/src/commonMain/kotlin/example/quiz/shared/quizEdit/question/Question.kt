package example.quiz.shared.quizEdit.question

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.Settings

interface Question {

    val models: Value<Model>

    data class Model(
        val type: QuestionType,
        val question: String,
        val answers: List<Answer>,
        val rightAnswers: List<Answer>,
        val settings: Settings
    )

    fun onQuestionChanged(question: String)
    fun onRightAnswerPicked(answerId: Long)
    fun  onAnswerDeleteClicked(id: Long)
    fun onAnswerChanged(answer: Answer)
    fun onChangeType(questionType: QuestionType)
    fun onDismissChangeType()
    fun onSelectType(questionType: QuestionType)
    fun onPickType(questionType: QuestionType)
    fun onSettingsDialog()
}