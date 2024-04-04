package example.quiz.shared.quizlist.dialogQuizCreate.integration

import com.example.quiz.dialog.store.QuizDialogStore
import example.quiz.shared.quizlist.dialogQuizCreate.QuizDialog

internal val stateToModel: (QuizDialogStore.State) -> QuizDialog.Model =
    {
        QuizDialog.Model(
            showDialog = it.showDialog,
            quizItems = it.quizItems,
            currentDialogScreenId = it.currentDialogScreenId,
            title = it.title,
            search = it.search,
            themeItems = it.themeItems,
            checkedThemeItems = it.checkedThemeItems,
            temporaryThemeItems = it.temporaryThemeItems
        )
    }