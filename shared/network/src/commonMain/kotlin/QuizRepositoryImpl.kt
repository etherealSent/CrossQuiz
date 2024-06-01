class QuizRepositoryImpl(
    private val quizMapper: QuizMapper,
    private val quizRemoteDataSource: QuizRemoteDataSource
): QuizRepository {
    override suspend fun getQuizzes(): List<QuizDomain> {
        val remoteResponse = quizRemoteDataSource.getQuizzes()
        return quizMapper.map(quizzes = remoteResponse)
    }
}