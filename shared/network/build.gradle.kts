plugins {
    id("multiplatform-setup")
    id("android-setup")
    kotlin("plugin.serialization") version "1.9.21"
}

android.namespace = "example.quiz.shared.network"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.serialization)
            }
        }
    }
}