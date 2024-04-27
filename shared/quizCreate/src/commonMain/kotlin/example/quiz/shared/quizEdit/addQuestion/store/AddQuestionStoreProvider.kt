package example.quiz.shared.quizEdit.addQuestion.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import example.quiz.shared.quizlist.QuestionType

data class AddQuestionStoreProvider(
    private val storeFactory: StoreFactory
) {
    fun provide(): AddQuestionStore = object : AddQuestionStore,
        Store<AddQuestionStore.Intent, AddQuestionStore.State, Nothing> by storeFactory.create(
            name = "AddQuestionStore",
            initialState = AddQuestionStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory =::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        object ShowDialog : Msg()
        data class QuestionTypeChanged(val type: QuestionType) : Msg()
        object CloseDialog : Msg()
        data class AddQuestionTypes(val questionTypes: List<QuestionType>) : Msg()
    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<AddQuestionStore.Intent, Unit, AddQuestionStore.State, Msg, Nothing>() {

        override fun executeIntent(intent: AddQuestionStore.Intent) {
            when (intent) {
                is AddQuestionStore.Intent.ShowDialog -> dispatch(Msg.ShowDialog)
                is AddQuestionStore.Intent.SetType -> dispatch(Msg.QuestionTypeChanged(intent.type))
                is AddQuestionStore.Intent.DismissDialog -> dispatch(Msg.CloseDialog)
                is AddQuestionStore.Intent.SetQuestionTypes -> dispatch(Msg.AddQuestionTypes(intent.questionTypes))
            }
        }
    }

    private object ReducerImpl : Reducer<AddQuestionStore.State, Msg> {
        override fun AddQuestionStore.State.reduce(msg: Msg): AddQuestionStore.State = when(msg) {
            is Msg.ShowDialog -> copy(showDialog = true)
            is Msg.QuestionTypeChanged -> copy(questionType = msg.type)
            is Msg.CloseDialog -> copy(showDialog = false)
            is Msg.AddQuestionTypes -> copy(questionTypes = msg.questionTypes)
        }
    }
}