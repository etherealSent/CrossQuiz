package example.quiz.shared.quizlist

data class QuizListItem(
    public val id: Long = 0L,
    public val orderNum: Long = 0L,
    public val title: String = "",
    public val themeList: List<String> = listOf(),
)

enum class Setup {
    Default
}

data class ThemeQuiz(
    val id: Int, val text: String
)