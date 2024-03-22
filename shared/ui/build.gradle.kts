plugins {
    id("multiplatform-compose-setup")
//    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.ui"
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:quizList"))
                implementation(project(":shared:quizSolve"))
                implementation(project(":shared:quizResult"))
//                implementation(project(":common:edit"))
                implementation(project(":shared:root"))
                implementation(libs.arkivanov.decompose)
                implementation(libs.arkivanov.decompose.extensions.compose)
            }
        }
    }
}
