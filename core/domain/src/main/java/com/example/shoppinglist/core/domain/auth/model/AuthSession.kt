package com.example.shoppinglist.core.domain.auth.model

/**
 * Result of a successful registration or login.
 * `auth/refresh` does not return [userId], so it has to be kept from this session.
 */
data class AuthSession(
    val userId: Long,
    val tokens: AuthTokens,
)
