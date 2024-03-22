package quizresult

sealed interface QuizResultState {

    data class Success(val result: QuizResult): QuizResultState
    data object Loading: QuizResultState
    data class Error(val error: String): QuizResultState
}