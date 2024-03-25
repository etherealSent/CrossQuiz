plugins {
    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.utils"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.arkivanov.mvikotlin)
                implementation(libs.arkivanov.decompose)
                implementation(libs.badoo.reaktive)
            }
        }
    }
}