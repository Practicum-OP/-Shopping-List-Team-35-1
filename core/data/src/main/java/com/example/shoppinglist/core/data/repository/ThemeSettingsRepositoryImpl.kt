package com.example.shoppinglist.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.shoppinglist.core.data.settings.userPreferencesDataStore
import com.example.shoppinglist.core.domain.settings.ThemeMode
import com.example.shoppinglist.core.domain.settings.ThemeSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ThemeSettingsRepositoryImpl(
    private val context: Context,
) : ThemeSettingsRepository {

    override fun observeThemeMode(): Flow<ThemeMode> {
        return context.userPreferencesDataStore.data
            .catch {
                emit(
                    androidx.datastore.preferences.core.emptyPreferences(),
                )
            }
            .map { preferences ->
                val value = preferences[THEME_MODE_KEY]

                runCatching {
                    ThemeMode.valueOf(value.orEmpty())
                }.getOrDefault(ThemeMode.SYSTEM)
            }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}
