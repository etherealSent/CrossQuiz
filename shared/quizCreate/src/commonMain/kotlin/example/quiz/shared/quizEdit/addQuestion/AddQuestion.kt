package example.quiz.shared.quizEdit.addQuestion

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.QuestionItem
import example.quiz.shared.quizlist.QuestionType

interface AddQuestion {
    val models: Value<Model>

    data class Model(
        val showDialog: Boolean,
        val questionType: QuestionType,
        val question: QuestionItem,
        val questionTypes: List<QuestionType>
    )

    fun onAddQuestionClicked()
    fun onQuestionTypeClicked(type: QuestionType)
    fun onDismissCreateQuestion()

}