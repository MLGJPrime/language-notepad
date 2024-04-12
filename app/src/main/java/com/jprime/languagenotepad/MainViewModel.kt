package com.jprime.languagenotepad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jprime.languagenotepad.domain.usecases.app_entry.AppEntryUseCases
import com.jprime.languagenotepad.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases,
) : ViewModel() {

    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        viewModelScope.launch {
            val shouldStartFromHomeScreen = appEntryUseCases.shouldStartFromHomeScreen()
            startDestination = if (shouldStartFromHomeScreen) {
                Route.LanguageNavigation.route
            } else {
                Route.AppStartNavigation.route
            }
            delay(300)
            splashCondition = false
        }
    }
}