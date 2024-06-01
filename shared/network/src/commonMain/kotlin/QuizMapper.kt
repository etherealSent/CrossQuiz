class QuizMapper {

    fun map(quizzes: List<QuizDto>): List<QuizDomain> {
        return quizzes.map { quiz -> mapQuiz(dto = quiz) }
    }

    private fun mapQuiz(dto: QuizDto): QuizDomain {
        return QuizDomain(
            id = dto.id,
            title = dto.title,
            subjectId = dto.subjectId,
            expertId = dto.expertId,
            description = dto.description
        )
    }
}