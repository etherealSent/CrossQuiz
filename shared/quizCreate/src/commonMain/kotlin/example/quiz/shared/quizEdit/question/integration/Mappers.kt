package example.quiz.shared.quizEdit.question.integration

import example.quiz.shared.quizEdit.question.Question
import example.quiz.shared.quizEdit.question.store.QuestionStore
import example.quiz.shared.quizEdit.quizEdit.QuizEdit
import example.quiz.shared.quizEdit.quizEdit.store.QuizEditStore


internal val stateToModel: (QuestionStore.State) -> Question.Model =
    {
        Question.Model(
            type = it.questionType,
            question = it.question,
            answers = it.answers,
            rightAnswers = it.rightAnswers,
            settings = it.settings
        )
    }