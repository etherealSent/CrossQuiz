package quizList.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizListItem(
    val id: Int,
    val author_id : Int,
    val subject_id: Int,
    val theme_id: Int,
    val times_solved: Int,
    val expert_id: Int,
    val max_points: Int,
)