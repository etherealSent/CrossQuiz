package example.quiz.shared.quizEdit.quizEdit

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.Question
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.Setup

interface QuizEdit {

    val models: Value<Model>

    data class Model(
        val id: Long = 0L,
        val orderId: Long = 0L,
        val orderNum: Long = 0L,
        val setup: Setup = Setup.Default,
        val title: String = "",
        val description: String = "",
        val themeList: List<String> = listOf(),
        val showMoreVertMenu: Boolean = false
    )
    fun onBackClicked()
    fun onTitleChanged(title: String)
    fun onDescriptionChanged(description: String)
    fun onQuizThemeClicked()
    fun onSettingsClicked()
    fun onMoreVertClicked()
    fun updateThemes(themeList: List<String>)

    sealed class Output {
        object QuizTheme : Output()
        object QuizList : Output()
        object QuizSettings : Output()
    }
}