package com.jprime.languagenotepad.onboarding.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jprime.languagenotepad.onboarding.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    val selectedLanguage: MutableLiveData<Language> = MutableLiveData()

    fun selectLanguage(language: Language) {
        selectedLanguage.value = language
    }
}