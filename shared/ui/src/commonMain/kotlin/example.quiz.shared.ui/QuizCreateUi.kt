package example.quiz.shared.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.quiz.dialog.CreateItemOfList
import com.example.quiz.dialog.ItemOfList
import example.quiz.shared.quizEdit.addQuestion.integration.AddQuestionComponent
import example.quiz.shared.quizEdit.editThemes.integration.EditThemesComponent
import example.quiz.shared.quizEdit.question.integration.QuestionComponent
import example.quiz.shared.quizEdit.quizEdit.integration.QuizEditComponent
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionItem
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.ThemeListItem

//
//
@Composable
fun QuizCreateContent(
    quizEditComponent: QuizEditComponent,
    questionComponent: QuestionComponent,
    addQuestionComponent: AddQuestionComponent,
    editThemesComponent: EditThemesComponent
) {
    val quizEditModel by quizEditComponent.models.subscribeAsState()
    val editThemesModel by editThemesComponent.models.subscribeAsState()
    val addQuestionModel by addQuestionComponent.models.subscribeAsState()

    if (editThemesModel.showDialog) {
        EditThemeDialog(
            search = editThemesModel.search,
            onDismissRequest = {
                editThemesComponent.onDismissRequest()
                editThemesComponent.onCreateTheme()
                quizEditComponent.updateThemes(editThemesModel.checkedThemeItems.map { it.id.toString() })
                               },
            onSearchChanged = editThemesComponent::onSearchChanged,
            onAddThemeClicked = editThemesComponent::onAddThemeClicked,
            onThemeClicked = editThemesComponent::onThemeClicked,
            themeItems = editThemesModel.themeItems,
            temporaryThemeItems = editThemesModel.temporaryThemeItems,
            checkedThemeItems = editThemesModel.checkedThemeItems
        )
    }

    if (addQuestionModel.showDialog) {
        DialogCreateQuestion(
            onDismissRequest = addQuestionComponent::onDismissCreateQuestion,
//            onCreateQuestion = addQuestionComponent::onCreateQuestionClicked,
            onQuestionTypeClicked = addQuestionComponent::onQuestionTypeClicked,
            questionType = addQuestionModel.questionType,
            questionTypes = addQuestionModel.questionTypes
        )
    }

    Scaffold(topBar = {
        TopBar(
            onBackClicked = quizEditComponent::onBackClicked,
            onSettingsClicked = quizEditComponent::onSettingsClicked,
            onMoreVertClicked = quizEditComponent::onMoreVertClicked
        )
    },
        floatingActionButton = { FAB(onClick = addQuestionComponent::onAddQuestionClicked) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn {
                item {
                    QuizTopDescription(
                        title = quizEditModel.title,
                        description = quizEditModel.description,
                        themeList = editThemesModel.checkedThemeItems,
                        onTitleChanged = quizEditComponent::onTitleChanged,
                        onDescriptionChanged = quizEditComponent::onDescriptionChanged,
                        onThemeClicked = editThemesComponent::onEditThemeClicked,
                    )
                }
            }
        }
    }
}


@Composable
private fun TopBar(
    onBackClicked: () -> Unit, onSettingsClicked: () -> Unit, onMoreVertClicked: () -> Unit
) {
    TopAppBar(backgroundColor = Color(0xFFD0BCFF), title = { Text(text = "") }, navigationIcon = {
        IconButton(onClick = onBackClicked) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF1D1B20))
        }
    }, actions = {
        IconButton(onClick = onSettingsClicked) {
            Icon(Icons.Filled.Settings, contentDescription = "Back", tint = Color(0xFF49454F))
        }
        IconButton(onClick = onMoreVertClicked) {
            Icon(Icons.Filled.MoreVert, contentDescription = "Back", tint = Color(0xFF49454F))
        }
    }, elevation = 0.dp
    )
}

