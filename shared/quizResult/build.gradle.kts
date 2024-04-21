plugins {
    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.quizResult"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
//                implementation(project(":shared:utils"))
//                implementation(project(":shared:database:quizdatabase"))
                implementation(project(":shared:network"))
                implementation(libs.arkivanov.decompose)
                implementation(libs.arkivanov.mvikotlin)
                implementation(libs.arkivanov.mvikotlin.extensions.coroutines)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.arkivanov.mvikotlin.main)
                implementation(libs.badoo.reaktive.testing)
            }
        }
    }
}