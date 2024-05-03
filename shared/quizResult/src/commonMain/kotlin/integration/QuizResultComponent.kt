package integration

import QuizResultState
import kotlinx.coroutines.flow.StateFlow

interface QuizResultComponent {

    val state: StateFlow<QuizResultState>

    fun onFinishClick()

    fun onReloadClick()
}