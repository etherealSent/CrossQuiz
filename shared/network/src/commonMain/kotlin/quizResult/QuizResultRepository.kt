package quizResult

import ApiResponse
import quizResult.models.QuizResult

interface QuizResultRepository {
    suspend fun getQuizResultById(id: String): ApiResponse<QuizResult, Unit>

    suspend fun getQuizResultByTestId(id: String): ApiResponse<QuizResult, Unit>

    suspend fun getQuizResultByStudentId(id: String): ApiResponse<QuizResult, Unit>
}