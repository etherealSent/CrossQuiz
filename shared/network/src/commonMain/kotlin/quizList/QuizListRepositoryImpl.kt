package quizList

import ApiResponse
import Routing
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import quizList.models.QuizListItem
import quizList.models.QuizListRequest
import safeRequest

class QuizListRepositoryImpl(
    private val httpClient: HttpClient
): QuizListRepository {

    override suspend fun getQuizList(body: QuizListRequest): ApiResponse<List<QuizListItem>, Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("${Routing.BASE_URL}${Routing.TESTS_URL}/tests/list/${body.subject_id}/${body.theme_id}")
        }
}