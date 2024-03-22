import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeQuizSolveComponent: QuizSolveComponent {
    override val currentQuiz: StateFlow<Quiz?>
        get() = MutableStateFlow(null)
    override val currentQuestion: StateFlow<Question>
        get() = MutableStateFlow(Question.Single(
            id = "1",
            questionText = "Какой язык является основным для андроид разработки в 2023 году?",
            answers = Answers.TextItems(
                listOf(
                    Answer.Text(
                        id = Answer.Id("1"),
                        text = "KotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlinKotlin"
                    ),
                    Answer.Text(
                        id = Answer.Id("2"),
                        text = "Java"
                    ),
                    Answer.Text(
                        id = Answer.Id("3"),
                        text = "C++"
                    ),
                    Answer.Text(
                        id = Answer.Id("4"),
                        text = "Python"
                    ),
                    Answer.Text(
                        id = Answer.Id("5"),
                        text = "Swift"
                    ),
                    Answer.Text(
                        id = Answer.Id("6"),
                        text = "C#"
                    ),
                ),
            ),
            correctAnswerId = Answer.Id("1"),
            questionNumber = "2",
        ))

    override fun onNextButtonClick() = Unit
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun QuizSolveScreenPreview() {
    QuizSolveScreen(FakeQuizSolveComponent())
}