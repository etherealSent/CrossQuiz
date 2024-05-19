package example.quiz.shared.quizEdit.questions.integration

import example.quiz.shared.quizEdit.questions.Questions
import example.quiz.shared.quizEdit.questions.store.QuestionStore


internal val stateToModel: (QuestionStore.State) -> Questions.Model =
    {
        Questions.Model(
            questions = it.questions,
            selectedQuestionId = it.selectedQuestionId
        )
    }