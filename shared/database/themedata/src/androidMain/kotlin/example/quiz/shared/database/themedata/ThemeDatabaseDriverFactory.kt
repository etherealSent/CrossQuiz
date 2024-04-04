package example.quiz.shared.database.themedata

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import example.quiz.shared.themedata.ThemeDatabase

@Suppress("FunctionName") // FactoryFunction
fun ThemeDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = ThemeDatabase.Schema.synchronous(),
        context = context,
        name = "ThemeDatabase.db"
    )