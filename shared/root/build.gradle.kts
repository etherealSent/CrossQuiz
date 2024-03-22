import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
}

android.namespace = "example.quiz.shared.root"

kotlin {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { target ->
            target.binaries {
                framework {
                    baseName = "Quiz"
                    linkerOpts.add("-lsqlite3")
                    export(project(":shared:database"))
                    export(project(":shared:quizList"))
//                    export(project(":common:edit"))
                    export(libs.arkivanov.mvikotlin)
                    export(libs.arkivanov.mvikotlin.main)
                    export(libs.arkivanov.decompose)
                    export(libs.arkivanov.essenty.lifecycle)
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
//                implementation(project(":common:utils"))
                implementation(project(":shared:database"))
                implementation(project(":shared:quizList"))
//                implementation(project(":common:edit"))
                api(libs.arkivanov.mvikotlin)
                api(libs.arkivanov.mvikotlin.main)
                api(libs.arkivanov.decompose)
                api(libs.arkivanov.essenty.lifecycle)
                implementation(libs.badoo.reaktive)
            }
        }

        iosMain {
            dependencies {
                api(project(":shared:database"))
                api(project(":shared:quizList"))
//                api(project(":common:edit"))
                api(libs.arkivanov.decompose)
                api(libs.arkivanov.mvikotlin.main)
                api(libs.arkivanov.essenty.lifecycle)
            }
        }
    }
}
