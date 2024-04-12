package com.jprime.languagenotepad.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jprime.languagenotepad.domain.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    val selectedLanguage: MutableLiveData<Language> = MutableLiveData()
    val languageText: MutableLiveData<String> = MutableLiveData()
    val isButtonPressed: MutableLiveData<Boolean> = MutableLiveData(false)

    fun selectLanguage(language: Language) {
        selectedLanguage.value = language
    }

    fun onLanguageTextChanged(newText: String) {
        languageText.value = newText
    }

    fun onButtonPressed() {
        isButtonPressed.value = true
    }
}