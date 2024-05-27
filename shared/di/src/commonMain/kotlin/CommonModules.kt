import example.quiz.shared.utils.Dispatcher
import example.quiz.shared.utils.provideDispatcher
import org.koin.core.module.Module
import org.koin.dsl.module

private fun dataModule() = module {
    factory<QuizMapper> {
        QuizMapper()
    }

    factory<QuizRepository> {
        QuizRepositoryImpl(
            quizMapper = get(),
            quizRemoteDataSource = get()
        )
    }

    factory<QuizRemoteDataSource> {
        QuizRemoteDataSource(
            ktorService = get(),
            dispatcher = get()
        )
    }

    factory<KtorService> {
        KtorService()
    }
}

private fun utilityModule() = module {
    factory<Dispatcher> {
        provideDispatcher()
    }
}

private fun domainModule() = module {
    factory<GetQuizzesListUseCase> {
        GetQuizzesListUseCase(
            quizRepository = get()
        )
    }
}

private fun commonModules() = listOf(
    domainModule(),
    dataModule(),
    utilityModule(),
)

fun getCommonModules(): List<Module> {
    return commonModules()
}