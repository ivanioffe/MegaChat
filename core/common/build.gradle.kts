plugins {
    alias(libs.plugins.ioffeivan.jvm.library)
    alias(libs.plugins.ioffeivan.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
