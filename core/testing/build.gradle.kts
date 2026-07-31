plugins {
    alias(libs.plugins.ioffeivan.android.library)
}

android {
    namespace = "com.ioffeivan.core.testing"

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
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.kotlinx.coroutines.test)
    implementation(platform(libs.test.junit5.bom))
    implementation(libs.test.junit5.api)
    implementation(libs.test.junit5.params)
}
