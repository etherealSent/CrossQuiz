interface QuizRepository {
    suspend fun getQuizzes(): List<QuizDomain>
}