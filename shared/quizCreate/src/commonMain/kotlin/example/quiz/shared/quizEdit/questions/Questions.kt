package example.quiz.shared.quizEdit.questions

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.Question


interface Questions {

    val models: Value<Model>

    data class Model(
        val questions: List<Question>,
        val selectedQuestionId: Long = 0L
    )
    fun onCreateQuestionClicked(questionType: QuestionType)
    fun onCreateAnswerClicked()
    fun onQuestionSelected(id: Long)
    fun onQuestionChanged(question: String)
    fun onRightAnswerPicked(answerId: Long)
    fun  onAnswerDeleteClicked(id: Long)
    fun onAnswerChanged(id: Long, answer: String)
    fun resetSelectedQuestionId()
}