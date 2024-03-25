package example.quiz.shared.quizlist.store

import com.arkivanov.mvikotlin.core.store.Store
import example.quiz.shared.quizlist.QuizListItem

interface QuizListStore : Store<QuizListStore.Intent, QuizListStore.State, Nothing> {
    sealed class Intent {
        object AddItem : Intent()
    }
    data class State(
        val items: List<QuizListItem> = emptyList()
    )
}