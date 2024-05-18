package example.quiz.shared.ui

import QuizResult
import QuizResultState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import integration.QuizResultComponent

private val retryText = "Повторить"
private val resultText = "Результаты"
private val congratulationsText = "Поздравляем с завершением теста"
private val badCongratulationsText = "К несчастью, вы не набрали минимальный балл"
private val feedbackText = "Написать отзыв на тест"
private val finishText = "Завершить тест"

@Composable
fun QuizResultScreen(component: QuizResultComponent) {
    val result by component.state.collectAsState()

    when (result) {
        is QuizResultState.Success -> Success(
            state = result as QuizResultState.Success,
            onFeedbackClick = { },
            onFinishClick = { component.onFinishClick() },
        )

        is QuizResultState.Loading -> Loading(modifier = Modifier)
        is QuizResultState.Error -> Error(
            state = result as QuizResultState.Error,
            onRetryClick = { component.onReloadClick() },
        )
    }
}

@Composable
private fun Success(
    state: QuizResultState.Success,
    onFeedbackClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    Surface {
        Scaffold(
            content = {
                Result(modifier = Modifier.padding(it), state)
            },
            bottomBar = {
                BottomNavigation(
                    onFinishClick = onFinishClick,
                    onFeedbackClick = onFeedbackClick,
                )
            }
        )
    }
}

@Composable
private fun Loading(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun Error(
    state: QuizResultState.Error,
    onRetryClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.error,
                textAlign = TextAlign.Center,
            )
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
                content = {
                    Text(text = retryText)
                },
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun Result(
    modifier: Modifier,
    state: QuizResultState.Success,
) {
    val color = getColor(state.result.assessment)
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = resultText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
            )
            ResultCell {
                MultiStyleTextScore(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp),
                    currentScore = state.result.currentScore,
                    maxScore = state.result.maxScore,
                    color = color,
                )
            }
            ResultCell {
                MultiStyleTextAnswers(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp),
                    currentAnswer = state.result.currentScore,
                    maxAnswer = state.result.maxScore,
                    color = color,
                )
            }
            BottomResult(state.result.assessment)
        }
    }
}

@Composable
private fun BottomResult(assessment: QuizResult.Assessment) {
    when (assessment) {
        QuizResult.Assessment.EXCELLENT,
        QuizResult.Assessment.GOOD -> {
            Text(congratulationsText)
            MultiStyleTextBottomResult(
                modifier = Modifier,
                assessment = assessment,
            )
        }

        QuizResult.Assessment.BAD -> {
            Text(badCongratulationsText)
        }
    }
}

@Composable
private fun ResultCell(
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
        ),
        modifier = Modifier.width(300.dp).padding(vertical = 8.dp)
    ) {
        content.invoke()
    }
}

@Composable
private fun BottomNavigation(
    onFinishClick: () -> Unit,
    onFeedbackClick: () -> Unit,
) {
    Column {
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
            content = {
                Text(text = feedbackText)
            },
            onClick = onFeedbackClick
        )

        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
            content = {
                Text(text = finishText)
            },
            onClick = onFinishClick
        )
    }
}

@Composable
private fun MultiStyleTextScore(
    modifier: Modifier,
    currentScore: Int,
    maxScore: Int,
    color: Color,
) {
    Text(
        buildAnnotatedString {
            append("Ваш результат (")
            withStyle(style = SpanStyle(color = color)) {
                append("$currentScore")
            }
            append(" $maxScore баллов")
        },
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun MultiStyleTextAnswers(
    modifier: Modifier,
    currentAnswer: Int,
    maxAnswer: Int,
    color: Color,
) {
    Text(
        buildAnnotatedString {
            append("Правильных ответов (")
            withStyle(style = SpanStyle(color = color)) {
                append("$currentAnswer")
            }
            append(" $maxAnswer")
        },
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun MultiStyleTextBottomResult(
    modifier: Modifier,
    assessment: QuizResult.Assessment,
) {
    val color = getColor(assessment)
    Text(
        buildAnnotatedString {
            append("У вас ")
            withStyle(style = SpanStyle(color = color)) {
                when (assessment) {
                    QuizResult.Assessment.EXCELLENT ->
                        append("отличный")

                    QuizResult.Assessment.GOOD ->
                        append("хороший")

                    QuizResult.Assessment.BAD -> Unit
                }
            }
            append(" результат")
        },
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
internal fun DefaultButton(
    modifier: Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colors.onPrimary,
        )
    ) {
        content.invoke()
    }
}

private fun getColor(assessment: QuizResult.Assessment): Color =
    when (assessment) {
        // TODO ELDAR add colors in UI system, change yellow color
        QuizResult.Assessment.EXCELLENT -> Color.Green
        QuizResult.Assessment.GOOD -> Color.Yellow
        QuizResult.Assessment.BAD -> Color.Red
    }