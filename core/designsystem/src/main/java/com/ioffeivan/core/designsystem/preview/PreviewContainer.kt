package com.ioffeivan.core.designsystem.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ioffeivan.core.designsystem.theme.MegaChatTheme

/**
 * A wrapper [Composable] used exclusively for Compose Previews.
 *
 * @param content The [Composable] content to be previewed.
 */
@Composable
fun PreviewContainer(
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MegaChatTheme(darkTheme = darkTheme) {
        Surface(
            modifier = modifier,
            content = content,
        )
    }
}
