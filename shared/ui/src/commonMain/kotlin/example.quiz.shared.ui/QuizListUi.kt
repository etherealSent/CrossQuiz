package example.quiz.shared.ui

import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import example.quiz.shared.quizlist.QuizListItem
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import example.quiz.shared.quizlist.integration.QuizListComponent

@Composable
fun QuizListContent(component: QuizListComponent) {
    val model by component.models.subscribeAsState()
    Scaffold(
        floatingActionButton = { FAB(onClick = component::onAddItemClicked) },
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListItem(
                list = model.items
            )
        }
    }
}

@Composable
private fun ListItem(
    list: List<QuizListItem>,
    nameOfList: String = "Доступные тесты"
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text(text = nameOfList, modifier = Modifier.padding(start = 20.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp) ) {
            val listState = rememberLazyListState()

            LazyColumn(state = listState) {
                items(list) { item ->
                    Item(
                        item = item,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    item: QuizListItem,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
//        .clickable { onItemClicked(item.id) }
        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
        .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row {
                //ImageBox
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .background(Color.Red, RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = item.title
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
                        Text(text = "От")
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(20.dp)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "item.creatorName")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "item.startDate")
                }
            }
            Text(text = "XX/YY", modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
private fun BottomBar() {
    BottomAppBar(modifier = Modifier) {
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
//
@Composable
private fun FAB(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = null)
    }
}

//
//
//
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewQuizMainContent() {
//    QuizMainContent()
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewListItem() {
//    ListItem(
//        list = listOf(
//            QuizListItem(
//                id = 0L,
//                title = "Название теста",
//                imageUrl = "",
//                themes = listOf("1", "2"),
//                creatorName = "Имя создателя теста",
//                startDate = "Старт - dd.mm.yyyy"
//            ),
//            QuizListItem(
//                id = 0L,
//                title = "Название теста",
//                imageUrl = "",
//                themes = listOf("1", "2"),
//                creatorName = "Имя создателя теста",
//                startDate = "Старт - dd.mm.yyyy"
//            )
//        ),
//        onItemClicked = {}
//    )
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewItem() {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Item(
//            item = QuizListItem(
//                id = 0L,
//                title = "Название теста",
//                imageUrl = "",
//                themes = listOf("1", "2"),
//                creatorName = "Имя создателя теста",
//                startDate = "Старт - dd.mm.yyyy"
//            ),
//            onItemClicked = {},
//            modifier = Modifier.padding(10.dp)
//        )
//    }
//}

