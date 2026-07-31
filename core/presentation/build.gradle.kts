plugins {
    alias(libs.plugins.ioffeivan.android.library)
}

android {
    namespace = "com.ioffeivan.core.presentation"

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
    implementation(projects.core.domain.base)

    implementation(libs.androidx.annotation.jvm)
}
