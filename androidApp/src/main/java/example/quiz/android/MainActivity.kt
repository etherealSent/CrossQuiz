package example.quiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import example.quiz.shared.database.quizdata.DefaultQuizSharedDatabase
import example.quiz.shared.database.quizdata.QuizDatabaseDriver
import example.quiz.shared.database.themedata.DefaultThemeSharedDatabase
import example.quiz.shared.database.themedata.ThemeDatabaseDriver
import example.quiz.shared.root.QuizRoot
import example.quiz.shared.root.integration.QuizRootComponent
import example.quiz.shared.ui.QuizRootContent
import getCommonModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = quizRoot(defaultComponentContext())

        setContent {
            Surface {
                QuizRootContent(root)
            }
        }

        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            getCommonModules()
        }
    }

    private fun quizRoot(componentContext: ComponentContext): QuizRoot =
        QuizRootComponent(
            componentContext = componentContext,
            storeFactory = LoggingStoreFactory(TimeTravelStoreFactory()),
            quizDatabase = DefaultQuizSharedDatabase(QuizDatabaseDriver(context = this)),
            themeDatabase = DefaultThemeSharedDatabase(ThemeDatabaseDriver(context = this))
        )
}
