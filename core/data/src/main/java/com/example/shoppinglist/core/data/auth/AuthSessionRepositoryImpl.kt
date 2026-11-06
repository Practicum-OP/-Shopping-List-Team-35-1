package com.example.shoppinglist.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.shoppinglist.core.domain.auth.AuthSessionRepository
import com.example.shoppinglist.core.domain.auth.model.AuthSession
import com.example.shoppinglist.core.domain.auth.model.AuthTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.security.GeneralSecurityException

private val SESSION_KEY = stringPreferencesKey(
    "encrypted_auth_session",
)

class AuthSessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val cipher: SessionCipher,
) : AuthSessionRepository {

    override suspend fun getSession(): AuthSession? =
        withContext(Dispatchers.IO) {
            val encrypted = dataStore.data.first()[SESSION_KEY]
                ?: return@withContext null

            val storedSession = try {
                json.decodeFromString<StoredAuthSession>(
                    cipher.decrypt(encrypted),
                )
            } catch (_: GeneralSecurityException) {
                clearSession()
                return@withContext null
            } catch (_: SerializationException) {
                clearSession()
                return@withContext null
            } catch (_: IllegalArgumentException) {
                clearSession()
                return@withContext null
            }

            if (
                storedSession.accessToken.isBlank() ||
                storedSession.refreshToken.isBlank()
            ) {
                clearSession()
                return@withContext null
            }

            AuthSession(
                userId = storedSession.userId,
                tokens = AuthTokens(
                    accessToken = storedSession.accessToken,
                    refreshToken = storedSession.refreshToken,
                ),
            )
        }

    override suspend fun saveSession(
        session: AuthSession,
    ) {
        withContext(Dispatchers.IO) {
            val storedSession = StoredAuthSession(
                userId = session.userId,
                accessToken = session.tokens.accessToken,
                refreshToken = session.tokens.refreshToken,
            )

            val encrypted = cipher.encrypt(
                json.encodeToString(storedSession),
            )

            dataStore.edit { preferences ->
                preferences[SESSION_KEY] = encrypted
            }
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_KEY)
        }
    }
}

@Serializable
private data class StoredAuthSession(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
)
