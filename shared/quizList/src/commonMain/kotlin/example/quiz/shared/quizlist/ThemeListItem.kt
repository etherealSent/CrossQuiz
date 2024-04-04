package example.quiz.shared.quizlist

data class ThemeListItem(
    public val id: Long = 0L,
    public val orderNum: Long = 0L,
    public val title: String = "",
    public val temporary: Boolean = true
)