package quizResult

import ApiResponse
import quizResult.models.QuizResult

interface QuizResultRepository {
    suspend fun getQuizResult(): ApiResponse<QuizResult, Unit>
}