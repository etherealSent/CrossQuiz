package integration

import Answer
import QuizSolveState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

interface QuizSolveComponent {

    val state: StateFlow<QuizSolveState>

    fun onAnswerClick(answer: Answer)

    fun onNextClick()

    fun onReloadClick()

    fun onQuizStart()
}