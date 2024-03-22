package quizresult

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeQuizResultComponentSuccess : QuizResultComponent {

    override val quizResultState: StateFlow<QuizResultState>
        get() = MutableStateFlow(
            QuizResultState.Success(
                QuizResult(
                    currentScore = 9,
                    maxScore = 10,
                    rightQuestion = 9,
                    maxQuestion = 10,
                    assessment = QuizResult.Assessment.EXCELLENT,
                )
            )
        )

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

class FakeQuizResultComponentLoading : QuizResultComponent {

    override val quizResultState: StateFlow<QuizResultState>
        get() = MutableStateFlow(
            QuizResultState.Loading
        )

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

