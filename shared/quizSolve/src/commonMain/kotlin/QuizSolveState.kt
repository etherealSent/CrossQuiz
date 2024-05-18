
sealed interface QuizSolveState {
    data class QuizSolve(
        val quiz: QuizModel,
        val currentQuestion: Question,
        val currentChosenAnswer: Answer?,
        val chosenAnswers: HashMap<Question, Answer>,
        val isStarted: Boolean = false,
    ): QuizSolveState

    data object Loading: QuizSolveState

    data object Error: QuizSolveState
}