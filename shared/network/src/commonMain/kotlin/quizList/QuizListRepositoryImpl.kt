package quizList

import ApiResponse
import io.ktor.client.HttpClient
import quizResult.models.QuizResult

class QuizListRepositoryImpl(
    val httpClient: HttpClient
): QuizListRepository {

    override suspend fun getQuizList(): ApiResponse<QuizResult, Unit> {
        TODO("Not yet implemented")
    }
}