@Composable
private fun FAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.size(65.dp).offset(x = 6.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF1F1F1),
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun QuizTopDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    themeList: List<ThemeListItem>,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onThemeClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().background(
            Color(0xFFD0BCFF), shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
        ).padding(start = 10.dp)
    ) {
        TextField(value = title,
            onValueChange = onTitleChanged,
            textStyle = TextStyle(fontSize = 28.sp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Название теста", fontSize = 28.sp) })
        TextField(value = description,
            onValueChange = onDescriptionChanged,
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Описание", fontSize = 16.sp) })
//        Row {
//            Spacer(modifier = Modifier.width(15.dp))
//            Theme(
//                modifier = Modifier.padding(top = 10.dp, end = 10.dp, bottom = 10.dp),
//                text = "Создать",
//                onClicked = onThemeClicked
//            )
            LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
                item {
                    Spacer(modifier = Modifier.width(15.dp))
                }
                item {
                    Theme(
                        modifier = Modifier.padding(end = 10.dp),
                        text = "Создать",
                        onClicked = onThemeClicked
                    )
                }
                items(themeList, key = { item -> item.id }) { item ->
                    Theme(
                        modifier = Modifier.padding(end = 10.dp),
                        text = item.title,
                        onClicked = onThemeClicked
                    )
                }
//            }
        }
    }
}

@Composable
private fun Theme(
    modifier: Modifier = Modifier, text: String, onClicked: () -> Unit
) {
    Box(modifier = modifier.border(1.dp, Color(0xFF49454F), shape = RoundedCornerShape(8.dp))
        .clickable { onClicked() }) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF49454F)
        )
    }
}

@Composable
private fun EditThemeDialog(
    search: String,
    onDismissRequest: () -> Unit,
    onSearchChanged: (String) -> Unit,
    onAddThemeClicked: () -> Unit,
    onThemeClicked: (Long, Boolean) -> Unit,
    themeItems: List<ThemeListItem>,
    temporaryThemeItems: List<ThemeListItem>,
    checkedThemeItems: List<ThemeListItem>
) {
    val filteredThemes = themeItems.filter { it.title.contains(search, ignoreCase = true) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (search.isNotEmpty()) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(backgroundColor = Color(0xFFECE6F0), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column {
                Text(
                    text = "Темы теста",
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1D1B20)
                )
                OutlinedTextField(value = search,
                    onValueChange = {
                        onSearchChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 35.dp, end = 35.dp, bottom = 15.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onAddThemeClicked()
                    }),
                    placeholder = { Text(text = "Введите название") },
                    label = { Text(text = "Введите название") })
                if (themeItems.isEmpty() or ((themeItems.filter {
                        it.title.equals(
                            search, ignoreCase = true
                        )
                    }.isEmpty()) and (search.isNotEmpty()))) {
                    CreateItemOfList(
                        createThemeOfQuiz = {
                            onAddThemeClicked()
                            focusManager.clearFocus()

                        },
                        text = search,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                            .clip(RoundedCornerShape(10.dp)).background(color = Color(0xFFE5E5E5))
                    )
                }
                if (!filteredThemes.isEmpty()) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(10.dp)).background(color = Color(0xFFE5E5E5))
                    ) {
                        LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                            items(temporaryThemeItems,
                                key = { themeOftest -> themeOftest.id }) { themeOfTest ->
                                ItemOfList(themeQuiz = themeOfTest.title,
                                    checked = checkedThemeItems.contains(themeOfTest),
                                    onCheckedChange = {
                                        onThemeClicked(
                                            themeOfTest.id, themeOfTest.temporary
                                        )
                                    })
                            }
                            items(filteredThemes,
                                key = { themeOftest -> themeOftest.id }) { themeOfTest ->
                                ItemOfList(themeQuiz = themeOfTest.title,
                                    checked = checkedThemeItems.contains(themeOfTest),
                                    onCheckedChange = {
                                        onThemeClicked(
                                            themeOfTest.id, themeOfTest.temporary
                                        )
                                    })
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text("Назад", color = Color(0xFF6750A4), modifier = Modifier.padding(end = 36.dp))
                    }
                }
            }
        }
    }
}


