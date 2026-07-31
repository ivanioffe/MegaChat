plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.ioffeivan.core.screenshot_testing"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
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

    testFixtures {
        enable = true
    }
}

dependencies {
    testFixturesImplementation(libs.composable.preview.scanner)
    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.robolectric)
    testFixturesImplementation(libs.roborazzi.previewScanner)
}
