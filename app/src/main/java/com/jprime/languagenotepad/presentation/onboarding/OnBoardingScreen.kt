package com.jprime.languagenotepad.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jprime.languagenotepad.domain.model.Language
import com.jprime.languagenotepad.presentation.common.components.CustomOutlinedTextField

@Composable
fun OnboardingScreen(languageViewModel: OnBoardingViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome to Language Notepad", style = MaterialTheme.typography.displayMedium)

            Spacer(modifier = Modifier.height(32.dp))

            CustomOutlinedTextField(
                onBoardingViewModel = languageViewModel,
                label = "Enter a language",
                supportingText = "Please enter a valid language"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    languageViewModel.selectLanguage(
                        Language(
                            languageViewModel.languageText.value ?: ""
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("OK")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    OnboardingScreen(OnBoardingViewModel())
}