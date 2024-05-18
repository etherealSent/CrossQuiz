package quizList

import ApiResponse
import quizList.models.QuizListItem
import quizList.models.QuizListRequest
import quizResult.models.QuizResult

interface QuizListRepository {

    suspend fun getQuizList(body: QuizListRequest): ApiResponse<List<QuizListItem>, Unit>
}