import com.android.build.api.dsl.ApplicationExtension
import com.ioffeivan.megachat.configureKotlinAndroid
import com.ioffeivan.megachat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = 36

                dependencies {
                    "implementation"(libs.findLibrary("androidx-activity-compose").get())
                    "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

                    "androidTestImplementation"(libs.findLibrary("androidx-test-runner").get())
                }
            }
        }
    }
}
