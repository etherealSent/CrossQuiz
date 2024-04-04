package example.quiz.shared.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import example.quiz.shared.root.QuizRoot

@Composable
fun QuizRootContent(component: QuizRoot) {
    Children(
        stack = component.childStack, animation = stackAnimation(fade() + scale())
    ) {
        when (val child = it.instance) {
            is QuizRoot.Child.QuizList -> QuizListContent(child.componentA, child.componentB)
        }
    }
}