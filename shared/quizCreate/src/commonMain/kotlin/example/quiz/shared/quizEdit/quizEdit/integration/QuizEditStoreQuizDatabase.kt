package example.quiz.shared.quizEdit.quizEdit.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import example.quiz.shared.database.quizdata.QuizEntity
import example.quiz.shared.database.quizdata.QuizSharedDatabase
import example.quiz.shared.quizEdit.quizEdit.store.QuizEditStoreProvider
import example.quiz.shared.quizlist.QuizListItem

class QuizEditStoreQuizDatabase(
    private val database: QuizSharedDatabase
) : QuizEditStoreProvider.QuizDatabase {
    override fun load(id: Long): Maybe<QuizListItem> =
        database
            .select(id = id)
            .map { it.toItem() }

    private fun QuizEntity.toItem(): QuizListItem = QuizListItem(
        id = id, title = title, description = description, orderNum = orderNum, themeList = themeList.split(",").toList()
    )

    override fun setDescription(id: Long, description: String): Completable =
        database.setDescription(id = id, description = description)

    override fun setThemes(id: Long, themeList: List<String>): Completable =
        database.setThemeList(id = id, themeList = themeList.joinToString(","))

    override fun setTitle(id: Long, title: String): Completable =
        database.setTitle(id = id, title = title)

}