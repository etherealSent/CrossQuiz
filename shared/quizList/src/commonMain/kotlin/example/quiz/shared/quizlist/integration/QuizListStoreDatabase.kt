package example.quiz.shared.quizlist.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable
import example.quiz.shared.database.quizdata.QuizEntity
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.quizlist.QuizListItem
import example.quiz.shared.quizlist.store.QuizListStoreProvider

class QuizListStoreDatabase(
    private val database: QuizSharedDatabase
) : QuizListStoreProvider.Database {
    override val updates: Observable<List<QuizListItem>> =
        database
            .observeAll()
            .mapIterable { it.toItem() }

    private fun QuizEntity.toItem(): QuizListItem =
        QuizListItem(
            id = id,
            title = title,
            orderNum = orderNum,
            themeList = themeList.split(",").toList()
        )

    override fun add(title: String, listThemes: String): Completable =
        database.add(title = title, themeList = listThemes)

    override fun delete(id: Long): Completable =
        database.deleteItem(id = id)
}