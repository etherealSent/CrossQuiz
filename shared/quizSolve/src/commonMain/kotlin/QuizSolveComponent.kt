import kotlinx.coroutines.flow.StateFlow

interface QuizSolveComponent {

    val currentQuiz: StateFlow<Quiz?>

    val currentQuestion: StateFlow<Question>
    fun onNextButtonClick()

}