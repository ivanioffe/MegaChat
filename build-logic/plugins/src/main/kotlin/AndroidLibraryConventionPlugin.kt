import com.android.build.api.dsl.LibraryExtension
import com.ioffeivan.megachat.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                testOptions.targetSdk = 36
                lint.targetSdk = 36
                defaultConfig.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}
