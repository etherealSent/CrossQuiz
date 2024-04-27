package example.quiz.shared.quizEdit.question.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.Settings

interface QuestionStore : Store<QuestionStore.Intent, QuestionStore.State, Nothing> {
    sealed class Intent {
        data class SetQuestion(val question: String) : Intent()
        data class PickAnswer(val answerId: Long) : Intent()
        data class DeleteAnswer(val id: Long) : Intent()
        data class EditAnswer(val answer: Answer) : Intent()

        data class ShowChangeTypeDialog(val questionType: QuestionType) : Intent()
        object CloseChangeTypeDialog : Intent()
        data class ChangeType(val type: QuestionType) : Intent()
        data class SelectType(val type: QuestionType) : Intent()

        object ShowSettingsDialog : Intent()
        object CloseSettingsDialog : Intent()
    }
    data class State(
        val questionType: QuestionType = QuestionType.OneAnswerQuestion,
        val dialogType: QuestionType = QuestionType.OneAnswerQuestion,
        val question: String = "",
        val answers: List<Answer> = listOf(),
        val rightAnswers: List<Answer> = listOf(),
        val settings: Settings = Settings.Default,
        val showChangeTypeDialog : Boolean = false,
        val showSettingsDialog : Boolean = false
    )
}