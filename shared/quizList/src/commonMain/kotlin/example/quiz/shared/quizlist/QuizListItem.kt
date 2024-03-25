package example.quiz.shared.quizlist

 data class QuizListItem(
    public val id: Long,
    public val orderNum: Long,
    public val title: String,
    public val themeList: List<String>,
)

enum class Setup {
    Default
}
data class ThemeQuiz(
    val id: Int,
    val text: String
)