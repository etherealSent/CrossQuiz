import de.jensklingenberg.ktorfit.Response

internal interface QuizResultRepository {
    // TODO ELDAR change generic
    suspend fun getQuizResult(): Response<Unit>
}