@Composable
private fun DialogCreateQuestion(
    onDismissRequest: () -> Unit,
    onQuestionTypeClicked: (QuestionType) -> Unit,
    questionType: QuestionType,
    questionTypes: List<QuestionType>
//    onCreateQuestion: (QuestionType) -> Unit,
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Тип вопроса",
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1D1B20)
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuestionTypes(
                    selectedType = questionType,
                    onSelected = onQuestionTypeClicked,
                    questionTypes = questionTypes
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text("Назад", color = Color(0xFF6750A4))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    TextButton(onClick = {
//                        onCreateQuestion(questionType)
                        onDismissRequest()
                    }
                    ) {
                        Text(
                            "Создать",
                            color = Color(0xFF6750A4)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionTypes(
    selectedType: QuestionType,
    onSelected: (QuestionType) -> Unit,
    questionTypes: List<QuestionType>
) {
    LazyRow {
        items(questionTypes) { type ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(5.dp)
                    .border(1.dp, Color(0xFF79747E), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFFFFF))
                    .padding(5.dp)
                    .clickable { onSelected(type) },
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Canvas(modifier = Modifier.size(12.dp)) {
                    val strokeWidth = 10f
                    val radius = size.minDimension / 2
                    drawCircle(
                        radius = radius,
                        color = Color.Gray
                    )
                    if (selectedType == type) {
                        drawCircle(
                            radius = radius - strokeWidth / 2,
                            color = Color(0xFF6750A4)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = type.name, fontSize = 14.sp)
            }
        }
    }
}


@Composable
private fun AddQuestionDialog() {

}

//
//    var selected by remember { mutableStateOf(0L) }
//
//    val scrollState = rememberScrollState()
//    val listQuestion by remember {
//        mutableStateOf(
//            mutableListOf(
//                QuestionItem.OneAnswerQuestion(
//                    id = 1L,
//                    orderNum = 1L,
//                    question = "Question 1",
//                    answerList = listOf(
//                        Answer(id = 1, orderNum = 1L, text = "Answer 1"),
//                        Answer(id = 2, text = "Answer 2"),
//                        Answer(id = 3, text = "Answer 3"),
//                    ),
//                    pickedAnswerId = 1L,
//                    rightAnswerId = 1L
//                ),
//                QuestionItem.OneAnswerQuestion(
//                    id = 2L,
//                    orderNum = 2L,
//                    question = "Question 2",
//                    answerList = listOf(
//                        Answer(id = 1, text = "Answer 1"),
//                        Answer(id = 2, text = "Answer 2"),
//                        Answer(id = 3, text = "Answer 3"),
//                    ),
//                    pickedAnswerId = 1L,
//                    rightAnswerId = 1L
//                )
//            )
//        )
//    }
//
//
////        QuestionItem.MultiplyAnswerQuestion(
////            id = 2,
////            orderNum = 2,
////            question = "Question 2",
////            answerIdList = listOf(1, 2),
////            rightAnswerIdList = listOf(1, 2)
////        ),
////        QuestionItem.ShortAnswerQuestion(
////            id = 3,
////            orderNum = 3,
////            question = "Question 3",
////            answer = "Answer",
////            rightAnswer = "Right Answer"
////        )
//
//    if (quizCreateModel.showDialog) {
//        DialogCreateQuestion(
//            onShowDialog = component::onShowDialog,
//            onCreateQuestion = {
//                listQuestion.add(
////                    when (it) {
////                        TypeQuestion.OneAnswerQuestion ->
//                    QuestionItem.OneAnswerQuestion(
//                        id = listQuestion.size + 1L,
//                        orderNum = listQuestion.size + 1L,
//                        question = "Question ${listQuestion.size + 1}",
//                        answerList = listOf(
//                            Answer(id = 1, text = "Answer 1"),
//                            Answer(id = 2, text = "Answer 2"),
//                            Answer(id = 3, text = "Answer 3"),
//                        ),
//                        pickedAnswerId = 0L,
//                        rightAnswerId = 1L
//                    )
//
////                        TypeQuestion.MultiplyAnswerQuestion -> QuestionItem.MultiplyAnswerQuestion()
////                        TypeQuestion.ShortAnswerQuestion -> QuestionItem.ShortAnswerQuestion()
//                )
//            }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopBar(
//                onBackClicked = component::onBackClicked
//            )
//        },
//        floatingActionButton = { FAB(onClick = component::onShowDialog) },
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box(modifier = Modifier.padding(it)) {
//            LazyColumn {
//                item {
//                    Column(
//                        modifier = Modifier.fillMaxWidth().background(
//                            Color(0xFFD0BCFF),
//                            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
//                        )
//                            .padding(start = 20.dp)
//                    ) {
//                        Text(
//                            text = quizCreateModel.title,
//                            fontSize = 28.sp,
//                            modifier = Modifier.padding(end = 10.dp)
//                        )
//                        LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
//                            items(quizCreateModel.themeList) { item ->
//                                Theme(
//                                    modifier = Modifier.padding(end = 10.dp),
//                                    themeListItem = item,
//                                    onClicked = {}
//                                )
//                            }
//                            item {
//                                Theme(
//                                    modifier = Modifier.padding(end = 10.dp),
//                                    themeListItem = "Создать",
//                                    onClicked = {}
//                                )
//                            }
//                        }
//                    }
//                }
//                items(listQuestion) {
//                    OneAnswerQuestion(
//                        Modifier.padding(top = 20.dp),
//                        selected = selected == it.id,
//                        onSelected = {
//                            if (selected == it.id) {
//                                selected = 0L
//                            } else {
//                                selected = it.id
//                            }
//                        },
//                        onDeleteQuestion = { listQuestion.remove(it) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//

//

//
//@Composable
//private fun DialogCreateQuestion(
//    onShowDialog: () -> Unit,
//    onCreateQuestion: (TypeQuestion) -> Unit,
//) {
//
//    var questionType = remember { mutableStateOf(TypeQuestion.OneAnswerQuestion) }
//
//    Dialog(
//        onDismissRequest = onShowDialog
//    ) {
//        Card(
//            shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp),
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "Тип вопроса",
//                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
//                    fontSize = 24.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color(0xFF1D1B20)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                QuestionTypes(
//                    selected = questionType.value,
//                    onSelected = {
//                        if (questionType.value.equals(it)) {
//                            questionType.value = TypeQuestion.OneAnswerQuestion
//                        } else {
//                            questionType.value = it
//                        }
//                    }
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//                    TextButton(
//                        onClick = onShowDialog,
//                    ) {
//                        Text("Назад", color = Color(0xFF6750A4))
//                    }
//                    Spacer(modifier = Modifier.width(10.dp))
//                    TextButton(onClick = {
//                        onCreateQuestion(questionType.value)
//                        onShowDialog()
//                    }
//                    ) {
//                        Text(
//                            "Создать",
//                            color = Color(0xFF6750A4)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//


//@Composable
//private fun QuestionTypes(
//    selected: TypeQuestion,
//    onSelected: (TypeQuestion) -> Unit,
//) {
//
//    val types = listOf<TypeQuestion>(
//        TypeQuestion.OneAnswerQuestion,
//        TypeQuestion.MultiplyAnswerQuestion,
//        TypeQuestion.MultiplyAnswerQuestion
//    )
//
//    LazyRow {
//        items(types) { type ->
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .padding(5.dp)
//                    .border(1.dp, Color(0xFF79747E), RoundedCornerShape(10.dp))
//                    .clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFFFFF))
//                    .padding(5.dp)
//                    .clickable { onSelected(type) },
//            ) {
//                Spacer(modifier = Modifier.height(10.dp))
//                Canvas(modifier = Modifier.size(12.dp)) {
//                    val strokeWidth = 10f
//                    val radius = size.minDimension / 2
//                    drawCircle(
//                        radius = radius,
//                        color = Color.Gray
//                    )
//                    if (selected == type) {
//                        drawCircle(
//                            radius = radius - strokeWidth / 2,
//                            color = Color(0xFF6750A4)
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(10.dp))
//                Text(text = type.name, fontSize = 14.sp)
//            }
//        }
//    }
//}
//
////@Composable
////private fun QuestionContent(
////    item: QuestionItem,
////    onSelected: (Long) -> Unit,
////    selected: Long,
////    modifier: Modifier
////) {
////    when (item) {
////        is QuestionItem.OneAnswerQuestion -> {
////            OneAnswerQuestion(
////                modifier = modifier,
////                selected = selected == item.id,
////                onSelected = { onSelected(item.id) }
////            )
////        }
////
////        is QuestionItem.MultiplyAnswerQuestion -> {
////            MultiplyAnswerQuestion(modifier)
////        }
////
////        is QuestionItem.ShortAnswerQuestion -> {
////            ShortAnswerQuestion(modifier)
////        }
////    }
////    if (selected == i) {
////        QuestionActions()
////    }
////}
//
//@Composable
//private fun MultiplyAnswerQuestion(
//    modifier: Modifier,
//    selected: Boolean = false,
//    onSelected: () -> Unit
//) {
//
//    val pickedAnswerIds by remember { mutableStateOf(mutableListOf<Long>()) }
//    val answerOptions by remember { mutableStateOf(mutableListOf<Answer>()) }
//    var question by remember { mutableStateOf("") }
//
//    Column(
//        modifier = modifier.background(
//            Color(0xFFFEF7FF),
//            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//        ).let {
//            if (selected) {
//                it.border(
//                    3.dp,
//                    Color(0xFF6750A4),
//                    shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//                )
//            } else {
//                it.border(
//                    1.dp,
//                    Color(0xFF6750A4),
//                    shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//                )
//            }
//        }.clickable {
//            onSelected()
//        }
//    ) {
//        Spacer(modifier = Modifier.height(5.dp))
//        TextField(
//            value = question,
//            onValueChange = { question = it },
//            label = { Text("Вопрос") },
//            modifier = Modifier.fillMaxWidth()
//                .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 10.dp).clickable {
//                    onSelected()
//                }
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//        answerOptions.forEach {  answer ->
//            AnswerItem(
//                answer = answer,
//                selected = pickedAnswerIds.contains(answer.id),
//                onSelected = {
//                    if (pickedAnswerIds.contains(answer.id)) pickedAnswerIds.remove(
//                        answer.id
//                    ) else pickedAnswerIds.add(answer.id)
//                },
//                onDeleteOption = { answerOptions.remove(answer) },
//                onEditOption = {
//                    answerOptions[answerOptions.indexOf(answer)] = answer.copy(text = it)
//                }
//            )
//        }
////        LazyColumn {
////            items(answerOptions) { answer ->
////                AnswerItem(
////                    answer = answer,
////                    selected = pickedAnswerIds.contains(answer.id),
////                    onSelected = {
////                        if (pickedAnswerIds.contains(answer.id)) pickedAnswerIds.remove(
////                            answer.id
////                        ) else pickedAnswerIds.add(answer.id)
////                    },
////                    onDeleteOption = { answerOptions.remove(answer) },
////                    onEditOption = {
////                        answerOptions[answerOptions.indexOf(answer)] = answer.copy(text = it)
////                    }
////                )
////            }
////        }
//        AddAnswerItem(onAddAnswerItem = {
//            answerOptions.add(
//                Answer(
//                    id = answerOptions.size + 1L,
//                    orderNum = answerOptions.size + 1L,
//                    text = ""
//                )
//            )
//        })
//        Spacer(modifier = Modifier.height(10.dp))
//    }
//    Spacer(modifier = Modifier.height(10.dp))
//    if (selected) {
//        QuestionActions(
//            onDeleteQuestion = {}
//        )
//    }
//}
//
//@Composable
//fun AddAnswerItem(
//    onAddAnswerItem: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 5.dp),
//    ) {
//        RadioButton(
//            modifier = Modifier,
//            selected = false,
//            onClick = { },
//            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
//        )
//        Text(
//            text = "Добавить вариант",
//            color = Color(0xFF49454F),
//            modifier = Modifier.padding(top = 12.dp).clickable { onAddAnswerItem() }
//        )
//    }
//}
//
//
@Composable
private fun AnswerItem(
    answer: Answer,
    selected: Boolean,
    onSelected: () -> Unit,
    onDeleteOption: () -> Unit,
    onEditOption: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RadioButton(
            modifier = Modifier,
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
        )
        TextField(
            value = answer.text,
            onValueChange = onEditOption,
            modifier = Modifier.offset(y = -1.dp).width(270.dp)
        )
        IconButton(
            onClick = onDeleteOption,
            modifier = Modifier
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF49454F))
        }
    }
}

