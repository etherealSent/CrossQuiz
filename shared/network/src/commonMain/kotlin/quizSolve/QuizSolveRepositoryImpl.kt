package quizSolve

import ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizSolve.models.Quiz
import quizSolve.models.Result
import safeRequest

class QuizSolveRepositoryImpl(
    private val httpClient: HttpClient
): QuizSolveRepository {

    override suspend fun getQuizById(id: Int): ApiResponse<Quiz, Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("${Routing.BASE_URL}${Routing.TESTS_URL}/tests/${id}")
        }

    override suspend fun createResult(result: Result): ApiResponse<Unit, Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Post
            setBody(result)
            url("${Routing.BASE_URL}${Routing.TESTS_URL}/results/add")
        }
}