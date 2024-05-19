package example.quiz.shared.quizEdit.addQuestion.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.QuestionType

interface AddQuestionStore : Store<AddQuestionStore.Intent, AddQuestionStore.State, Nothing> {

    sealed class Intent {
        object ShowDialog : Intent()
        data class SetType(val type: QuestionType) : Intent()
        object DismissDialog : Intent()
        data class SetQuestionTypes(val questionTypes: List<QuestionType>) : Intent()
    }

    data class State(
        val showDialog: Boolean = false,
        val questionType: QuestionType = QuestionType.OneOptionQuestion,
        val questionTypes: List<QuestionType> = listOf()
    )
}