package quizResult

import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizResult.models.QuizResult
import safeRequest

class QuizResultRepositoryImpl(
    private val httpClient: HttpClient,
) : QuizResultRepository {

    override suspend fun getQuizResult() = httpClient.safeRequest<QuizResult, Unit> {
        method = HttpMethod.Get
        // TODO
        url("")
    }

}