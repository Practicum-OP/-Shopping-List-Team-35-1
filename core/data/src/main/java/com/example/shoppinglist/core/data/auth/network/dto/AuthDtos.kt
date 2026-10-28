package com.example.shoppinglist.core.data.auth.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("user_id") val userId: Long,
)

@Serializable
data class TokenPairResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable
data class TokenCheckResponse(
    @SerialName("is_valid") val isValid: Boolean,
)

@Serializable
data class ErrorResponse(
    @SerialName("code") val code: String,
    @SerialName("message") val message: String? = null,
)
