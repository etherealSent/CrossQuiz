package integration

import QuizSolveState
import com.arkivanov.decompose.value.Value

interface QuizSolveComponent {

    val state: Value<QuizSolveState>
}