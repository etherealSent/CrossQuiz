package example.quiz.shared.quizEdit.editThemes.integration

import example.quiz.shared.quizEdit.editThemes.EditThemes
import example.quiz.shared.quizEdit.editThemes.store.EditThemesStore

internal val stateToModel: (EditThemesStore.State) -> EditThemes.Model =
    {
        EditThemes.Model(
            themeItems = it.themeItems,
            temporaryThemeItems = it.temporaryThemeItems,
            checkedThemeItems = it.checkedThemeItems,
            search = it.search,
            showDialog = it.showDialog
        )
    }