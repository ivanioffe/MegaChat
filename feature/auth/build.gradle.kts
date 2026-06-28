plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.hilt)
    alias(libs.plugins.ioffeivan.screenshotTesting)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ioffeivan.feature.auth"

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
    implementation(projects.core.auth)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain.base)
    implementation(projects.core.firebase)
    implementation(projects.core.mvu)
    implementation(projects.core.presentation)
    implementation(projects.core.ui)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.firebase.auth)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(platform(libs.test.junit5.bom))
    testImplementation(libs.test.junit5.api)
    testImplementation(libs.test.junit5.params)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(testFixtures(projects.core.mvu))
    testImplementation(projects.core.testing)

    testRuntimeOnly(libs.test.junit.platform.launcher)
    testRuntimeOnly(libs.test.junit5.engine)
}
