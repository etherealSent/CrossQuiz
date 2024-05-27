package example.quiz.shared.utils

import kotlin.coroutines.CoroutineContext

interface Dispatcher {
    val io: CoroutineContext
}

expect fun provideDispatcher(): Dispatcher