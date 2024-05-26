
sealed interface QuizSolveState {
    data class QuizSolve(
        val quiz: QuizModel,
        val currentQuestion: Question,
        val currentChosenAnswer: Answer?,
        val chosenAnswers: HashMap<Question, Answer>,
        val questionIndex: Int = 0,
        val isStarted: Boolean = false,
        val canMoveForward: Boolean = false,
        val progressBar: Float = 0f,
    ): QuizSolveState

    data object Loading: QuizSolveState

    data object Error: QuizSolveState
}