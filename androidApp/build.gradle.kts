plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

android {
    compileSdk = 34
    namespace = "example.quiz.android"

    defaultConfig {
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared:database:quizdata"))
    implementation(project(":shared:database:themedata"))
    implementation(project(":shared:utils"))
    implementation(project(":shared:root"))
    implementation(project(":shared:ui"))
    implementation(compose.material)
    implementation(libs.arkivanov.mvikotlin)
    implementation(libs.arkivanov.mvikotlin.main)
    implementation(libs.arkivanov.mvikotlin.logging)
    implementation(libs.arkivanov.mvikotlin.timetravel)
    implementation(libs.arkivanov.decompose)
    implementation(libs.arkivanov.decompose.extensions.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.test)
}
