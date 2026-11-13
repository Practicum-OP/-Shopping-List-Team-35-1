package com.example.shoppinglist.core.domain.auth

import com.example.shoppinglist.core.domain.auth.model.AuthResult
import com.example.shoppinglist.core.domain.auth.model.AuthSession
import com.example.shoppinglist.core.domain.auth.model.AuthTokens

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthResult<AuthSession>

    suspend fun login(email: String, password: String): AuthResult<AuthSession>

    suspend fun refresh(refreshToken: String): AuthResult<AuthTokens>

    /** `false` means the access token expired or is unknown: call [refresh]. */
    suspend fun isTokenValid(accessToken: String): AuthResult<Boolean>

    suspend fun recoverPassword(email: String): AuthResult<Unit>
}
