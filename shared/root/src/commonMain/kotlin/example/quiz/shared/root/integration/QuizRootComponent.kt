package example.quiz.shared.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.example.quiz.dialog.integration.QuizDialogComponent
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.database.themedata.ThemeSharedDatabase
import example.quiz.shared.quizEdit.addQuestion.integration.AddQuestionComponent
import example.quiz.shared.quizEdit.editThemes.integration.EditThemesComponent
import example.quiz.shared.quizEdit.questions.integration.QuestionsComponent
import example.quiz.shared.quizEdit.quizEdit.QuizEdit
import example.quiz.shared.quizEdit.quizEdit.integration.QuizEditComponent
import example.quiz.shared.quizlist.QuizList
import example.quiz.shared.quizlist.integration.QuizListComponent
import example.quiz.shared.root.QuizRoot
import kotlinx.serialization.Serializable

class QuizRootComponent internal constructor(
    componentContext: ComponentContext,
    private val quizList: (ComponentContext, Consumer<QuizList.Output>) -> QuizListComponent,
    private val dialog: (ComponentContext) -> QuizDialogComponent,
    private val quizEdit: (ComponentContext, itemId: Long, Consumer<QuizEdit.Output>) -> QuizEditComponent,
    private val question: (ComponentContext) -> QuestionsComponent,
    private val addQuestion: (ComponentContext) -> AddQuestionComponent,
    private val editThemes: (ComponentContext, themeList: List<Long>) -> EditThemesComponent
) : QuizRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        quizDatabase: QuizSharedDatabase,
        themeDatabase: ThemeSharedDatabase
    ) : this(
        componentContext = componentContext,
        quizList = { childContext, output ->
            QuizListComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = quizDatabase,
                output = output
            )
        },
        dialog = { childContext ->
            QuizDialogComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                quizSharedDatabase = quizDatabase,
                themeSharedDatabase = themeDatabase,
           )
        },
        quizEdit= { childContext, itemId, output ->
            QuizEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                quizSharedDatabase = quizDatabase,
                output = output,
                itemId = itemId
            )
        },
        question = { childContext ->
          QuestionsComponent(
              componentContext = componentContext,
              storeFactory = storeFactory,
          )
        },
        addQuestion = { childContext ->
            AddQuestionComponent(
                componentContext = componentContext,
                storeFactory = storeFactory
            )
        },
        editThemes = { childContext, themeList ->
            EditThemesComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                database = themeDatabase,
                themeList = themeList
            )
        }
    )

    private val navigation = StackNavigation<Configuration>()

    private val stack =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.QuizList,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = Configuration.serializer()
        )

    override val childStack: Value<ChildStack<*, QuizRoot.Child>> = stack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): QuizRoot.Child =
        when (configuration) {
            is Configuration.QuizList -> QuizRoot.Child.QuizList(
                quizList(componentContext, Consumer2(::onQuizListOutput)),
                dialog(componentContext)
            )
            is Configuration.QuizCreate -> QuizRoot.Child.QuizEdit(
                quizEdit(componentContext, configuration.itemId, Consumer2(::onQuizEditOutput)),
                question(componentContext),
                addQuestion(componentContext),
                editThemes(componentContext, configuration.themeList)
            )
        }

    private fun onQuizListOutput(output: QuizList.Output) {
        when (output) {
            is QuizList.Output.EditQuiz -> navigation.push(Configuration.QuizCreate(output.itemId, themeList = output.themeList))
        }
    }

    private fun onQuizEditOutput(output: QuizEdit.Output) {
        when (output) {
            is QuizEdit.Output.QuizList -> navigation.pop()
            is QuizEdit.Output.QuizTheme -> navigation.pop()
            is QuizEdit.Output.QuizSettings -> navigation.pop()
        }
    }

    @Serializable
    private sealed class Configuration {
        @Serializable
        data object QuizList : Configuration()
        @Serializable
        data class QuizCreate(val itemId: Long, val themeList: List<Long>) : Configuration()
    }
}

@Suppress("FunctionName") // Factory function
inline fun <T> Consumer2(crossinline block: (T) -> Unit): Consumer<T> =
    object : Consumer<T> {
        override fun onNext(value: T) {
            block(value)
        }
    }

