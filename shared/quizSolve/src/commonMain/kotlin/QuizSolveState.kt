
sealed interface QuizSolveState {
    data class QuizSolve(
        val quiz: Quiz,
        val currentQuestion: Question,
        val currentChosenAnswers: List<Answer>,
        val chosenAnswers: HashMap<Question, List<Answer>>,
    ): QuizSolveState

    data object Initial: QuizSolveState
}