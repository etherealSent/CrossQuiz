package example.quiz.shared.quizlist.dialogQuizCreate

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup
import example.quiz.shared.quizlist.ThemeListItem

interface QuizDialog {
    val models: Value<Model>

    fun onAddQuizClicked()

    fun onDialogDismissRequest()

    fun onBackClicked()

    fun onNextClicked()

//    fun onSetupChanged(setup: Setup)

    fun onInputTitleChanged(title: String)

    fun onSearchThemeChanged(search: String)

    fun addTemporaryTheme()

    fun addThemeClicked()

    fun onThemeClicked(themeId: Long, temporary: Boolean)


    fun createQuiz()


    data class Model(
        val showDialog: Boolean,
        val currentDialogScreenId: Long,
        val title: String,
        val search: String,
        val themeItems: List<ThemeListItem>,
        val checkedThemeItems: List<ThemeListItem>,
        val quizItems: List<QuizListItem>,
        val temporaryThemeItems: List<ThemeListItem>
    )

}