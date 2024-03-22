package quizresult

import kotlinx.coroutines.flow.StateFlow

interface QuizResultComponent {

    val quizResultState: StateFlow<QuizResultState>

    fun finishQuiz()

    fun openFeedback()

    fun retry()
}