package example.quiz.shared.quizlist.dialogQuizCreate.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable
import dialog.store.QuizDialogStoreProvider
import example.quiz.shared.database.themedata.ThemeEntity
import example.quiz.shared.database.themedata.ThemeSharedDatabase
import example.quiz.shared.quizlist.ThemeListItem

class QuizDialogStoreThemeDatabase(
    private val database: ThemeSharedDatabase
) : QuizDialogStoreProvider.ThemeDatabase {
    override val updates: Observable<List<ThemeListItem>> =
        database.observeAll().mapIterable { it.toItem() }

    private fun ThemeEntity.toItem(): ThemeListItem = ThemeListItem(
        id = id, orderNum = orderNum, title = title, temporary = false
    )

    override fun add(title: String): Completable = database.add(title)
}