package com.jprime.languagenotepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jprime.languagenotepad.ui.theme.LanguageNotepadTheme
import com.jprime.languagenotepad.onboarding.presentation.OnboardingScreen
import com.jprime.languagenotepad.onboarding.presentation.OnBoardingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageNotepadTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: OnBoardingViewModel = viewModel()
                    if (viewModel.selectedLanguage.value == null) {
                        OnboardingScreen(viewModel)
                    } else {
                        // Skip onboarding and load main app UI
                    }
                }
            }
        }
    }
}