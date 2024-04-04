package example.quiz.shared.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.quiz.dialog.integration.QuizDialogComponent
import example.quiz.shared.quizlist.integration.QuizListComponent

interface QuizRoot {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class QuizList(
            val componentA: QuizListComponent,
            val componentB: QuizDialogComponent
        ) : Child()
    }
}
