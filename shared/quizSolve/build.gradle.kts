plugins {
    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.quizSolve"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
//                implementation(project(":shared:utils"))
                implementation(project(":shared:database"))
                implementation(libs.arkivanov.decompose)
                implementation(libs.arkivanov.mvikotlin)
                implementation(libs.arkivanov.mvikotlin.extensions.reaktive)
                implementation(libs.badoo.reaktive)
                implementation(libs.arkivanov.mvikotlin.extensions.coroutines)
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