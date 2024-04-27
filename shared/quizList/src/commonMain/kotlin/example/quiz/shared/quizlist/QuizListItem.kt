package example.quiz.shared.quizlist

data class QuizListItem(
    public val id: Long = 0L,
    public val orderNum: Long = 0L,
    public val title: String = "",
    public val description: String = "",
    public val themeList: List<String> = listOf(),
    public val setup: Setup = Setup.Default,
//    public val questionList: List<QuestionItem> = listOf()
)

enum class Setup {
    Default
}
