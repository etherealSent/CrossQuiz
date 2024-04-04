package quizresult.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import quizresult.QuizResultState

internal class QuizResultStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: QuizResultRepository,
) {

    fun provide(): QuizResultStore =
        object : QuizResultStore,
            Store<QuizResultStore.Intent, QuizResultState, Nothing> by storeFactory.create(
                name = "QuizResultStore",
                initialState = QuizResultState.Loading,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data object SetLoading : Msg()
        data class SetResult(val success: QuizResultState.Success) : Msg()
        data class SetError(val throwable: Throwable) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<QuizResultStore.Intent, Unit, QuizResultState, Msg, Nothing>() {
        override fun executeIntent(intent: QuizResultStore.Intent) =
            when (intent) {
                is QuizResultStore.Intent.Load -> loadResult()
                is QuizResultStore.Intent.SendFeedback,
                is QuizResultStore.Intent.FinishQuiz -> {
                    // TODO ELDAR HOW TO INTENT NEW SCREEN
                }
            }

        private fun loadResult() {
            scope.launch {
                dispatch(Msg.SetLoading)
                // TODO ELDAR ADD CONVERTERS
                val response = try {
                    //repository.getQuizResult()
                } catch (exception: Exception) {
                    dispatch(Msg.SetError(exception))
                }
                // dispatch(Msg.SetResult(response))
            }
        }
    }

    private object ReducerImpl : Reducer<QuizResultState, Msg> {
        override fun QuizResultState.reduce(msg: Msg): QuizResultState {
            return when (msg) {
                is Msg.SetLoading -> QuizResultState.Loading
                // TODO HANDLE DIFFERENT ERRORS
                is Msg.SetError -> QuizResultState.Error("ERROR")
                is Msg.SetResult -> QuizResultState.Success(msg.success.result)
            }
        }
    }
}
