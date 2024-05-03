package quizResult.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val id_result_id: String,
    val idStudent: Int,
    val idTest: Int,
    val solutions: List<Solution>,
)