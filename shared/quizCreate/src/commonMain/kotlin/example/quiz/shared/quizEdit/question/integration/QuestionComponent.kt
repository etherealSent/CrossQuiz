package example.quiz.shared.quizEdit.question.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.shared.quizEdit.question.Question
import example.quiz.shared.quizEdit.question.store.QuestionStore
import example.quiz.shared.quizEdit.question.store.QuestionStoreProvider
import example.quiz.shared.quizlist.Answer
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.utils.asValue


class QuestionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : Question, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        QuestionStoreProvider(
            storeFactory = storeFactory
        ).provide()
    }

    override val models: Value<Question.Model> = store.asValue().map(stateToModel)

    override fun onQuestionChanged(question: String) {
        store.accept(QuestionStore.Intent.SetQuestion(question))
    }

    override fun onRightAnswerPicked(answerId: Long) {
        store.accept(QuestionStore.Intent.PickAnswer(answerId))
    }

    override fun onAnswerDeleteClicked(answerId: Long) {
        store.accept(QuestionStore.Intent.DeleteAnswer(answerId))
    }

    override fun onAnswerChanged(answer: Answer) {
        store.accept(QuestionStore.Intent.EditAnswer(answer))
    }

    override fun onChangeType(questionType: QuestionType) {
        store.accept(QuestionStore.Intent.ShowChangeTypeDialog(questionType))
    }

    override fun onDismissChangeType() {
        store.accept(QuestionStore.Intent.CloseChangeTypeDialog)
    }

    override fun onSelectType(questionType: QuestionType) {
        store.accept(QuestionStore.Intent.ChangeType(questionType))
    }

    override fun onPickType(questionType: QuestionType) {
        store.accept(QuestionStore.Intent.SelectType(questionType))
    }

    override fun onSettingsDialog() {
        store.accept(QuestionStore.Intent.ShowSettingsDialog)
    }
}