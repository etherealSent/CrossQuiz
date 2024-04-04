package example.quiz.shared.quizlist

import com.arkivanov.decompose.value.Value

interface QuizList {

    val models: Value<Model>
    fun onAddItemClicked()

    fun onItemDeleteClicked(id: Long)

    data class Model(
        val items: List<QuizListItem>
    )

}