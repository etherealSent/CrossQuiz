package example.quiz.android

import android.app.Application
import com.arkivanov.mvikotlin.timetravel.server.TimeTravelServer
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TimeTravelServer().start()
    }
}