package quizCreate

import ApiResponse
import Routing
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizCreate.GPT.models.RequestGPT
import quizCreate.GPT.models.ResponseGPT
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

    override suspend fun getQuizFromGPT(request: RequestGPT): ApiResponse<ResponseGPT, Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Post
            setBody(request)
            url("https://ask.chadgpt.ru/api/public/gpt-4")
        }
}