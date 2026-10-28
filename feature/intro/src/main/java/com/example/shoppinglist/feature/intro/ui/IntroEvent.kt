package com.example.shoppinglist.feature.intro.ui

sealed interface IntroEvent {

    data object ScreenClicked : IntroEvent

    data object DelayFinished : IntroEvent
}
