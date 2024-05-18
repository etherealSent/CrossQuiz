package quizSolve

import ApiResponse
import quizSolve.models.Quiz
import quizSolve.models.Result

interface QuizSolveRepository {

    suspend fun getQuizById(id: Int): ApiResponse<Quiz, Unit>

    // TODO что возвращает
    suspend fun createResult(result: Result): ApiResponse<Unit, Unit>
}