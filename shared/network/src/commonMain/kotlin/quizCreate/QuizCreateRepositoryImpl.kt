package quizCreate

import ApiResponse
import Routing
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizCreate.models.QuizCreateItem
import safeRequest

class QuizCreateRepositoryImpl(
    private val httpClient: HttpClient
): QuizCreateRepository {

    override suspend fun createQuiz(quiz: QuizCreateItem): ApiResponse<Unit, Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            setBody(quiz)
            url("${Routing.BASE_URL}${Routing.TESTS_URL}/tests/add")
        }
}