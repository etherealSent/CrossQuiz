package example.quiz.shared.quizlist

import com.arkivanov.decompose.value.Value

interface QuizMain {
    val models: Value<Model>

    fun onItemClicked(id: Long)

    fun onAddItemClicked()

    data class Model(
        val items: List<QuizListItem>,
    )
}