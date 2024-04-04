plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("app.cash.sqldelight")
}

android.namespace = "example.quiz.shared.quizdata"

sqldelight {
    databases {
        create("QuizDatabase") {
            packageName.set("example.quiz.shared.quizdata")
            generateAsync = true
        }
    }
}

kotlin {
    sourceSets {
        kotlin {
            commonMain {
                dependencies {
                    implementation(libs.badoo.reaktive)
                    implementation(libs.badoo.reaktive.coroutines.interop)
                }
            }

            androidMain {
                dependencies {
                    implementation(libs.squareup.sqldelight.android.driver)
                    implementation(libs.squareup.sqldelight.sqlite.driver)
                }
            }

//            desktopMain {
//                dependencies {
//                    implementation(libs.squareup.sqldelight.sqlite.driver)
//                }
//            }
//
//            iosMain {
//                dependencies {
//                    implementation(libs.squareup.sqldelight.native.driver)
//                }
//            }
//
//            jsMain {
//                dependencies {
//                    implementation(libs.squareup.sqldelight.webworker.driver)
//                }
//            }
        }
    }
}