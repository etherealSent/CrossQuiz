package com.example.quiz.dialog.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import com.example.quiz.dialog.store.QuizDialogStore
import dialog.store.QuizDialogStoreProvider
import example.quiz.shared.utils.asValue
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.database.themedata.ThemeSharedDatabase
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.dialogQuizCreate.QuizDialog
import example.quiz.shared.quizlist.dialogQuizCreate.integration.QuizDialogStoreQuizDatabase
import example.quiz.shared.quizlist.dialogQuizCreate.integration.QuizDialogStoreThemeDatabase
import example.quiz.shared.quizlist.dialogQuizCreate.integration.stateToModel

class QuizDialogComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    quizSharedDatabase: QuizSharedDatabase,
    themeSharedDatabase: ThemeSharedDatabase,
) : QuizDialog, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        QuizDialogStoreProvider(
            storeFactory = storeFactory,
            quizDatabase = QuizDialogStoreQuizDatabase(database = quizSharedDatabase),
            themeDatabase = QuizDialogStoreThemeDatabase(database = themeSharedDatabase)
        ).provide()
    }

    override val models: Value<QuizDialog.Model> = store.asValue().map(stateToModel)

    override fun onAddQuizClicked() {
        store.accept(QuizDialogStore.Intent.ShowDialog)
    }

    override fun onDialogDismissRequest() {
        store.accept(QuizDialogStore.Intent.CloseDialog)
    }

    override fun onBackClicked() {
        store.accept(QuizDialogStore.Intent.OnBackClicked)
    }

    override fun onNextClicked() {
        store.accept(QuizDialogStore.Intent.OnNextClicked)
    }

    override fun onInputTitleChanged(title: String) {
        store.accept(QuizDialogStore.Intent.SetTitle(title = title))
    }

    override fun onSearchThemeChanged(search: String) {
        store.accept(QuizDialogStore.Intent.SetSearch(search = search))
    }

    override fun onThemeClicked(themeId: Long, temporary: Boolean) {
        store.accept(QuizDialogStore.Intent.PickTheme(themeId = themeId, temporary = temporary))
    }

    override fun addThemeClicked() {
        store.accept(QuizDialogStore.Intent.CreateTheme)
    }

    override fun addTemporaryTheme() {
        store.accept(QuizDialogStore.Intent.AddTemporaryTheme)
    }

    override fun createQuiz() {
        store.accept(QuizDialogStore.Intent.CreateQuiz)
    }
}