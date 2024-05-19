package example.quiz.shared.quizEdit.quizEdit.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.quizEdit.quizEdit.QuizEdit
import example.quiz.shared.quizEdit.quizEdit.store.QuizEditStore
import example.quiz.shared.quizEdit.quizEdit.store.QuizEditStoreProvider
import example.quiz.shared.quizlist.QuestionType
import example.quiz.shared.utils.asValue

class QuizEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    quizSharedDatabase: QuizSharedDatabase,
    itemId: Long,
    private val output: Consumer<QuizEdit.Output>
) : QuizEdit, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        QuizEditStoreProvider(
            storeFactory = storeFactory,
            quizDatabase = QuizEditStoreQuizDatabase(database = quizSharedDatabase),
            itemId = itemId
        ).provide()
    }
    override val models: Value<QuizEdit.Model> = store.asValue().map(stateToModel)

    override fun onBackClicked() {
        output(QuizEdit.Output.QuizList)
    }

    override fun onTitleChanged(title: String) {
        store.accept(QuizEditStore.Intent.SetTitle(title))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(QuizEditStore.Intent.SetDescription(description))
    }

    override fun onQuizThemeClicked() {
        output(QuizEdit.Output.QuizTheme)
    }

    override fun onSettingsClicked() {
        output(QuizEdit.Output.QuizSettings)
    }

    override fun onMoreVertClicked() {
        // TODO
    }
    override fun updateThemes(themeList: List<String>) {
        store.accept(QuizEditStore.Intent.UpdateThemes(themeList))
    }
}