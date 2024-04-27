package example.quiz.shared.quizlist


data class QuestionItem(
    public val type: QuestionType = QuestionType.OneAnswerQuestion,
    public val question: String = "",
    public val answers: List<Answer> = listOf(),
    public val rightAnswers: List<Answer> = listOf(),
    public val settings: Settings = Settings.Default
)

enum class QuestionType(title: String) {
    OneAnswerQuestion("1изN"), MultiplyAnswerQuestion("XизN"), ShortAnswerQuestion("Текст")
}

sealed class Settings {
    object Default : Settings()
    object Custom : Settings()
}


data class Answer(
    public val id: Long = 0L,
    public val orderNum: Long = 0L,
    public val text: String = ""
)