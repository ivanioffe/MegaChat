package com.ioffeivan.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcon
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        contentPadding = contentPadding,
        content = content,
        modifier =
            modifier
                .height(56.dp),
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    PrimaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding =
            if (leadingIcon != null) {
                ButtonDefaults.ButtonWithIconContentPadding
            } else {
                ButtonDefaults.ContentPadding
            },
    ) {
        PrimaryButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun PrimaryOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        contentPadding = contentPadding,
        content = content,
        modifier =
            modifier
                .height(56.dp),
    )
}

@Composable
fun PrimaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    PrimaryOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding =
            if (leadingIcon != null) {
                ButtonDefaults.ButtonWithIconContentPadding
            } else {
                ButtonDefaults.ContentPadding
            },
    ) {
        PrimaryButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun LoadingButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = !isLoading,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    PrimaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .size(32.dp),
                strokeWidth = 3.dp,
            )
        } else {
            content()
        }
    }
}

@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = !isLoading,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    LoadingButton(
        isLoading = isLoading,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding =
            if (leadingIcon != null && !isLoading) {
                ButtonDefaults.ButtonWithIconContentPadding
            } else {
                ButtonDefaults.ContentPadding
            },
    ) {
        PrimaryButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
private fun PrimaryButtonContent(
    text: String,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(
            modifier =
                Modifier
                    .sizeIn(maxHeight = 24.dp),
        ) {
            leadingIcon()
        }
    }

    Box(
        modifier =
            Modifier.padding(
                start =
                    if (leadingIcon != null) {
                        ButtonDefaults.IconSpacing
                    } else {
                        0.dp
                    },
            ),
    ) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonPreviewLight() {
    PreviewContainer(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PrimaryButton(text = "Enabled", onClick = {})

            PrimaryButton(text = "Disabled", enabled = false, onClick = {})

            PrimaryButton(
                text = "With Icon",
                leadingIcon = { PrimaryIcon(id = PrimaryIcons.Lock) },
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryButtonPreviewDark() {
    PreviewContainer(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PrimaryButton(text = "Enabled", onClick = {})

            PrimaryButton(text = "Disabled", enabled = false, onClick = {})

            PrimaryButton(
                text = "With Icon",
                leadingIcon = { PrimaryIcon(id = PrimaryIcons.Lock) },
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryOutlinedButtonPreviewLight() {
    PreviewContainer(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PrimaryOutlinedButton(text = "Enabled", onClick = {})

            PrimaryOutlinedButton(text = "Disabled", enabled = false, onClick = {})

            PrimaryOutlinedButton(
                text = "With Icon",
                leadingIcon = { PrimaryIcon(id = PrimaryIcons.Lock) },
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryOutlinedButtonPreviewDark() {
    PreviewContainer(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PrimaryOutlinedButton(text = "Enabled", onClick = {})

            PrimaryOutlinedButton(text = "Disabled", enabled = false, onClick = {})

            PrimaryOutlinedButton(
                text = "With Icon",
                leadingIcon = { PrimaryIcon(id = PrimaryIcons.Lock) },
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun LoadingButtonPreviewLight() {
    PreviewContainer(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LoadingButton(text = "Submit", isLoading = true, onClick = {})

            LoadingButton(text = "Submit", isLoading = false, onClick = {})
        }
    }
}

@Preview
@Composable
private fun LoadingButtonPreviewDark() {
    PreviewContainer(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LoadingButton(text = "Submit", isLoading = true, onClick = {})

            LoadingButton(text = "Submit", isLoading = false, onClick = {})
        }
    }
}
