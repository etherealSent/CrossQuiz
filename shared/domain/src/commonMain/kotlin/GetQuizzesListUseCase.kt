class GetQuizzesListUseCase(
    private val quizRepository: QuizRepository
) {

    suspend fun execute(): List<QuizDomain> {
        return quizRepository.getQuizzes()
    }
}