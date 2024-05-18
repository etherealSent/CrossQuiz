package quizList.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizListRequest(
    val subject_id: Int,
    val theme_id: Int,
)