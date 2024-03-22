data class QuizSolveState(
    val quiz: Quiz,
    val currentQuestion: Question,
    val currentChosenAnswers: List<Answer>,
    val chosenAnswers: HashMap<Question, List<Answer>>,
)