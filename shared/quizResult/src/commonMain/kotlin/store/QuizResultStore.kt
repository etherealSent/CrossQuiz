package store

import com.arkivanov.mvikotlin.core.store.Store
import QuizResultState

// executor - бизнес логика
// reducer - преобразование старого стейта в новый по месседжам
interface QuizResultStore: Store<QuizResultStore.Intent, QuizResultState, Nothing> {

    sealed interface Intent {
        data class Load(val id: String): Intent
        data object FinishQuiz: Intent
        data object SendFeedback: Intent
    }
}