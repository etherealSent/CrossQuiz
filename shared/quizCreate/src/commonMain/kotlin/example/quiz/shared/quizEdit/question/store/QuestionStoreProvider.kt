package example.quiz.shared.quizEdit.question.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionType

data class QuestionStoreProvider(
    private val storeFactory: StoreFactory
) {
    fun provide(): QuestionStore = object : QuestionStore,
        Store<QuestionStore.Intent, QuestionStore.State, Nothing> by storeFactory.create(
            name = "QuestionStore",
            initialState = QuestionStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory =::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class QuestionChanged(val question: String) : Msg()
        data class AnswerPicked(val id: Long) : Msg()
        data class AnswerDeleted(val id: Long) : Msg()
        data class AnswerTextChanged(val answer: Answer) : Msg()

        object OpenChangeTypeDialog : Msg()
        data class ChangeType(val type: QuestionType) : Msg()
        data class SelectType(val type: QuestionType) : Msg()
        object CloseChangeTypeDialog : Msg()
        object OpenSettingsDialog : Msg()
    }

    private inner class ExecutorImpl :
        ReaktiveExecutor<QuestionStore.Intent, Unit, QuestionStore.State, Msg, Nothing>() {

        override fun executeIntent(intent: QuestionStore.Intent) {
            when (intent) {
                is QuestionStore.Intent.SetQuestion -> dispatch(Msg.QuestionChanged(intent.question))
                is QuestionStore.Intent.PickAnswer -> dispatch(Msg.AnswerPicked(intent.answerId))
                is QuestionStore.Intent.DeleteAnswer -> dispatch(Msg.AnswerDeleted(intent.id))
                is QuestionStore.Intent.EditAnswer -> dispatch(Msg.AnswerTextChanged(intent.answer))
                is QuestionStore.Intent.ShowChangeTypeDialog -> dispatch(Msg.OpenChangeTypeDialog)
                is QuestionStore.Intent.CloseChangeTypeDialog -> dispatch(Msg.CloseChangeTypeDialog)
                is QuestionStore.Intent.ShowSettingsDialog -> dispatch(Msg.OpenSettingsDialog)
                is QuestionStore.Intent.CloseSettingsDialog -> Unit
                is QuestionStore.Intent.ChangeType -> dispatch(Msg.ChangeType(intent.type))
                is QuestionStore.Intent.SelectType -> dispatch(Msg.SelectType(intent.type))
            }
        }
    }

    private object ReducerImpl : Reducer<QuestionStore.State, Msg> {
        override fun QuestionStore.State.reduce(msg: Msg): QuestionStore.State = when(msg) {
            is Msg.QuestionChanged -> copy(question = msg.question)
            is Msg.AnswerPicked -> copy(rightAnswers = answers.filter { it.id == msg.id })
            is Msg.AnswerDeleted -> copy(answers = answers.filter { it.id != msg.id })
            is Msg.AnswerTextChanged -> copy(answers = answerTextChanged(msg.answer.id, msg.answer.text, answers))
            is Msg.OpenChangeTypeDialog -> copy(showChangeTypeDialog = true)
            is Msg.CloseChangeTypeDialog -> copy(showChangeTypeDialog = false)
            is Msg.ChangeType -> copy(questionType = msg.type)
            is Msg.SelectType -> copy(questionType = msg.type)
            is Msg.OpenSettingsDialog -> copy()
        }

        private fun answerTextChanged(id: Long, text: String, answers: List<Answer>): List<Answer> {
            return answers.map {
                if (it.id == id) {
                    it.copy(text = text)
                } else {
                    it
                }
            }
        }
    }
}