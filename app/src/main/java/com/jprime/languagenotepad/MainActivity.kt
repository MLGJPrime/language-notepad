package com.jprime.languagenotepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jprime.languagenotepad.presentation.onboarding.OnBoardingViewModel
import com.jprime.languagenotepad.presentation.onboarding.OnboardingScreen
import com.jprime.languagenotepad.ui.theme.LanguageNotepadTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.selectedLanguage.value == null
            }
        }

        setContent {
            LanguageNotepadTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }

                Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
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