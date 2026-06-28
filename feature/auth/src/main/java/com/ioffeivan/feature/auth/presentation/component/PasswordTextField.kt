package com.ioffeivan.feature.auth.presentation.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.ioffeivan.core.designsystem.component.PrimaryTextField
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcon
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.feature.auth.R

@Composable
internal fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    onShowPasswordToggle: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    showPassword: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    toggleVisibilityContentDescription: String = stringResource(R.string.toggle_password_visibility),
) {
    PrimaryTextField(
        value = password,
        onValueChange = onPasswordChange,
        enabled = enabled,
        placeholder = placeholder,
        trailingIcon = {
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = onShowPasswordToggle,
                enabled = enabled,
                contentDescription = toggleVisibilityContentDescription,
            )
        },
        supportingText = {
            errorMessage?.let {
                Text(text = errorMessage)
            }
        },
        isError = errorMessage != null,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier,
    )
}

@Composable
private fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = stringResource(R.string.toggle_confirm_password_visibility),
) {
    val icon =
        if (showPassword) {
            PrimaryIcons.Visibility
        } else {
            PrimaryIcons.VisibilityOff
        }

    IconButton(
        onClick = onTogglePasswordVisibility,
        modifier = modifier,
        enabled = enabled,
    ) {
        PrimaryIcon(
            id = icon,
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
private fun PasswordTextFieldPreviewLight() {
    PreviewContainer(darkTheme = false) {
        PasswordTextField(
            password = "password",
            onPasswordChange = {},
            onShowPasswordToggle = {},
        )
    }
}

@Preview
@Composable
private fun PasswordTextFieldPreviewDark() {
    PreviewContainer(darkTheme = true) {
        PasswordTextField(
            password = "password",
            onPasswordChange = {},
            onShowPasswordToggle = {},
        )
    }
}
