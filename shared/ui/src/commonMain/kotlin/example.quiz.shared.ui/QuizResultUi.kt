package example.quiz.shared.ui

//@Composable
//fun QuizResultScreen(component: QuizResultComponent) {
//    val result by component.quizResultState.collectAsState(Dispatchers.Main.immediate)
//
//    when (result) {
//        is QuizResultState.Success -> Success(
//            state = result as QuizResultState.Success,
//            onFeedbackClick = { component.openFeedback() },
//            onFinishClick = { component.finishQuiz() },
//        )
//
//        is QuizResultState.Loading -> Loading(modifier = Modifier)
//        is QuizResultState.Error -> Error(
//            state = result as QuizResultState.Error,
//            onRetryClick = { component.retry() },
//        )
//    }
//}
//
//@Composable
//private fun Success(
//    state: QuizResultState.Success,
//    onFeedbackClick: () -> Unit,
//    onFinishClick: () -> Unit,
//) {
//    Surface {
//        Scaffold(
//            content = {
//                Result(modifier = Modifier.padding(it), state)
//            },
//            bottomBar = {
//                BottomNavigation(
//                    onFinishClick = onFinishClick,
//                    onFeedbackClick = onFeedbackClick,
//                )
//            }
//        )
//    }
//}
//
//@Composable
//private fun Loading(modifier: Modifier) {
//    Box(modifier = modifier.fillMaxSize()) {
//        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//    }
//}
//
//@Composable
//private fun Error(
//    state: QuizResultState.Error,
//    onRetryClick: () -> Unit,
//) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.align(Alignment.Center)) {
//            Text(
//                modifier = Modifier.fillMaxWidth(),
//                text = state.error,
//                textAlign = TextAlign.Center,
//            )
//            DefaultButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
//                content = {
//                    Text(text = stringResource(R.string.quiz_result_error_button))
//                },
//                onClick = onRetryClick,
//            )
//        }
//    }
//}
//
//@Composable
//private fun Result(
//    modifier: Modifier,
//    state: QuizResultState.Success,
//) {
//    val color = getColor(state.result.assessment)
//    Box(modifier = modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.align(Alignment.Center),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Text(
//                stringResource(R.string.quiz_result_main),
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.h3,
//            )
//            ResultCell {
//                MultiStyleTextScore(
//                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp),
//                    currentScore = state.result.currentScore,
//                    maxScore = state.result.maxScore,
//                    color = color,
//                )
//            }
//            ResultCell {
//                MultiStyleTextAnswers(
//                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp),
//                    currentAnswer = state.result.currentScore,
//                    maxAnswer = state.result.maxScore,
//                    color = color,
//                )
//            }
//            BottomResult(state.result.assessment)
//        }
//    }
//}
//
//@Composable
//private fun BottomResult(assessment: QuizResult.Assessment) {
//    when (assessment) {
//        QuizResult.Assessment.EXCELLENT,
//        QuizResult.Assessment.GOOD -> {
//            Text(stringResource(R.string.quiz_result_congratulations))
//            MultiStyleTextBottomResult(
//                modifier = Modifier,
//                assessment = assessment,
//            )
//        }
//
//        QuizResult.Assessment.BAD -> {
//            Text(stringResource(R.string.quiz_result_bottom_result_bad))
//        }
//    }
//}
//
//@Composable
//private fun ResultCell(
//    content: @Composable () -> Unit,
//) {
//    Surface(
//        shape = RoundedCornerShape(20.dp),
//        border = BorderStroke(
//            width = 1.dp,
//            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
//        ),
//        modifier = Modifier.width(300.dp).padding(vertical = 8.dp)
//    ) {
//        content.invoke()
//    }
//}
//
//@Composable
//private fun BottomNavigation(
//    onFinishClick: () -> Unit,
//    onFeedbackClick: () -> Unit,
//) {
//    Column {
//        DefaultButton(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
//            content = {
//                Text(text = stringResource(R.string.quiz_result_feedback))
//            },
//            onClick = onFeedbackClick
//        )
//
//        DefaultButton(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
//            content = {
//                Text(text = stringResource(R.string.quiz_result_finish))
//            },
//            onClick = onFinishClick
//        )
//    }
//}
//
//@Composable
//private fun MultiStyleTextScore(
//    modifier: Modifier,
//    currentScore: Int,
//    maxScore: Int,
//    color: Color,
//) {
//    Text(
//        buildAnnotatedString {
//            append(stringResource(R.string.quiz_result_your_result))
//            withStyle(style = SpanStyle(color = color)) {
//                append("$currentScore")
//            }
//            append(" ")
//            append(stringResource(R.string.quiz_result_out_of, maxScore))
//            append(" ")
//            append(stringResource(R.string.quiz_result_scores))
//        },
//        textAlign = TextAlign.Center,
//        modifier = modifier,
//    )
//}
//
//@Composable
//private fun MultiStyleTextAnswers(
//    modifier: Modifier,
//    currentAnswer: Int,
//    maxAnswer: Int,
//    color: Color,
//) {
//    Text(
//        buildAnnotatedString {
//            append(stringResource(R.string.quiz_result_correct_answers))
//            withStyle(style = SpanStyle(color = color)) {
//                append("$currentAnswer")
//            }
//            append(" ")
//            append(stringResource(R.string.quiz_result_out_of, maxAnswer))
//        },
//        textAlign = TextAlign.Center,
//        modifier = modifier,
//    )
//}
//
//@Composable
//private fun MultiStyleTextBottomResult(
//    modifier: Modifier,
//    assessment: QuizResult.Assessment,
//) {
//    val color = getColor(assessment)
//    Text(
//        buildAnnotatedString {
//            append(stringResource(R.string.quiz_result_bottom_result_you))
//            append(" ")
//            withStyle(style = SpanStyle(color = color)) {
//                when (assessment) {
//                    QuizResult.Assessment.EXCELLENT ->
//                        append(stringResource(R.string.quiz_result_bottom_result_great))
//
//                    QuizResult.Assessment.GOOD ->
//                        append(stringResource(R.string.quiz_result_bottom_result_good))
//
//                    QuizResult.Assessment.BAD -> Unit
//                }
//            }
//            append(" ")
//            append(stringResource(R.string.quiz_result_bottom_result))
//        },
//        textAlign = TextAlign.Center,
//        modifier = modifier,
//    )
//}
//
//@Composable
//private fun DefaultButton(
//    modifier: Modifier,
//    content: @Composable () -> Unit,
//    onClick: () -> Unit
//) {
//    Button(
//        shape = RoundedCornerShape(20.dp),
//        modifier = modifier,
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
//            contentColor = MaterialTheme.colors.onPrimary,
//        )
//    ) {
//        content.invoke()
//    }
//}
//
//private fun getColor(assessment: QuizResult.Assessment): Color =
//    when (assessment) {
//        // TODO ELDAR add colors in UI system, change yellow color
//        QuizResult.Assessment.EXCELLENT -> Color.Green
//        QuizResult.Assessment.GOOD -> Color.Yellow
//        QuizResult.Assessment.BAD -> Color.Red
//    }