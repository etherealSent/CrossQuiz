package example.quiz.shared.quizEdit.quizEdit.integration

import example.quiz.shared.quizEdit.quizEdit.QuizEdit
import example.quiz.shared.quizEdit.quizEdit.store.QuizEditStore

internal val stateToModel: (QuizEditStore.State) -> QuizEdit.Model =
    {
        QuizEdit.Model(
            id = it.id,
            orderNum = it.orderNum,
            title = it.title,
            themeList = it.themeList,
            setup = it.setup,
            description = it.description,
            showMoreVertMenu = it.showMoreVertMenu,
        )
    }