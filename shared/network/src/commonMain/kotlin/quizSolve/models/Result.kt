package quizSolve.models

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val idStudent: Int,
    val idTest: Int,
    val solutions: List<Solution>,
) {

    @Serializable
    data class Solution(
        val answer: String,
        val correctAnswer: String,
    )
}