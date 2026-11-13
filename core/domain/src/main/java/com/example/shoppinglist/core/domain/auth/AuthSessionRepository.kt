package com.example.shoppinglist.core.domain.auth

import com.example.shoppinglist.core.domain.auth.model.AuthSession

interface AuthSessionRepository {

    suspend fun getSession(): AuthSession?

    suspend fun saveSession(session: AuthSession)

    suspend fun clearSession()
}
