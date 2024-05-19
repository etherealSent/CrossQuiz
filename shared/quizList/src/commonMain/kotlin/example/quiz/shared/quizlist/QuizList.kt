package example.quiz.shared.quizlist

import com.arkivanov.decompose.value.Value

interface QuizList {

    val models: Value<Model>

    fun onQuizClicked(quizItemId: Long, themeList: List<Long>)


    data class Model(
        val items: List<QuizListItem>
    )

    sealed class Output {
        data class EditQuiz(val itemId: Long, val themeList: List<Long>) : Output()
    }

}

