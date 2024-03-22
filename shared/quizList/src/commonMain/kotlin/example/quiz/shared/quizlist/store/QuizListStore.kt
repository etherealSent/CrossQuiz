package example.quiz.shared.quizlist.store

import example.quiz.shared.quizlist.QuizListItem

internal interface QuizMainStore {

    sealed class Intent {
        object AddItem : Intent()
    }

    data class State(
        val items: List<QuizListItem> = emptyList()
    )

}