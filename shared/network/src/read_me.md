Запросы на бэк
Смотрим swagger в поисках нужной ручки и моделей
http://87.249.49.46/users/swagger/
http://87.249.49.46/tests/swagger/
http://87.249.49.46/subjects/swagger/

Запросы и модели описываем в модуле network
Пишем интерфейс репозитория и реализацию

interface SubjectRepo {
suspend fun getSubjectList(): ApiResponse<List<Subject>, Unit>
}

class SubjectRepoImpl(
private val httpClient: HttpClient
): SubjectRepo {

    override suspend fun getSubjectList(): ApiResponse<List<Subject>, Unit> =
        httpClient.safeRequest<List<Subject>, Unit> {
            method = HttpMethod.Get
            url("$BASE_URL/subjects/subjects/list/")
        }
}

Возвращаемый тип - ApiResponse с дженериками
Он уже кэтчит ошибки, поэтому после получения нужно просто примапить sealed результат к UI sealed результату
В билдере прописываем метод, урл и другие параметры по надобности

Для того, чтобы сделать запрос нужен HttpClient
В зависимости нужно добавить следующие библиотеки
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)

Создаем клиент
val httpClient = HttpClient(Android) {
    install(Logging) {
        logger = Logger.DEFAULT
    }
    install(ContentNegotiation) {
        json()
    }
}
В будущем это будет через DI
Можно установить логирование и НАДО установить как будем делать сериализацию - json

В корутине делаем запрос (естественно не в GlobalScope)
GlobalScope.launch {
    val result = SubjectRepoImpl(httpClient).getSubjectList()
    when (result) {
        is ApiResponse.Success -> {}
        is ApiResponse.Error -> {}
    }
}

Post запросы
https://ktor.io/docs/client-requests.html#objects
