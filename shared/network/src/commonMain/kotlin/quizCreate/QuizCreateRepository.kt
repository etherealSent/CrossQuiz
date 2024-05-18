package quizCreate

import ApiResponse
import quizCreate.GPT.models.RequestGPT
import quizCreate.GPT.models.ResponseGPT
import quizCreate.models.QuizCreateItem
import quizList.models.QuizListItem
import quizList.models.QuizListRequest

interface QuizCreateRepository {

    // TODO что возвращает этот post запрос? успешно ли?
    suspend fun createQuiz(quiz: QuizCreateItem): ApiResponse<Unit, Unit>

    suspend fun getQuizFromGPT(request: RequestGPT): ApiResponse<ResponseGPT, Unit>
}