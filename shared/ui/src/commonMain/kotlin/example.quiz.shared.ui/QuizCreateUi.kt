package example.quiz.shared.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import example.quiz.shared.quizEdit.addQuestion.integration.AddQuestionComponent
import example.quiz.shared.quizEdit.editThemes.integration.EditThemesComponent
import example.quiz.shared.quizEdit.questions.integration.QuestionsComponent
import example.quiz.shared.quizEdit.quizEdit.integration.QuizEditComponent
import example.quiz.shared.quizlist.MultiplyOptionQuestion
import example.quiz.shared.quizlist.OptionAnswer
import example.quiz.shared.quizlist.OptionQuestion
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.quizlist.TextQuestion
import example.quiz.shared.quizlist.ThemeListItem
@Composable
fun QuizCreateContent(
    quizEditComponent: QuizEditComponent,
    questionComponent: QuestionsComponent,
    addQuestionComponent: AddQuestionComponent,
    editThemesComponent: EditThemesComponent
) {
    val quizEditModel by quizEditComponent.models.subscribeAsState()
    val editThemesModel by editThemesComponent.models.subscribeAsState()
    val addQuestionModel by addQuestionComponent.models.subscribeAsState()
    val questionsModel by questionComponent.models.subscribeAsState()

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

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
            onCreateQuestion = questionComponent::onCreateQuestionClicked,
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
        modifier = Modifier.fillMaxSize().background(Color(0xFFFEF7FF)).clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
                questionComponent.resetSelectedQuestionId()
            }
    ) {
        Box(modifier = Modifier
            .padding(it)
        ) {
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
                items(questionsModel.questions) { question ->
                    when (question) {
                        is OptionQuestion -> OneOptionQuestion(
                            question = question,
                            selected = questionsModel.selectedQuestionId == question.id,
                            onSelected = questionComponent::onQuestionSelected,
                            onQuestionChanged = questionComponent::onQuestionChanged,
                            onAddAnswerItem = questionComponent::onCreateAnswerClicked,
                            onEditOption = questionComponent::onAnswerChanged,
                            onDeleteOption = questionComponent::onAnswerDeleteClicked,
                            onOptionAnswerSelected = questionComponent::onRightAnswerPicked,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            isFocused = isFocused.value,
                            onFocused = { isFocused.value = it }
                        )
                        is TextQuestion -> TextQuestion(question = question)
                        is MultiplyOptionQuestion -> MultiplyOptionQuestion(question = question)
                    }
                }
            }
        }
    }
}

@Composable
fun OneOptionQuestion(
    modifier: Modifier = Modifier,
    question: OptionQuestion,
    selected: Boolean,
    onSelected: (Long) -> Unit,
    onQuestionChanged: (String) -> Unit,
    onAddAnswerItem: () -> Unit,
    onOptionAnswerSelected: (Long) -> Unit,
    onDeleteOption: (Long) -> Unit,
    onEditOption: (Long, String) -> Unit,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    isFocused: Boolean,
    onFocused: (Boolean) -> Unit
) {

    Box(modifier =Modifier.padding(10.dp) .clickable { focusManager.clearFocus() }) {
        Column(
            modifier = modifier.background(
                Color(0xFFFFFFFF),
                shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            ).let {
                if (selected) {
                    it.border(
                        2.dp,
                        Color(0xFF6750A4),
                        shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                    )
                } else {
                    it.border(
                        1.dp,
                        Color(0xFF79747E),
                        shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                    )
                }
            }.clickable {
                onSelected(question.id)
            }
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = question.question,
                onValueChange = onQuestionChanged,
                placeholder = { Text("Вопрос") },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 10.dp).focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onFocused(focusState.isFocused)
                        if (focusState.isFocused) {
                            onSelected(question.id)
                        }
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (selected) Color(0xFFF8F9FA) else Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 16.sp)
            )
            question.options.forEach { optionAnswer ->
                OptionAnswer(
                    modifier = Modifier.focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            onFocused(focusState.isFocused)
                            if (focusState.isFocused) { onSelected(question.id) } },
                    optionAnswer = optionAnswer,
                    selected = question.rightAnswer?.id == optionAnswer.id,
                    onSelected = { onOptionAnswerSelected(optionAnswer.id) },
                    onDeleteOption = { onDeleteOption(optionAnswer.id) },
                    onEditOption = { onEditOption(optionAnswer.id, it) },
                    questionSelected = selected
                )
            }
            if (selected) {
                CreateOptionAnswer(onAddAnswerItem = onAddAnswerItem)
            }
        }
    }
}

@Composable
fun OptionAnswer(
    modifier: Modifier = Modifier,
    optionAnswer: OptionAnswer,
    selected: Boolean,
    onSelected: () -> Unit,
    onEditOption: (String) -> Unit,
    onDeleteOption: () -> Unit,
    questionSelected: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RadioButton(
            modifier = Modifier,
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
        )
        TextField(
            value = optionAnswer.text,
            onValueChange = onEditOption,
            modifier = Modifier.offset(y = -2.dp).width(270.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        if (questionSelected) {
            IconButton(
                onClick = onDeleteOption,
                modifier = Modifier
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF49454F))
            }
        } else {
            Spacer(modifier = Modifier.width(36.dp))
        }
    }
}

@Composable
fun CreateOptionAnswer(
    modifier: Modifier = Modifier,
    onAddAnswerItem: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 5.dp),
    ) {
        RadioButton(
            modifier = Modifier,
            selected = false,
            onClick = { },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6750A4))
        )
        Text(
            text = "Добавить вариант",
            color = Color(0xFF49454F),
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable { onAddAnswerItem() }
                .offset(y = 1.dp)
        )
    }
}

@Composable
fun TextQuestion(
    question: TextQuestion
) {
    Text("TextQuestion")
}

@Composable
fun MultiplyOptionQuestion(
    question: MultiplyOptionQuestion
) {
    Text("MultiplyOptionQuestion")
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
        Card(
            backgroundColor = Color(0xFFECE6F0),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
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
                            items(
                                temporaryThemeItems,
                                key = { themeOftest -> themeOftest.id }) { themeOfTest ->
                                ItemOfList(themeQuiz = themeOfTest.title,
                                    checked = checkedThemeItems.contains(themeOfTest),
                                    onCheckedChange = {
                                        onThemeClicked(
                                            themeOfTest.id, themeOfTest.temporary
                                        )
                                    })
                            }
                            items(
                                filteredThemes,
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
                        Text(
                            "Назад",
                            color = Color(0xFF6750A4),
                            modifier = Modifier.padding(end = 36.dp)
                        )
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
    questionTypes: List<QuestionType>,
    onCreateQuestion: (QuestionType) -> Unit,
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
                        onCreateQuestion(questionType)
                        onDismissRequest()
                    }) {
                        Text(
                            "Создать", color = Color(0xFF6750A4)
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
                modifier = Modifier.padding(5.dp)
                    .border(1.dp, Color(0xFF79747E), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFFFFF)).padding(5.dp)
                    .clickable { onSelected(type) },
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Canvas(modifier = Modifier.size(12.dp)) {
                    val strokeWidth = 10f
                    val radius = size.minDimension / 2
                    drawCircle(
                        radius = radius, color = Color.Gray
                    )
                    if (selectedType == type) {
                        drawCircle(
                            radius = radius - strokeWidth / 2, color = Color(0xFF6750A4)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = type.name, fontSize = 14.sp)
            }
        }
    }
}