package example.quiz.shared.quizlist.dialogQuizCreate

import com.arkivanov.decompose.value.Value
import example.quiz.common.database.QuizEntity

interface DialogQuizCreate {
    val models: Value<Model>

    fun onBackClicked()

    fun onNextClicked()

    fun onDialogDismissed()

    fun onSetupPicked()

    fun onTitleTextChanged()

    fun onThemePicked()

    fun onThemeCreateClicked()

    fun onQuizCreateClicked()

    data class Model(
        val quiz: QuizEntity
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}