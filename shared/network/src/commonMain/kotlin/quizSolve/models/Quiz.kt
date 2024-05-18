package quizSolve.models

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val author_id : Int,
    val subject_id: Int,
    val theme_id: Int,
    val expert_id: Int,
    val questions: List<Question>
) {
    @Serializable
    data class Question(
        val question_text: String,
        val addition_info: String,
        val answers: List<Answer>,
    )

    @Serializable
    data class Answer(
        val answer_text: String
    )
}