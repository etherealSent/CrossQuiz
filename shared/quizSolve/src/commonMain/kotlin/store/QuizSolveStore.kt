package store

import Answer
import QuizSolveState
import com.arkivanov.mvikotlin.core.store.Store

interface QuizSolveStore : Store<QuizSolveStore.Intent, QuizSolveState, Nothing> {

    sealed interface Intent {
        data object NextQuestion : Intent

        data class AnswerClick(val answer: Answer) : Intent
    }
}