package integration

import QuizSolveState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import store.QuizSolveStoreProvider

class QuizSolveComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
): QuizSolveComponent, ComponentContext by componentContext {

//    private val store =
//        instanceKeeper.getStore {
//            QuizSolveStoreProvider(
//                storeFactory = storeFactory,
//            ).provide()
//        }

    override val state: Value<QuizSolveState>
        get() = TODO("Not yet implemented")

}