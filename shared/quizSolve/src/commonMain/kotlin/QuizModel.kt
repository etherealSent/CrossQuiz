// TODO ELDAR REMOVE IMAGE SUPPORT
data class QuizModel(
    val id: String,
    val questions: List<Question>,
)

sealed interface Question {
    val id: String
    val questionNumber: String
    val questionText: String
    val answers: List<Answer>

    data class Single(
        override val id: String,
        override val questionNumber: String,
        override val questionText: String,
        override val answers: List<Answer>,
    ) : Question

//    data class Multiple(
//        override val id: String,
//        override val questionNumber: String,
//        override val questionText: String,
//        override val answers: List<Answer>,
//    ) : Question
}

//sealed interface Answers {
//    val items: List<Answer>
//
//    data class TextItems(
//        override val items: List<Answer.Text>
//    ) : Answers
//
//    data class ImageItems(
//        override val items: List<Answer.Image>
//    ): Answers
//}

data class Answer(
    val id: Id,
    val text: String,
) {
    data class Id(val raw: String)
}

//sealed interface Answer {
//    val id: Id
//
//    data class Text(
//        override val id: Id,
//        val text: String,
//    ): Answer
//
//    data class Image(
//        override val id: Id,
//        val url: String,
//        val text: String?,
//    ): Answer
//
//    data class Id(val raw: String)
//}

