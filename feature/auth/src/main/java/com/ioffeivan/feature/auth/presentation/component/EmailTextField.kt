package com.ioffeivan.feature.auth.presentation.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ioffeivan.core.designsystem.component.PrimaryTextField
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.feature.auth.R

@Composable
internal fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    PrimaryTextField(
        value = email,
        onValueChange = onEmailChange,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(R.string.email))
        },
        supportingText = {
            errorMessage?.let {
                Text(text = errorMessage)
            }
        },
        isError = errorMessage != null,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun EmailTextFieldPreviewLight() {
    PreviewContainer(darkTheme = false) {
        EmailTextField(
            email = "email",
            onEmailChange = {},
        )
    }
}

@Preview
@Composable
private fun EmailTextFieldPreviewDark() {
    PreviewContainer(darkTheme = true) {
        EmailTextField(
            email = "email",
            onEmailChange = {},
        )
    }
}
