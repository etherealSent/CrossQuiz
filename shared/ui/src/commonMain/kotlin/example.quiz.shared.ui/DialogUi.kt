package example.quiz.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
//import androidx.compose.material.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import example.quiz.shared.quizlist.ThemeListItem
import example.quiz.shared.quizlist.dialogQuizCreate.DialogType
import example.quiz.shared.quizlist.dialogQuizCreate.QuizDialogItem

@Composable
fun DialogContent(
    onDismissRequest: () -> Unit,
    onBackClicked: () -> Unit,
    currentDialogId: Long,
    onNextClicked: () -> Unit,
    title: String,
    onTitleChanged: (String) -> Unit,
    search: String,
    onSearchThemeChanged: (String) -> Unit,
    themesOfTests: List<ThemeListItem>,
    createThemeOfQuiz: () -> Unit,
    createQuiz: () -> Unit,
    addTemporaryTheme: () -> Unit,
    checkedThemes: List<ThemeListItem>,
    onThemeChecked: (Long, Boolean) -> Unit,
    temporaryThemes: List<ThemeListItem>,
) {

    // init dialogItems in Dialog component
    val dialogItems = listOf<QuizDialogItem>(
        QuizDialogItem(0, DialogType.PickBase),
        QuizDialogItem(1, DialogType.PickName),
        QuizDialogItem(2, DialogType.PickThemes)
    )

    // add field to Dialog component
    val currentDialogType = dialogItems[currentDialogId.toInt()].dialogType

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            when (currentDialogType) {
                DialogType.PickBase -> PickBaseDialog(
                    onDismissRequest = onDismissRequest, onConfirmation = onNextClicked
                )

                DialogType.PickName -> PickNameDialog(
                    onDismissRequest = onBackClicked,
                    onConfirmation = onNextClicked,
                    text = title,
                    onTextChanged = onTitleChanged
                )

                DialogType.PickThemes -> PickThemesDialog(
                    onDismissRequest = onDismissRequest,
                    search = search,
                    onSearchChanged = onSearchThemeChanged,
                    themesOfTests = themesOfTests,
                    addTemporaryTheme = addTemporaryTheme,
                    createThemeOfQuiz = createThemeOfQuiz,
                    checkedThemes = checkedThemes,
                    onThemeChecked = onThemeChecked,
                    temporaryThemes = temporaryThemes,
                    createQuiz = createQuiz,
                    onBackClicked = onBackClicked
                )
            }
        }
    }
}

@Composable
fun PickBaseDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {

    var picked by remember { mutableStateOf(true) }
    Column {
        Text(
            text = "Создание теста",
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF1D1B20)
        )
        TypeOfTest(modifier = Modifier.padding(horizontal = 10.dp),
            picked = picked,
            onClick = { picked = !picked })
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
            TextButton(onClick = { if (picked) onConfirmation() }) {
                Text("Продолжить", color = if (!picked) Color(0xFF1D1B20) else Color(0xFF6750A4))
            }
        }
    }
}

@Composable
fun PickNameDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    text: String,
    onTextChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column {
        Text(
            text = if (text.isEmpty()) "Название теста" else text,
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF1D1B20)
        )
        OutlinedTextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp)
                .focusRequester(focusRequester),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = { onDismissRequest() },
            ) {
                Text("Назад", color = Color(0xFF6750A4))
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(onClick = { if (text.isNotEmpty()) onConfirmation() }) {
                Text(
                    "Продолжить",
                    color = if (text.isEmpty()) Color(0xFF1D1B20) else Color(0xFF6750A4)
                )
            }
        }
    }

}

@Composable
fun PickThemesDialog(
    onDismissRequest: () -> Unit,
    onBackClicked: () -> Unit,
    search: String,
    onSearchChanged: (String) -> Unit,
    themesOfTests: List<ThemeListItem>,
    checkedThemes: List<ThemeListItem>,
    onThemeChecked: (Long, Boolean) -> Unit,
    createThemeOfQuiz: () -> Unit,
    createQuiz: () -> Unit,
    addTemporaryTheme: () -> Unit,
    temporaryThemes: List<ThemeListItem>
) {
    val filteredThemes = themesOfTests.filter { it.title.contains(search, ignoreCase = true) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    if (search.isNotEmpty()) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
//    }

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
            modifier = Modifier.fillMaxWidth().padding(start = 35.dp, end = 35.dp, bottom = 15.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                addTemporaryTheme()
            }),
            placeholder = { Text(text = "Введите название") },
            label = { Text(text = "Введите название") })
        if ((themesOfTests.isEmpty() and temporaryThemes.isEmpty() and search.isNotEmpty()) or ((themesOfTests.filter {
                it.title.equals(
                    search, ignoreCase = true
                )
            }.isEmpty()) and (search.isNotEmpty()))) {
            CreateItemOfList(
                createThemeOfQuiz = {
                    addTemporaryTheme()
                    focusManager.clearFocus()

                },
                text = search,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp)).background(color = Color(0xFFE5E5E5))
            )
        }
        if (filteredThemes.isNotEmpty() or temporaryThemes.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp).clip(RoundedCornerShape(10.dp))
                    .background(color = Color(0xFFE5E5E5))
            ) {
                LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                    items(temporaryThemes, key = { themeOftest -> themeOftest.id }) { themeOfTest ->
                        ItemOfList(themeQuiz = themeOfTest.title,
                            checked = checkedThemes.contains(themeOfTest),
                            onCheckedChange = {
                                onThemeChecked(
                                    themeOfTest.id, themeOfTest.temporary
                                )
                            })
                    }
                    items(filteredThemes, key = { themeOftest -> themeOftest.id }) { themeOfTest ->
                        ItemOfList(themeQuiz = themeOfTest.title,
                            checked = checkedThemes.contains(themeOfTest),
                            onCheckedChange = {
                                onThemeChecked(
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
            TextButton(
                onClick = { onBackClicked() },
            ) {
                Text("Назад", color = Color(0xFF6750A4))
            }
            TextButton(onClick = {
                if (checkedThemes.isNotEmpty()) {
                    createThemeOfQuiz()
                    createQuiz()
                    onDismissRequest()
                }
            }) {
                Text(
                    "Создать", color = if (checkedThemes.isEmpty()) Color(0xFF1C1B1F) else Color(
                        0xFF6750A4
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemOfList(
    modifier: Modifier = Modifier, themeQuiz: String, checked: Boolean, onCheckedChange: () -> Unit
) {
    ListItem(modifier = modifier.background(Color(0xFFF3EDF7)).fillMaxWidth().clickable { onCheckedChange() },
        text = { Text(text = themeQuiz) },
        trailing = { Checkbox(checked = checked, onCheckedChange = { onCheckedChange() }) })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateItemOfList(
    createThemeOfQuiz: () -> Unit, text: String, modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier.background(Color(0xFFF3EDF7)).fillMaxWidth().clickable { createThemeOfQuiz() },
        text = { Text(text = "Создать " + text) },
//        leadingContent = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypeOfTest(
    picked: Boolean = true, onClick: () -> Unit = {}, modifier: Modifier = Modifier
) {
    Card(onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically).let {
            if (picked) it.border(2.dp, Color(0xFF6750A4), RoundedCornerShape(20.dp))
            else it
        }) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp).fillMaxWidth()
        ) {
            Text(text = "Шаблон тест", fontWeight = FontWeight.Medium, color = Color.Black)
            Text(text = "Включает в себя 3 тестовых вопроса с вариантами ответа")
        }
    }
}