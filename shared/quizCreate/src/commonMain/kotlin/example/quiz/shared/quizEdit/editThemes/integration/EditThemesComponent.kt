package example.quiz.shared.quizEdit.editThemes.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import example.quiz.shared.database.themedata.ThemeSharedDatabase
import example.quiz.shared.quizEdit.editThemes.EditThemes
import example.quiz.shared.quizEdit.editThemes.store.EditThemesStore
import example.quiz.shared.quizEdit.editThemes.store.EditThemesStoreProvider
import example.quiz.shared.quizlist.ThemeListItem
import example.quiz.shared.utils.asValue
import kotlinx.coroutines.delay

class EditThemesComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: ThemeSharedDatabase,
    themeList: List<String>
) : EditThemes, ComponentContext by componentContext {
    private val store =
        instanceKeeper.getStore {
            EditThemesStoreProvider(
                storeFactory = storeFactory,
                database = EditThemesStoreDatabase(database = database),
            ).provide()
        }

    override val models: Value<EditThemes.Model> = store.asValue().map(stateToModel)

    init {
        store.accept(EditThemesStore.Intent.GetThemeItems(themeList))
    }

    override fun getThemeItems(themeIds: List<String>) {
        return store.accept(EditThemesStore.Intent.GetThemeItems(themeIds))
    }

    override fun onAddThemeClicked() {
        store.accept(EditThemesStore.Intent.AddTemporaryTheme)
    }

    override fun onCreateTheme() {
        store.accept(EditThemesStore.Intent.CreateTheme)
    }

    override fun onEditThemeClicked() {
        store.accept(EditThemesStore.Intent.ShowDialog)
    }

    override fun onThemeClicked(themeId: Long, temporary: Boolean) {
        store.accept(EditThemesStore.Intent.AddTheme(themeId, temporary))
    }

    override fun onDismissRequest() {
        store.accept(EditThemesStore.Intent.CloseDialog)
    }

    override fun onSearchChanged(search: String) {
        store.accept(EditThemesStore.Intent.SetSearch(search))
    }
}
