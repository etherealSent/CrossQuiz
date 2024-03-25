package example.quiz.shared.root

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import example.quiz.shared.quizlist.integration.QuizListComponent

interface QuizRoot {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class QuizList(val component: QuizListComponent) : Child()
    }
}
