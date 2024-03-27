package quizList

import ApiResponse
import quizResult.models.QuizResult

interface QuizListRepository {

    suspend fun getQuizList(): ApiResponse<QuizResult, Unit>
}