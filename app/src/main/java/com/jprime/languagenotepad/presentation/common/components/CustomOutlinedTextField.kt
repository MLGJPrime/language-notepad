package com.jprime.languagenotepad.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.jprime.languagenotepad.presentation.onboarding.OnBoardingViewModel

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    onBoardingViewModel: OnBoardingViewModel,
    label: String,
    supportingText: String? = null,
    leadingIcon: ImageVector? = null,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = onBoardingViewModel.languageText.value ?: "",
                onValueChange = onBoardingViewModel::onLanguageTextChanged,
                label = {
                    Text(text = label)
                },
                leadingIcon = {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null
                        )
                    }
                },
                supportingText = {
                    if (supportingText != null) {
                        Text(text = supportingText)
                    }
                },
                isError = false,
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCustomOutlinedTextField() {
    CustomOutlinedTextField(
        onBoardingViewModel = OnBoardingViewModel(),
        label = "Enter language",
        supportingText = "Please enter a valid language"
    )
}