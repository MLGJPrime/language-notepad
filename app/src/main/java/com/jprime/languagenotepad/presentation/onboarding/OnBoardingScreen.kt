package com.jprime.languagenotepad.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jprime.languagenotepad.domain.model.Language
import com.jprime.languagenotepad.presentation.common.components.ButtonContent
import com.jprime.languagenotepad.presentation.common.components.CustomButton
import com.jprime.languagenotepad.presentation.common.components.CustomOutlinedTextField

@Composable
fun OnboardingScreen(languageViewModel: OnBoardingViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f)) // Add this line to push the content down

            Text(
                "Welcome to Language Notepad",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center // Center justify the text
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomOutlinedTextField(
                onBoardingViewModel = languageViewModel,
                label = "Enter a language",
                supportingText = if (languageViewModel.isButtonPressed.value == true && languageViewModel.languageText.value.isNullOrEmpty()) "Please enter a valid language" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                content = ButtonContent.Text("OK"),
                onClick = {
                    if (languageViewModel.languageText.value.isNullOrEmpty()) {
                        languageViewModel.onButtonPressed()
                    } else {
                        languageViewModel.selectLanguage(
                            Language(
                                id = 0,
                                languageViewModel.languageText.value ?: ""
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.weight(2f)) // Add this line to push the content down
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    OnboardingScreen(OnBoardingViewModel())
}