package example.quiz.shared.quizEdit.editThemes

import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.ThemeListItem

interface EditThemes {

    val models: Value<Model>

    fun onEditThemeClicked()
    fun onAddThemeClicked()
    fun onCreateTheme()
    fun onSearchChanged(search: String)
    fun onThemeClicked(themeId: Long, temporary: Boolean)
    fun getThemeItems(themeIds: List<String>)
    fun onDismissRequest()

    data class Model(
        val themeItems: List<ThemeListItem>,
        val temporaryThemeItems: List<ThemeListItem>,
        val checkedThemeItems: List<ThemeListItem>,
        val search: String,
        val showDialog: Boolean
    )

}