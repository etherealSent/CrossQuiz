package example.quiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import example.quiz.common.database.DefaultQuizSharedDatabase
import example.quiz.common.database.QuizDatabaseDriver
import example.quiz.shared.root.QuizRoot
import example.quiz.shared.root.integration.QuizRootComponent
import example.quiz.shared.ui.QuizRootContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = quizRoot(defaultComponentContext())

        setContent {
            Surface {
                QuizRootContent(root)
            }
        }
    }
    private fun quizRoot(componentContext: ComponentContext): QuizRoot =
        QuizRootComponent(
            componentContext = componentContext,
            storeFactory = LoggingStoreFactory(TimeTravelStoreFactory()),
            database = DefaultQuizSharedDatabase(QuizDatabaseDriver(context = this))
        )
}