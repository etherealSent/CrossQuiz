package example.quiz.shared.quizlist.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.common.database.QuizSharedDatabase
import example.quiz.common.utils.asValue
import example.quiz.shared.quizlist.QuizList
import example.quiz.shared.quizlist.store.QuizListStore
import example.quiz.shared.quizlist.store.QuizListStoreProvider

class QuizListComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: QuizSharedDatabase,
) : QuizList, ComponentContext by componentContext{
    private val store =
        instanceKeeper.getStore {
            QuizListStoreProvider(
                storeFactory = storeFactory,
                database = QuizListStoreDatabase(database = database)
            ).provide()
        }

    override val models: Value<QuizList.Model> = store.asValue().map(stateToModel)

    override fun onAddItemClicked() {
        store.accept(QuizListStore.Intent.AddItem)
    }

}