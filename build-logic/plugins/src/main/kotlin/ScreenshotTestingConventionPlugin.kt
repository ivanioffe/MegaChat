import com.android.build.api.dsl.LibraryExtension
import com.ioffeivan.megachat.commonExtension
import com.ioffeivan.megachat.libs
import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ScreenshotTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.takahirom.roborazzi")

            val namespaceProvider =
                target.provider {
                    commonExtension.namespace ?: ""
                }

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                        isReturnDefaultValues = true

                        all {
                            it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                            it.systemProperties["roborazzi.module.namespace"] =
                                namespaceProvider.get()
                        }
                    }
                }

                extensions.configure<RoborazziExtension> {
                    outputDir.set(file("src/test/screenshots"))

                    generateComposePreviewRobolectricTests {
                        enable.set(true)
                        includePrivatePreviews.set(true)
                        useScanOptionParametersInTester.set(true)

                        packages.set(namespaceProvider.map { listOf(it) })

                        robolectricConfig.set(
                            mapOf(
                                "sdk" to "[33]",
                                "qualifiers" to "RobolectricDeviceQualifiers.Pixel5",
                            ),
                        )

                        testerQualifiedClassName.set(
                            "com.ioffeivan.core.screenshot_testing.CustomAndroidComposePreviewTester",
                        )
                    }
                }

                dependencies {
                    "testImplementation"(testFixtures(project(":core:screenshot-testing")))

                    "testImplementation"(libs.findLibrary("androidx.compose.ui.test.junit4").get())
                    "testImplementation"(libs.findLibrary("androidx.compose.ui.test.manifest").get())
                    "testImplementation"(libs.findLibrary("composable.preview.scanner").get())
                    "testImplementation"(libs.findLibrary("junit").get())
                    "testImplementation"(libs.findLibrary("junit.vintage.engine").get())
                    "testImplementation"(libs.findLibrary("robolectric").get())
                    "testImplementation"(libs.findLibrary("roborazzi.previewScanner").get())
                }
            }
        }
    }
}
