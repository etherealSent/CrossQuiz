package example.quiz.shared.quizEdit.editThemes.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.toMaybe
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.mapIterable
import example.quiz.shared.database.themedata.ThemeEntity
import example.quiz.shared.database.themedata.ThemeSharedDatabase
import example.quiz.shared.quizEdit.editThemes.store.EditThemesStoreProvider
import example.quiz.shared.quizlist.ThemeListItem
import kotlinx.coroutines.selects.select

class EditThemesStoreDatabase(
    private val database: ThemeSharedDatabase
) : EditThemesStoreProvider.Database {
    override val updates: Observable<List<ThemeListItem>> =
        database
            .observeAll()
            .mapIterable { it.toItem() }


    private fun ThemeEntity.toItem(): ThemeListItem =
        ThemeListItem(
            id = id,
            orderNum = orderNum,
            title = title,
            temporary = false
        )

    override fun add(title: String): Completable =
        database.add(title)
}