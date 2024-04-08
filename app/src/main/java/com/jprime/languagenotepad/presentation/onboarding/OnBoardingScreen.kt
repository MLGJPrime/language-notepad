package com.jprime.languagenotepad.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jprime.languagenotepad.domain.model.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(languageViewModel: OnBoardingViewModel = viewModel()) {
    // State for the TextField
    val text = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome to Language Notepad", style = MaterialTheme.typography.displayMedium)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Enter a language") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { languageViewModel.selectLanguage(Language(text.value)) },
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