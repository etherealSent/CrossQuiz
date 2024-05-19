package example.quiz.shared.quizEdit.addQuestion.integration

import example.quiz.shared.quizEdit.addQuestion.AddQuestion
import example.quiz.shared.quizEdit.addQuestion.store.AddQuestionStore

internal val stateToModel: (AddQuestionStore.State) -> AddQuestion.Model =
    {
        AddQuestion.Model(
            showDialog = it.showDialog,
            questionType = it.questionType,
            questionTypes = it.questionTypes
        )
    }