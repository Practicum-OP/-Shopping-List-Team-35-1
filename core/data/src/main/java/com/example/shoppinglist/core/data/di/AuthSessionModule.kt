package com.example.shoppinglist.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.shoppinglist.core.data.auth.AuthSessionRepositoryImpl
import com.example.shoppinglist.core.data.auth.SessionCipher
import com.example.shoppinglist.core.domain.auth.AuthSessionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val AUTH_SESSION_DATA_STORE = "auth_session_data_store"
private const val AUTH_SESSION_FILE = "auth_session"

val authSessionModule = module {

    single<DataStore<Preferences>>(
        qualifier = named(AUTH_SESSION_DATA_STORE),
    ) {
        val context = androidContext()

        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(
                    AUTH_SESSION_FILE,
                )
            },
        )
    }

    single {
        SessionCipher()
    }

    single<AuthSessionRepository> {
        AuthSessionRepositoryImpl(
            dataStore = get(
                qualifier = named(AUTH_SESSION_DATA_STORE),
            ),
            json = get(),
            cipher = get(),
        )
    }
}
