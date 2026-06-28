package com.ioffeivan.feature.onboarding.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ioffeivan.core.designsystem.component.PrimaryOutlinedButton
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcon
import com.ioffeivan.core.designsystem.component.icon.PrimaryIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.feature.onboarding.R

@Composable
internal fun SignInWithPasswordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryOutlinedButton(
        text = stringResource(R.string.sign_in_with_password),
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            PrimaryIcon(
                id = PrimaryIcons.Lock,
            )
        },
    )
}

@Preview
@Composable
private fun SignInWithPasswordButtonPreviewLight() {
    PreviewContainer(darkTheme = false) {
        SignInWithPasswordButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SignInWithPasswordButtonPreviewDark() {
    PreviewContainer(darkTheme = true) {
        SignInWithPasswordButton(
            onClick = {},
        )
    }
}
