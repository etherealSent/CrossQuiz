enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        repositories {
            mavenLocal()
            google()
            gradlePluginPortal()
            mavenCentral()
            maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
    }
    includeBuild("buildSystem/convention")
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "quiz"
include(
    ":androidApp",
    ":shared",
    ":shared:database",
    ":shared:quizList",
    ":shared:root",
    ":shared:ui",
    ":shared:network",
    ":shared:quizResult",
    ":shared:quizSolve"
)
