package example.quiz.shared.database.themedata

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable

interface ThemeSharedDatabase {

    fun observeAll(): Observable<List<ThemeEntity>>

    fun selectById(listId: List<Long>): Maybe<List<ThemeEntity>>

    fun select(id: Long): Maybe<ThemeEntity>

    fun add(title: String): Completable

    fun clear(): Completable
}