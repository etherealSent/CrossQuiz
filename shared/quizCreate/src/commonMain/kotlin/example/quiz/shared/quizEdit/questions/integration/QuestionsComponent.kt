package example.quiz.shared.quizEdit.questions.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.shared.quizEdit.questions.Questions
import example.quiz.shared.quizEdit.questions.store.QuestionStore
import example.quiz.shared.quizEdit.questions.store.QuestionStoreProvider
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.utils.asValue


class QuestionsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : Questions, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        QuestionStoreProvider(
            storeFactory = storeFactory
        ).provide()
    }

    override val models: Value<Questions.Model> = store.asValue().map(stateToModel)

    override fun onQuestionChanged(question: String) {
        store.accept(QuestionStore.Intent.SetQuestion(question))
    }

    override fun onRightAnswerPicked(answerId: Long) {
        store.accept(QuestionStore.Intent.PickAnswer(answerId))
    }

    override fun onAnswerDeleteClicked(id: Long) {
        store.accept(QuestionStore.Intent.DeleteAnswer(id))
    }

    override fun onAnswerChanged(id: Long, answer: String) {
        store.accept(QuestionStore.Intent.EditAnswer(id, answer))
    }
    override fun onCreateQuestionClicked(questionType: QuestionType) {
        store.accept(QuestionStore.Intent.CreateQuestion(questionType))
    }
    override fun onQuestionSelected(id: Long) {
        store.accept(QuestionStore.Intent.SelectQuestion(id))
    }

    override fun onCreateAnswerClicked() {
        store.accept(QuestionStore.Intent.CreateAnswer)
    }

    override fun resetSelectedQuestionId() {
        store.accept(QuestionStore.Intent.SelectQuestion(0L))
    }
}