package com.example.shoppinglist.core.domain.settings

import kotlinx.coroutines.flow.Flow

interface ThemeSettingsRepository {

    fun observeThemeMode(): Flow<ThemeMode>

    suspend fun setThemeMode(mode: ThemeMode)
}
