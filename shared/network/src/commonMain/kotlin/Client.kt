import io.ktor.client.HttpClient

// expect val apiClient: HttpClient

/*
Client example
val httpClient = HttpClient(Android) {
            install(Logging) {
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                json()
            }
        }
 */

/*
impls

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)
    implementation(libs.ch.qos.logback)
 */