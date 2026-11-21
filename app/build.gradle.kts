plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "za.co.deathbook.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "za.co.deathbook.app"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
}
