package quizResult

import Routing
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizResult.models.QuizResult
import safeRequest

class QuizResultRepositoryImpl(
    private val httpClient: HttpClient,
) : QuizResultRepository {

    override suspend fun getQuizResultById(id: String) = httpClient.safeRequest<QuizResult, Unit> {
        method = HttpMethod.Get
        url("${Routing.BASE_URL}/tests/api/v1/results/getByResultId/$id")
    }

    override suspend fun getQuizResultByTestId(id: String) =
        httpClient.safeRequest<QuizResult, Unit> {
            method = HttpMethod.Get
            url("${Routing.BASE_URL}/tests/api/v1/results/full/getByTestId/$id")
        }

    override suspend fun getQuizResultByStudentId(id: String) =
        httpClient.safeRequest<QuizResult, Unit> {
            method = HttpMethod.Get
            url("${Routing.BASE_URL}/tests/api/v1/results/full/getByStudentId/$id")
        }
}