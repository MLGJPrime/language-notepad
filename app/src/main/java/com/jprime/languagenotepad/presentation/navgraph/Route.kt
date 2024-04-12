package com.jprime.languagenotepad.presentation.navgraph

sealed class Route(
    val route: String,
) {

    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object NotepadScreen : Route(route = "notepadScreen")
    object CreateClassScreen : Route(route = "createClassScreen")
    object FlashCardScreen : Route(route = "flashCardScreen")
    object DetailsScreen : Route(route = "detailsScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object LanguageNavigation : Route(route = "languageNavigation")
    object LanguageNavigatorScreen : Route(route = "languageNavigator")
}