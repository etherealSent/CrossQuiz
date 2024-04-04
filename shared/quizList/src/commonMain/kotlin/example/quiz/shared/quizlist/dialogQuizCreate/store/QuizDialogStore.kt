package com.example.quiz.dialog.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup
import example.quiz.shared.quizlist.ThemeListItem

interface QuizDialogStore : Store<QuizDialogStore.Intent, QuizDialogStore.State, Nothing> {

    sealed class Intent {
        data class SetTitle(val title: String) : Intent()
        data class SetSearch(val search: String) : Intent()
        data class SetupQuiz(val setup: Setup) : Intent()
        data class PickTheme(val themeId: Long, val temporary: Boolean) : Intent()
        object CreateQuiz : Intent()
        object ShowDialog : Intent()
        object CloseDialog : Intent()
        object OnNextClicked : Intent()
        object OnBackClicked : Intent()
        object CreateTheme : Intent()
        object AddTemporaryTheme : Intent()
    }

    data class State(
        val showDialog: Boolean = false,
        val currentDialogScreenId: Long = 0,
        val quizItems: List<QuizListItem> = listOf(),
        val quizItem: QuizListItem = QuizListItem(),
        val title: String = "",
        val search: String = "",
        val setup: Setup = Setup.Default,
        val themeItems: List<ThemeListItem> = listOf(),
        val checkedThemeItems: List<ThemeListItem> = listOf(),
        val temporaryThemeItems: List<ThemeListItem> = listOf()
    )
}

