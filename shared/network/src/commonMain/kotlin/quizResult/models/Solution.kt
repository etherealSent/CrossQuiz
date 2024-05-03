package quizResult.models

import kotlinx.serialization.Serializable

@Serializable
data class Solution(
    val answer: String,
    val correct_answer: String,
)