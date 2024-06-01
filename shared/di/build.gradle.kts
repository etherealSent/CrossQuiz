plugins {
    id("multiplatform-setup")
    id("android-setup")
}

android.namespace = "example.quiz.shared.di"

kotlin {
    sourceSets {
        commonMain {
            dependencies {

                api(libs.koin.core)
                implementation(project(":shared:network"))
                implementation(project(":shared:domain"))
                implementation(project(":shared:utils"))
            }
        }
    }
}