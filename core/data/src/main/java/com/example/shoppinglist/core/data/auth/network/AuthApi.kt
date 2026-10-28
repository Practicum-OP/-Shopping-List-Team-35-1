package com.example.shoppinglist.core.data.auth.network

import com.example.shoppinglist.core.data.auth.network.dto.AuthResponse
import com.example.shoppinglist.core.data.auth.network.dto.CredentialsRequest
import com.example.shoppinglist.core.data.auth.network.dto.RefreshRequest
import com.example.shoppinglist.core.data.auth.network.dto.TokenCheckResponse
import com.example.shoppinglist.core.data.auth.network.dto.TokenPairResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Paths have no leading slash: otherwise Retrofit drops `/functions/v1/` from the base URL.
 */
interface AuthApi {
    /**
     * @param debugAccessTtlSeconds access token lifetime on the stand (5..900 s), handy for testing refresh.
     * Not sent when `null`.
     */
    @POST("auth/registration")
    suspend fun register(
        @Body body: CredentialsRequest,
        @Header(DEBUG_ACCESS_TTL_HEADER) debugAccessTtlSeconds: Int? = null,
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body body: CredentialsRequest,
        @Header(DEBUG_ACCESS_TTL_HEADER) debugAccessTtlSeconds: Int? = null,
    ): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): TokenPairResponse

    /** Always answers 200: an expired or unknown token comes back as `is_valid = false`. */
    @GET("auth/check")
    suspend fun check(@Header(AUTHORIZATION_HEADER) authorization: String): TokenCheckResponse

    /** Email goes in a header, not in the body. */
    @POST("auth/recovery")
    suspend fun recover(@Header(EMAIL_HEADER) email: String)

    companion object {
        const val DEBUG_ACCESS_TTL_HEADER = "X-Debug-Access-Ttl"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val EMAIL_HEADER = "email"

        fun bearer(accessToken: String): String = "Bearer $accessToken"
    }
}
