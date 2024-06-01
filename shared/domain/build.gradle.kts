plugins {
    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.domain"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
//                implementation(libs.ktor.client.core)
//                implementation(libs.ktor.client.serialization)
//                implementation(libs.ktor.client.content.negotiation)
//                implementation(libs.ktor.serialization.kotlinx.json)
//                implementation(project(":shared:utils"))
//                implementation(project(":shared:domain"))
            }
        }
    }
}