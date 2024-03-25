package example.quiz.shared.root.integration

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.common.database.QuizSharedDatabase
import example.quiz.shared.quizlist.integration.QuizListComponent
import example.quiz.shared.root.QuizRoot
import kotlinx.serialization.Serializable

class QuizRootComponent internal constructor(
    componentContext: ComponentContext,
    private val quizList: (ComponentContext) -> QuizListComponent
) : QuizRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: QuizSharedDatabase
    ) : this(
        componentContext = componentContext,
        quizList = { childContext ->
            QuizListComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database
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

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): QuizRoot.Child =
        when (configuration) {
            Configuration.QuizList -> QuizRoot.Child.QuizList(quizList(componentContext))
        }

    @Serializable
    private sealed class Configuration {
        @Serializable
        data object QuizList : Configuration()
    }
}



