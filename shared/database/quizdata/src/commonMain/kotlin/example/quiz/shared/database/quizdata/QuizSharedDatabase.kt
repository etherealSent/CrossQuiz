package example.quiz.shared.database.quizdata

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

interface QuizSharedDatabase {

    fun observeAll(): Observable<List<QuizEntity>>

    fun add(title: String, themeList: String): Completable

    fun setTitle(id: Long, title: String): Completable

//    fun setImageUrl(id: Long): Completable

    fun setThemeList(id: Long, themeList: String): Completable

    fun deleteItem(id: Long): Completable

//        fun setCreatorName(id: Long, creatorName: String): Completable

//        fun setStartDate(id: Long, startDate: String): Completable

    fun clear(): Completable
}