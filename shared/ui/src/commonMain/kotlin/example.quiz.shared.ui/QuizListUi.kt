package example.quiz.shared.ui

import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.quiz.dialog.DialogContent
import com.example.quiz.dialog.integration.QuizDialogComponent
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.ThemeListItem
import example.quiz.shared.quizlist.integration.QuizListComponent


@Composable
fun QuizListContent(
    quizListComponent: QuizListComponent, quizDialogComponent: QuizDialogComponent
) {
    val quizListModel by quizListComponent.models.subscribeAsState()
    val quizDialogModel by quizDialogComponent.models.subscribeAsState()
    Scaffold(floatingActionButton = { FAB(onClick = quizDialogComponent::onAddQuizClicked) },
        bottomBar = { BottomBar() }) { innerPadding ->
        ListItem(
            modifier = Modifier.padding(innerPadding),
            list = quizListModel.items,
            themes = quizDialogModel.themeItems,
            deleteItem = quizListComponent::onItemDeleteClicked
        )
    }
    if (quizDialogModel.showDialog) {
        DialogContent(
            onDismissRequest = quizDialogComponent::onDialogDismissRequest,
            currentDialogId = quizDialogModel.currentDialogScreenId,
            onBackClicked = quizDialogComponent::onBackClicked,
            onNextClicked = quizDialogComponent::onNextClicked,
            title = quizDialogModel.title,
            onTitleChanged = quizDialogComponent::onInputTitleChanged,
            search = quizDialogModel.search,
            onSearchThemeChanged = quizDialogComponent::onSearchThemeChanged,
            onThemeChecked = quizDialogComponent::onThemeClicked,
            themesOfTests = quizDialogModel.themeItems,
            addTemporaryTheme = quizDialogComponent::addTemporaryTheme,
            checkedThemes = quizDialogModel.checkedThemeItems,
            temporaryThemes = quizDialogModel.temporaryThemeItems,
            createQuiz = quizDialogComponent::createQuiz,
            createThemeOfQuiz = quizDialogComponent::addThemeClicked
        )
    }
}

@Composable
private fun ListItem(
    modifier: Modifier = Modifier,
    list: List<QuizListItem>,
    nameOfList: String = "Доступные тесты",
    themes: List<ThemeListItem>,
    deleteItem: (Long) -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        item {
            Text(
                text = nameOfList,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 10.dp, top = 20.dp)
            )
        }
        items(list) { item ->
            Item(
                item = item,
                modifier = Modifier.padding(vertical = 10.dp).clickable { deleteItem(item.id) },
                themes = themes,
                themeIds = item.themeList
            )
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
private fun Item(
    item: QuizListItem,
    themeIds: List<String>,
    themes: List<ThemeListItem>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.border(1.dp, Color(0xFF79747E), RoundedCornerShape(10.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ) {
            Row {
                //ImageBox
                Box(
                    modifier = Modifier.size(105.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
                        Text(
                            text = "От", fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier.clip(CircleShape).size(20.dp).background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "item.creatorName", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "item.startDate", fontSize = 14.sp, color = Color(0xFF49454F))
                }
            }
            Text(
                text = "XX/YY", modifier = Modifier.align(Alignment.End), color = Color(0xFF49454F)
            )
            LazyRow {
                items(themeIds) { theme ->
                    Box(
                        modifier = Modifier.padding(5.dp)
                            .border(1.dp, Color(0xFF79747E), RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp)).background(Color(0xFFD0BCFF))
                            .padding(5.dp)
                    ) {
                        Text(text = themes[theme.toInt() - 1].title, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar() {
    BottomAppBar(backgroundColor = Color(0xFFD0BCFF)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }
    }
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

