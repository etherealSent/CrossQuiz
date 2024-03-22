package com.example.quiz.dialog.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.Setup

interface QuizDialogStore : Store<QuizDialogStore.Intent, QuizDialogStore.State, Nothing> {

    sealed class Intent {
        data class SetTitle(val title: String) : Intent()
        data class SetupQuiz(val setup: Setup) : Intent()
        data class PickTheme(val id: Long, val theme: String) : Intent()
        object CreateQuiz : Intent()
    }

    data class State(
        val quizList: List<QuizListItem> = listOf(),
        val title: String  = "",
        val setup: Setup = Setup.Default,
        val themeList: List<String> = listOf()
    )
}

