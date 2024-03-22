import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class QuizSolveComponentImpl(
    componentContext: ComponentContext
): QuizSolveComponent, ComponentContext by componentContext {

    override val currentQuiz: StateFlow<Quiz>
        get() = TODO("Not yet implemented")
    override val currentQuestion: StateFlow<Question>
        get() = TODO("Not yet implemented")

    override fun onNextButtonClick() {
        TODO("Not yet implemented")
    }
}