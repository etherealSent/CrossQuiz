package quizresult

import kotlinx.coroutines.flow.StateFlow

class QuizResultComponentImpl: QuizResultComponent {

    override val quizResultState: StateFlow<QuizResultState>
        get() = TODO("Not yet implemented")

    override fun finishQuiz() {
        TODO("Not yet implemented")
    }

    override fun openFeedback() {
        TODO("Not yet implemented")
    }

    override fun retry() {
        TODO("Not yet implemented")
    }
}
