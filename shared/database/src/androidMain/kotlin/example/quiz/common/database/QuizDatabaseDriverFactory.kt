package example.quiz.common.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import example.quiz.database.QuizDatabase

@Suppress("FunctionName") // FactoryFunction
fun QuizDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
       schema = QuizDatabase.Schema.synchronous(),
        context = context,
        name = "QuizDatabase.db"
    )