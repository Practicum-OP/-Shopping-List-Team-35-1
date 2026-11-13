package com.example.shoppinglist.core.domain.auth.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
)
