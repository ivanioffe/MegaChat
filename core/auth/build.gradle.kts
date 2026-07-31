plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.hilt)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.ioffeivan.core.auth"

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

    buildFeatures {
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.android.identity.googleid)
}
