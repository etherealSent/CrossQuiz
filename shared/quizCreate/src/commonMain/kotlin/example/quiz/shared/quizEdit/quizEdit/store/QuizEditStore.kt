package example.quiz.shared.quizEdit.quizEdit.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.QuestionItem
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup

interface QuizEditStore : Store<QuizEditStore.Intent, QuizEditStore.State, QuizEditStore.Label> {

    sealed class Intent {
        data class SetTitle(val title: String) : Intent()
        data class SetDescription(val description: String) : Intent()
        object ShowMoreVertMenu : Intent()
        data class UpdateThemes(val themeList: List<String>) : Intent()
    }

    data class State(
        val id: Long = 0L,
        val orderId: Long = 0L,
        val orderNum: Long = 0L,
        val setup: Setup = Setup.Default,
        val title: String = "",
        val description: String = "",
        val themeList: List<String> = listOf(),
        val questionItems: List<QuestionItem> = listOf(),
        val selectedQuestionId: Long = 0L,
        val showMoreVertMenu: Boolean = false
    )

    sealed class Label {
        data class Changed(val item: QuizListItem) : Label()
    }
}