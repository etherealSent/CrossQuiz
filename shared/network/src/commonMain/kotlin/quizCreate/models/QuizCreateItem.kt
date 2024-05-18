package quizCreate.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizCreateItem(
    val author_id : Int,
    val subject_id: Int,
    val theme_id: Int,
    val expert_id: Int,
    val max_points: Int,
    val questions: List<Question>
) {

    @Serializable
    data class Question(
        val question_text: String,
        val addition_info: String,
        val question_points: Int,
        val answers: List<Answer>,
    )

    @Serializable
    data class Answer(
        val answer_text: String,
        val is_correct: Boolean,
    )
}
