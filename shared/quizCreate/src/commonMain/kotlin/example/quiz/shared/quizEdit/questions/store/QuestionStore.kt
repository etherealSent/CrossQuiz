package example.quiz.shared.quizEdit.questions.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.Question
import example.quiz.shared.quizlist.QuestionType

interface QuestionStore : Store<QuestionStore.Intent, QuestionStore.State, Nothing> {
    sealed class Intent {
        data class CreateQuestion(val questionType: QuestionType) : Intent()
        data class SelectQuestion(val id: Long) : Intent()
        data class SetQuestion(val question: String) : Intent()
        data object CreateAnswer : Intent()
        data class PickAnswer(val answerId: Long) : Intent()
        data class DeleteAnswer(val id: Long) : Intent()
        data class EditAnswer(val id: Long, val answer: String) : Intent()

    }
    data class State(
        val questions: List<Question> = listOf(),
        val selectedQuestionId: Long = 0L
    )
}