package com.jprime.languagenotepad.onboarding.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jprime.languagenotepad.onboarding.model.Language

@Composable
fun OnboardingScreen(languageViewModel: OnBoardingViewModel = viewModel()) {
    // State for the TextField
    val text = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it },
            label = { Text("Enter a language") }
        )

        Button(
            onClick = { languageViewModel.selectLanguage(Language(text.value)) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("OK")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    OnboardingScreen(OnBoardingViewModel())
}