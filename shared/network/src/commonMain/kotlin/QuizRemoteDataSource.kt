import example.quiz.shared.utils.Dispatcher
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

class QuizRemoteDataSource(
    private val ktorService: KtorService,
    private val dispatcher: Dispatcher
) {

    suspend fun getQuizzes(): List<QuizDto> {
        return withContext(dispatcher.io) {
            ktorService.client.get("${Routing.BASE_URL}${Routing.WORK_URL}/courses/list/").body()
        }
    }
}