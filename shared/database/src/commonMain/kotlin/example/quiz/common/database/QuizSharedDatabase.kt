package example.quiz.common.database

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

interface QuizSharedDatabase {

    fun observeAll(): Observable<List<QuizEntity>>

    // todo: replace setup string with Setup class
    fun add(title: String, themeList: String): Completable

    fun setTitle(id: Long, title: String): Completable

//    fun setImageUrl(id: Long): Completable

    fun setThemeList(id: Long, themeList: String): Completable

//        fun setCreatorName(id: Long, creatorName: String): Completable

//        fun setStartDate(id: Long, startDate: String): Completable

    fun clear(): Completable
}