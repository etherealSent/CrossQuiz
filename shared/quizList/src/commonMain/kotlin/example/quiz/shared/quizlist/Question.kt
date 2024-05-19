package example.quiz.shared.quizlist


enum class QuestionType {
    OneOptionQuestion, MultiplyOptionQuestion, TextQuestion
}

//sealed class Settings {
//    object Default : Settings()
//    object Custom : Settings()
//}

interface Answer {
    val id: Long
}

data class OptionAnswer(
    override val id: Long,
    val orderNum: Long,
    val text: String
) : Answer

data class TextAnswer(
    override val id: Long,
    val textField: String
) : Answer

interface Question {
    val id: Long
    val question: String
    val questionType: QuestionType
}

data class OptionQuestion(
    override val questionType: QuestionType = QuestionType.OneOptionQuestion,
    override val id: Long = 0,
    override val question: String = "",
    val rightAnswer: OptionAnswer? = null,
    val userAnswer: OptionAnswer? = null,
    val options: List<OptionAnswer> = listOf()
) : Question

data class MultiplyOptionQuestion(
    override val questionType: QuestionType = QuestionType.MultiplyOptionQuestion,
    override val id: Long = 0,
    override val question: String = "",
    val rightAnswer: List<OptionAnswer?> = listOf(),
    val userAnswer: List<OptionAnswer>? = null,
    val options: List<OptionAnswer> = listOf(),
) : Question

data class TextQuestion(
    override val questionType: QuestionType = QuestionType.TextQuestion,
    override val id: Long = 0,
    override val question: String = "",
    val rightAnswer: TextAnswer? = null,
    val userAnswer: OptionAnswer? = null
) : Question

