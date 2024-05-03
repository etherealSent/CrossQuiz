package example.quiz.shared.database.quizdata

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.db.SqlDriver
import com.badoo.reaktive.base.setCancellable
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.coroutinesinterop.maybeFromCoroutine
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.mapNotNull
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.flatMapSingle
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.replay
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.asObservable
import com.badoo.reaktive.single.flatMapCompletable
import com.badoo.reaktive.single.flatMapMaybe
import com.badoo.reaktive.single.flatMapObservable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.mapNotNull
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleOf
import example.quiz.shared.quizdata.QuizDatabase

class DefaultQuizSharedDatabase(driver: Single<SqlDriver>) : QuizSharedDatabase {
    constructor(driver: SqlDriver) : this(singleOf(driver))

    private val queries: Single<QuizDatabaseQueries> =
        driver
            .map { it ->
                QuizDatabase(
                    it
                ).quizDatabaseQueries
            }
            .asObservable()
            .replay()
            .autoConnect()
            .firstOrError()


    override fun observeAll(): Observable<List<QuizEntity>> =
        query(QuizDatabaseQueries::selectAll)
            .observe { it.awaitAsList() }

    override fun select(id: Long): Maybe<QuizEntity> =
        query { it.select(id = id) }
            .flatMapMaybe { maybeFromCoroutine { it.awaitAsList() } }
            .mapNotNull { it.firstOrNull() }

    override fun add(title: String, themeList: String): Completable =
        execute { it.add(title = title, themeList = themeList) }

    override fun setTitle(id: Long, title: String): Completable =
        execute { it.setTitle(id = id, title = title) }

    override fun setDescription(id: Long, description: String): Completable =
        execute { it.setDescription(id = id, description = description) }

    override fun setThemeList(id: Long, themeList: String): Completable =
        execute { it.setThemeList(id = id, themeList = themeList) }

    override fun clear(): Completable =
        execute { it.clear() }

    override fun deleteItem(id: Long): Completable =
        execute { it.delete(id = id) }

    private fun <T : Any> query(query: (QuizDatabaseQueries) -> Query<T>): Single<Query<T>> =
        queries
            .observeOn(ioScheduler)
            .map(query)

    private fun execute(query: suspend (QuizDatabaseQueries) -> Unit): Completable =
        queries
            .observeOn(ioScheduler)
            .flatMapCompletable { completableFromCoroutine { query(it) } }

    private fun <T : Any, R> Single<Query<T>>.observe(get: suspend (Query<T>) -> R): Observable<R> =
        flatMapObservable { it.observed() }
            .observeOn(ioScheduler)
            .flatMapSingle { singleFromCoroutine { get(it) } }

    private fun <T : Any> Query<T>.observed(): Observable<Query<T>> =
        observable { emitter ->
            val listener =
                object : Query.Listener {
                    override fun queryResultsChanged() {
                        emitter.onNext(this@observed)
                    }
                }

            emitter.onNext(this@observed)
            addListener(listener)
            emitter.setCancellable { removeListener(listener) }
        }
}