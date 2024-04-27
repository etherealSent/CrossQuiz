package example.quiz.shared.quizEdit.editThemes.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.quiz.dialog.store.QuizDialogStore
import example.quiz.shared.quizlist.ThemeListItem

interface EditThemesStore : Store<EditThemesStore.Intent, EditThemesStore.State, Nothing> {

    sealed class Intent {
        object ShowDialog : Intent()
        data class SetSearch(val search: String) : Intent()
        object AddTemporaryTheme : Intent()
        data class AddTheme(val themeId: Long, val temporary: Boolean) : Intent()
        object CloseDialog : Intent()
        object CreateTheme : Intent()
        data class GetThemeItems(val themeIds: List<String>) : Intent()
    }

    data class State(
        val themeItems: List<ThemeListItem> = listOf(),
        val temporaryThemeItems: List<ThemeListItem> = listOf(),
        val checkedThemeItems: List<ThemeListItem> = listOf(),
        val search: String = "",
        val showDialog: Boolean = false
    )
}