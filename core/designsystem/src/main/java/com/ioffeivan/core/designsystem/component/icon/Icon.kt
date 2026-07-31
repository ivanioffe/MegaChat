package com.ioffeivan.core.designsystem.component.icon

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

/**
 * Primary icon component for Design System.
 *
 * Displays a drawable resource vector icon.
 * Use with [PrimaryIcons] for consistent iconography.
 *
 * @param id Drawable resource ID of the vector icon.
 * @param modifier Modifier for layout customization.
 * @param contentDescription Accessibility description for screen readers.
 * @param tint Icon color.
 */
@Composable
fun PrimaryIcon(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        imageVector = ImageVector.vectorResource(id),
        modifier = modifier,
        contentDescription = contentDescription,
        tint = tint,
    )
}
