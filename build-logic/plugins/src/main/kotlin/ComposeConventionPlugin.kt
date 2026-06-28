import com.ioffeivan.megachat.commonExtension
import com.ioffeivan.megachat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            commonExtension.apply {
                buildFeatures {
                    compose = true
                }

                dependencies {
                    "implementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                    "implementation"(libs.findLibrary("androidx-compose-ui").get())
                    "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
                    "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                    "implementation"(libs.findLibrary("androidx-compose-material3").get())

                    "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
                }
            }
        }
    }
}
