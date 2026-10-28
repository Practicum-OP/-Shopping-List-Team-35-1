package com.example.shoppinglist.feature.intro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val INTRO_DELAY_MILLIS = 4_000L

class IntroViewModel : ViewModel() {

    private val effectChannel = Channel<IntroEffect>(
        capacity = Channel.BUFFERED,
    )

    val effect = effectChannel.receiveAsFlow()

    private var navigationSent = false

    init {
        startDelay()
    }

    fun onEvent(event: IntroEvent) {
        when (event) {
            IntroEvent.ScreenClicked,
            IntroEvent.DelayFinished -> openLogin()
        }
    }

    private fun startDelay() {
        viewModelScope.launch {
            delay(INTRO_DELAY_MILLIS)
            onEvent(IntroEvent.DelayFinished)
        }
    }

    private fun openLogin() {
        if (navigationSent) return

        navigationSent = true
        effectChannel.trySend(IntroEffect.OpenLogin)
    }
}
