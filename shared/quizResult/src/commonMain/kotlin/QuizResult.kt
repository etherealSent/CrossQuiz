data class QuizResult(
    val currentScore: Int,
    val maxScore: Int,
    val rightQuestion: Int,
    val maxQuestion: Int,
    val assessment: Assessment,
) {
    enum class Assessment {
        EXCELLENT,
        GOOD,
        BAD,
    }
}