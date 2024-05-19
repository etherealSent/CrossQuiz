package example.quiz.shared.quizlist.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import example.quiz.shared.utils.asValue
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.quizlist.QuizList
import example.quiz.shared.quizlist.store.QuizListStoreProvider

class QuizListComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: QuizSharedDatabase,
    private val output: Consumer<QuizList.Output>
) : QuizList, ComponentContext by componentContext {
    private val store =
        instanceKeeper.getStore {
            QuizListStoreProvider(
                storeFactory = storeFactory,
                database = QuizListStoreDatabase(database = database)
            ).provide()
        }

    override val models: Value<QuizList.Model> = store.asValue().map(stateToModel)

    override fun onQuizClicked(quizItemId: Long, themeList: List<Long>) {
        output(QuizList.Output.EditQuiz(quizItemId, themeList))
    }

}