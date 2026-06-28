plugins {
    alias(libs.plugins.ioffeivan.android.application)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ioffeivan.megachat"

    defaultConfig {
        applicationId = "com.ioffeivan.megachat"

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)

    implementation(projects.feature.auth)
    implementation(projects.feature.onboarding)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
