package example.quiz.shared.quizlist.integration

import example.quiz.shared.quizlist.QuizList
import example.quiz.shared.quizlist.store.QuizListStore

internal val stateToModel: (QuizListStore.State) -> QuizList.Model =
    {
        QuizList.Model(
            items = it.items
        )
    }