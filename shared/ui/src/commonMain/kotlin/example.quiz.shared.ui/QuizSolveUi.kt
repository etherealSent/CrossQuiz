package example.quiz.shared.ui

import Answer
import Question
import QuizSolveState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import integration.QuizSolveComponent
import kotlinx.coroutines.Dispatchers

private val nextQuestionText: String = "Следующий вопрос"
private fun progressText(progress: Int) = "$progress заданий решено"
private fun questionNumberText(number: String) = "Вопрос $number"

// TODO ELDAR colors and paddings in separate theme file
@Composable
fun QuizSolveScreen(component: QuizSolveComponent) {
    val state by component.state.collectAsState(Dispatchers.Main.immediate)
    when (state) {
        is QuizSolveState.Loading -> {
            Loading(modifier = Modifier)
        }

        is QuizSolveState.Error -> {
            Error { component.onReloadClick() }
        }

        is QuizSolveState.QuizSolve -> {
            val quizState = state as QuizSolveState.QuizSolve
            if (quizState.isStarted) {
                Surface {
                    Scaffold(
                        topBar = {
                            ProgressBar(
                                progress = quizState.progressBar,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                        },
                        content = {
                            QuizMainContent(
                                question = quizState.currentQuestion,
                                modifier = Modifier.padding(it)
                            )
                        },
                        bottomBar = {
                            BottomNavigation(
                                onNextButtonClick = { component.onNextClick() },
                                isEnabled = quizState.canMoveForward,
                                text = nextQuestionText,
                            )
                        }
                    )
                }
            } else {
                // TODO вьюха перед началом теста
                Surface {
                    Scaffold(
                        content = {
                            MainContent(
                                quizSolveState = quizState,
                                modifier = Modifier.padding(it),
                            )
                        },
                        bottomBar = {
                            BottomNavigation(
                                onNextButtonClick = { component.onNextClick() },
                                isEnabled = true,
                                text = "Начать тест"
                            )
                        }
                    )
                }
            }
        }
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
    onRetryClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Произошла ошибка, попробуйте снова",
                textAlign = TextAlign.Center,
            )
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
                content = {
                    Text(text = "Reload")
                },
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun QuizMainContent(
    question: Question,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
    ) {
        QuestionText(
            description = question.questionText,
            questionNumber = question.questionNumber,
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (question) {
            is Question.Single -> SingleAnswerList(answers = question.answers)
            //is Question.Multiple -> MultipleAnswerList(answers = question.answers)
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier,
    quizSolveState: QuizSolveState.QuizSolve,
) {
    Column(modifier = modifier) {
        Text(quizSolveState.quiz.id)
    }
}

@Composable
private fun ProgressBar(modifier: Modifier, progress: Float) {
    Column(
        modifier = modifier
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            strokeCap = StrokeCap.Round,
        )
        Text(
            progressText((progress * 100).toInt()),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SingleAnswerList(answers: List<Answer>) {
    var currentChosenAnswer by remember { mutableStateOf(Answer.Id("-1")) }
    LazyColumn {
        itemsIndexed(items = answers) { index, item ->
            AnswerTextItem(
                item = item,
                isChosen = currentChosenAnswer == item.id,
                isSingle = true,
                onClick = { currentChosenAnswer = it.id },
            )
        }
    }
}
//    when (answers) {
//        is Answers.TextItems -> {
//            LazyColumn {
//                itemsIndexed(items = answers.items) { index, item ->
//                    AnswerTextItem(
//                        item = item,
//                        isChosen = currentChosenAnswer == item.id,
//                        isSingle = true,
//                        onClick = { currentChosenAnswer = it.id },
//                    )
//                }
//            }
//        }
//
//        is Answers.ImageItems -> {
//            LazyColumn {
//                itemsIndexed(items = answers.items) { index, item ->
//                    AnswerImageItem(
//                        item = item,
//                        isChosen = currentChosenAnswer == item.id,
//                        isSingle = true,
//                        onClick = {
//                            currentChosenAnswer = it.id
//                        },
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
private fun MultipleAnswerList(answers: List<Answer>) {
    val currentChosenAnswers = remember { mutableStateListOf<Answer.Id>() }
    LazyColumn {
        itemsIndexed(items = answers) { index, item ->
            AnswerTextItem(
                item = item,
                isChosen = currentChosenAnswers.contains(item.id),
                isSingle = false,
                onClick = {
                    if (currentChosenAnswers.contains(it.id)) {
                        currentChosenAnswers.remove(it.id)
                    } else {
                        currentChosenAnswers.add(it.id)
                    }
                },
            )
        }
    }
//    when (answers) {
//        is Answers.TextItems -> {
//            LazyColumn {
//                itemsIndexed(items = answers.items) { index, item ->
//                    AnswerTextItem(
//                        item = item,
//                        isChosen = currentChosenAnswers.contains(item.id),
//                        isSingle = false,
//                        onClick = {
//                            if (currentChosenAnswers.contains(it.id)) {
//                                currentChosenAnswers.remove(it.id)
//                            } else {
//                                currentChosenAnswers.add(it.id)
//                            }
//                        },
//                    )
//                }
//            }
//        }
//
//        is Answers.ImageItems -> {
//            LazyRow {
//                itemsIndexed(items = answers.items) { index, item ->
//                    AnswerImageItem(
//                        item = item,
//                        isChosen = currentChosenAnswers.contains(item.id),
//                        isSingle = false,
//                        onClick = {
//                            if (currentChosenAnswers.contains(it.id)) {
//                                currentChosenAnswers.remove(it.id)
//                            } else {
//                                currentChosenAnswers.add(it.id)
//                            }
//                        },
//                    )
//                }
//            }
//        }
//    }
}

@Composable
private fun AnswerTextItem(
    item: Answer,
    onClick: (Answer) -> Unit,
    isChosen: Boolean,
    isSingle: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = isChosen,
                    onClick = { onClick(item) },
                )
                .padding(end = 16.dp, top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val icon = when {
                isChosen && !isSingle -> Icons.Default.Check
                isChosen && isSingle -> Icons.Default.CheckCircle
                !isChosen && isSingle -> Icons.Default.CheckCircle
                else -> Icons.Default.Check
            }
            IconToggleButton(
                modifier = Modifier.clickable { onClick(item) },
                checked = isChosen,
                onCheckedChange = { },
                content = {
                    Icon(
                        modifier = Modifier.clickable { onClick(item) },
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            )
            Text(
                modifier = Modifier.weight(1f),
                text = item.text
            )
        }
    }
}

//@Composable
//private fun AnswerImageItem(
//    item: Answer.Image,
//    onClick: (Answer.Image) -> Unit,
//    isChosen: Boolean,
//    isSingle: Boolean,
//) {
//    Surface(
//        shape = MaterialTheme.shapes.small,
//        border = BorderStroke(
//            width = 1.dp,
//            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
//        ),
//        modifier = Modifier.padding(vertical = 8.dp)
//    ) {
//        Column(
//            modifier = Modifier.clickable(onClick = { onClick(item) }),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//             TODO ELDAR why not working
//            AsyncImage(
//                modifier = Modifier
//                    .size(200.dp)
//                    .padding(vertical = 10.dp),
//                contentScale = ContentScale.Crop,
//                model = item.url,
//                contentDescription = null,
//            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                IconToggleButton(
//                    modifier = Modifier.clickable { onClick(item) },
//                    checked = isChosen,
//                    onCheckedChange = { },
//                    content = {
//                        Icon(
//                            modifier = Modifier.clickable { onClick(item) },
//                            painter = painterResource(
//                                when {
//                                    isChosen && !isSingle -> R.drawable.baseline_check_box_24
//                                    isChosen && isSingle -> R.drawable.baseline_check_circle_24
//                                    !isChosen && isSingle -> R.drawable.outline_circle_24
//                                    else -> R.drawable.baseline_check_box_outline_blank_24
//                                }
//                            ),
//                            contentDescription = null,
//                            tint = MaterialTheme.colors.primary,
//                        )
//                    }
//                )
//                if (item.text != null) {
//                    Text(
//                        modifier = Modifier.weight(1f),
//                        text = item.text,
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
private fun QuestionText(
    modifier: Modifier = Modifier,
    description: String,
    questionNumber: String,
) {
    val backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.06f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = questionNumberText(questionNumber),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    onNextButtonClick: () -> Unit,
    isEnabled: Boolean,
    text: String,
) {
    Button(
        enabled = isEnabled,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
        onClick = onNextButtonClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colors.onPrimary,
            // change color
            disabledBackgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f),
        )
    ) {
        Text(text = text)
    }
}