//@Composable
//private fun OneAnswerQuestion(
//    modifier: Modifier,
//    question: QuestionItem,
//    selected: Boolean,
//    onSelected: () -> Unit,
//    onDeleteQuestion: () -> Unit,
//) {
//
//    var pickedAnswerId by remember { mutableStateOf(0L) }
//    val answerOptions by remember {
//        mutableStateOf(
//            mutableListOf<Answer>(
//                Answer(
//                    id = 1,
//                    orderNum = 1,
//                    text = "Answer 1"
//                ),
//                Answer(
//                    id = 2,
//                    orderNum = 2,
//                    text = "Answer 2"
//                ),
//                Answer(
//                    id = 3,
//                    orderNum = 3,
//                    text = "Answer 3"
//                )
//            )
//        )
//    }
//    var question by remember { mutableStateOf("") }
//
//    Box(
//        modifier = modifier.padding(5.dp).background(
//            Color(0xFFFEF7FF),
//            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//        ).let {
//            if (selected) {
//                it.border(
//                    3.dp,
//                    Color(0xFF6750A4),
//                    shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//                )
//            } else {
//                it.border(
//                    1.dp,
//                    Color(0xFF6750A4),
//                    shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
//                )
//            }
//        }.clickable {
//            onSelected()
//        }
//    ) {
//        Column {
//            Spacer(modifier = Modifier.height(5.dp))
//            TextField(
//                value = question,
//                onValueChange = { question = it },
//                label = { Text("Вопрос") },
//                modifier = Modifier.fillMaxWidth()
//                    .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 10.dp).clickable {
//                        onSelected()
//                    }
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Column(Modifier.wrapContentHeight()) {
////                items(answerOptions) { answer ->
//                answerOptions.forEach { answer ->
//                    AnswerItem(
//                        answer = answer,
//                        selected = (pickedAnswerId == answer.id),
//                        onSelected = {
//                            pickedAnswerId = if (pickedAnswerId == answer.id) 0L else answer.id
//                        },
//                        onDeleteOption = { answerOptions.remove(answer) },
//                        onEditOption = {
//                            answerOptions[answerOptions.indexOf(answer)] = answer.copy(text = it)
//                        }
//                    )
//                }
//                    AddAnswerItem(onAddAnswerItem = {
//                        answerOptions.add(
//                            Answer(
//                                id = answerOptions.size + 1L,
//                                orderNum = answerOptions.size + 1L,
//                                text = ""
//                            )
//                        )
//                    })
////                }
//            }
//        }
//    }
//    Spacer(modifier = Modifier.height(10.dp))
//    if (selected) {
//        QuestionActions(onDeleteQuestion = onDeleteQuestion)
//    }
//}
//
//
//@Composable
//private fun ShortAnswerQuestion(modifier: Modifier) {
//
//}
//
//
//@Composable
//private fun QuestionActions(
//    onDeleteQuestion: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.End
//    ) {
//        IconButton(
//            onClick = { },
//            modifier = Modifier
//                .background(color = Color(0xFFD0BCFF), shape = RoundedCornerShape(100))
//
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF49454F))
//        }
//        Spacer(modifier = Modifier.width(15.dp))
//        IconButton(
//            onClick = onDeleteQuestion,
//            modifier = Modifier
//                .background(Color(0xFFD0BCFF), shape = RoundedCornerShape(100))
//        ) {
//            Icon(Icons.Default.Delete, contentDescription = "Add", tint = Color(0xFF49454F))
//        }
//        Spacer(modifier = Modifier.width(15.dp))
//        IconButton(
//            onClick = { },
//            modifier = Modifier
//                .background(Color(0xFFD0BCFF), shape = RoundedCornerShape(100))
//        ) {
//            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFF49454F))
//        }
//
//    }
//}
//
//@Composable
//private fun QuestionAction() {
//
//}