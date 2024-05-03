package store

import ApiResponse
import QuizResult
import QuizResultState
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import quizResult.QuizResultRepository
import toQuizResultUI

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
        data class SetResult(val result: QuizResult) : Msg()
        data object SetError : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<QuizResultStore.Intent, Unit, QuizResultState, Msg, Nothing>() {
        override fun executeIntent(intent: QuizResultStore.Intent) =
            when (intent) {
                is QuizResultStore.Intent.Load -> loadResult(intent.id)
                is QuizResultStore.Intent.SendFeedback,
                is QuizResultStore.Intent.FinishQuiz -> {
                    // TODO ELDAR HOW TO INTENT NEW SCREEN
                }
            }

        private fun loadResult(id: String) {
            scope.launch {
                dispatch(Msg.SetLoading)
                when (val response = repository.getQuizResultById(id)) {
                    is ApiResponse.Success -> {
                        dispatch(Msg.SetResult(response.body.toQuizResultUI()))
                    }

                    is ApiResponse.Error -> {
                        dispatch(Msg.SetError)
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<QuizResultState, Msg> {
        override fun QuizResultState.reduce(msg: Msg): QuizResultState {
            return when (msg) {
                is Msg.SetLoading -> QuizResultState.Loading
                is Msg.SetError -> QuizResultState.Error("ERROR")
                is Msg.SetResult -> QuizResultState.Success(msg.result)
            }
        }
    }
}
