package quizSolve

import ApiResponse
import quizCreate.models.QuizCreateItem
import quizSolve.models.Quiz

interface QuizSolveRepository {

    suspend fun getQuizById(id: Int): ApiResponse<Quiz, Unit>
}