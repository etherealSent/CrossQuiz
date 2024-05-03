package example.quiz.shared.quizEdit.addQuestion.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.shared.quizEdit.addQuestion.AddQuestion
import example.quiz.shared.quizEdit.addQuestion.store.AddQuestionStore
import example.quiz.shared.quizEdit.addQuestion.store.AddQuestionStoreProvider
import example.quiz.shared.quizEdit.question.store.QuestionStore
import example.quiz.shared.quizEdit.question.store.QuestionStoreProvider
import example.quiz.shared.quizlist.QuestionItem
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.utils.asValue

class AddQuestionComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory
) : AddQuestion, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        AddQuestionStoreProvider(
            storeFactory = storeFactory
        ).provide()
    }

    init {
        store.accept(AddQuestionStore.Intent.SetQuestionTypes(QuestionType.entries))
    }

    override val models: Value<AddQuestion.Model> = store.asValue().map(stateToModel)
    override fun onAddQuestionClicked() {
        store.accept(AddQuestionStore.Intent.ShowDialog)
    }
    override fun onQuestionTypeClicked(type: QuestionType) {
        store.accept(AddQuestionStore.Intent.SetType(type))
    }
    override fun onDismissCreateQuestion() {
        store.accept(AddQuestionStore.Intent.DismissDialog)
    }
}