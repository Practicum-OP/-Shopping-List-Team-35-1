package com.example.shoppinglist.core.domain.settings

import kotlinx.coroutines.flow.Flow

class ObserveThemeMode(
    private val repository: ThemeSettingsRepository,
) {
    operator fun invoke(): Flow<ThemeMode> {
        return repository.observeThemeMode()
    }
}

class ChangeThemeMode(
    private val repository: ThemeSettingsRepository,
) {
    suspend operator fun invoke(mode: ThemeMode) {
        repository.setThemeMode(mode)
    }
}
