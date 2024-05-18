package quizCreate

import ApiResponse
import quizCreate.models.QuizCreateItem
import quizList.models.QuizListItem
import quizList.models.QuizListRequest

interface QuizCreateRepository {

    // TODO что возвращает этот post запрос? успешно ли?
    suspend fun createQuiz(quiz: QuizCreateItem): ApiResponse<Unit, Unit>
}