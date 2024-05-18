package store

import Answer
import QuizSolveState
import com.arkivanov.mvikotlin.core.store.Store

interface QuizSolveStore : Store<QuizSolveStore.Intent, QuizSolveState, Nothing> {

    sealed interface Intent {

        sealed interface QuizSolveIntent : Intent {

            data object NextQuestion : QuizSolveIntent

            data class AnswerClick(val answer: Answer) : QuizSolveIntent

        }

        data class LoadQuiz(val id: Int) : Intent
    }
}