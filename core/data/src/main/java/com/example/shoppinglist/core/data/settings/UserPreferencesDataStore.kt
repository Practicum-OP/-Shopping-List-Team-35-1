package com.example.shoppinglist.core.data.settings

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences",
)
