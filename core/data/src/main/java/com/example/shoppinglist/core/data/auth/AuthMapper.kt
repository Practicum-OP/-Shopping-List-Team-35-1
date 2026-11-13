package com.example.shoppinglist.core.data.auth

import com.example.shoppinglist.core.data.auth.network.dto.AuthResponse
import com.example.shoppinglist.core.data.auth.network.dto.ErrorResponse
import com.example.shoppinglist.core.data.auth.network.dto.TokenPairResponse
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthSession
import com.example.shoppinglist.core.domain.auth.model.AuthTokens

internal fun AuthResponse.toDomain(): AuthSession = AuthSession(
    userId = userId,
    tokens = AuthTokens(accessToken = accessToken, refreshToken = refreshToken),
)

internal fun TokenPairResponse.toDomain(): AuthTokens = AuthTokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
)

internal fun ErrorResponse.toDomain(): AuthError = when (code) {
    "INVALID_EMAIL" -> AuthError.InvalidEmail
    "PASSWORD_TOO_SHORT" -> AuthError.PasswordTooShort
    "PASSWORD_TOO_LONG" -> AuthError.PasswordTooLong
    "USER_EXISTS" -> AuthError.UserAlreadyExists
    "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials
    "INVALID_REFRESH_TOKEN" -> AuthError.InvalidRefreshToken
    else -> AuthError.Unknown(message)
}
