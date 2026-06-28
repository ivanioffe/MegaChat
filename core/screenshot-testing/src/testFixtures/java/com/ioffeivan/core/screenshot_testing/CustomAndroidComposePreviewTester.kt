package com.ioffeivan.core.screenshot_testing

import com.dropbox.differ.SimpleImageComparator
import com.github.takahirom.roborazzi.AndroidComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester.TestParameter.JUnit4TestParameter.AndroidPreviewJUnit4TestParameter
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

private const val MAX_DISTANCE: Float = 0.01F

@OptIn(ExperimentalRoborazziApi::class)
class CustomAndroidComposePreviewTester :
    ComposePreviewTester<AndroidPreviewJUnit4TestParameter> by AndroidComposePreviewTester() {
    override fun test(testParameter: AndroidPreviewJUnit4TestParameter) {
        val preview = testParameter.preview
        val filePath = createFilePath(preview)

        preview.captureRoboImage(
            filePath = "src/test/screenshots/$filePath",
            roborazziOptions =
                RoborazziOptions(
                    compareOptions =
                        RoborazziOptions.CompareOptions(
                            imageComparator =
                                SimpleImageComparator(
                                    maxDistance = MAX_DISTANCE,
                                    vShift = 1,
                                    hShift = 1,
                                ),
                        ),
                ),
        )
    }

    private fun createFilePath(preview: ComposablePreview<AndroidPreviewInfo>): String {
        val moduleNamespace = System.getProperty("roborazzi.module.namespace") ?: ""

        val fullClassName = preview.declaringClass

        val packageName = fullClassName.substringBeforeLast(".")
        val relativePackage =
            packageName
                .removePrefix(moduleNamespace)
                .removePrefix(".")

        val folderPath = relativePackage.replace(".", "/")

        val fileName = "${preview.methodName}.png"
        val filePath = if (folderPath.isEmpty()) fileName else "$folderPath/$fileName"

        return filePath
    }
}
