package com.example.shoppinglist.core.data.auth

import com.example.shoppinglist.core.data.auth.network.AuthApi
import com.example.shoppinglist.core.data.auth.network.dto.CredentialsRequest
import com.example.shoppinglist.core.data.auth.network.dto.ErrorResponse
import com.example.shoppinglist.core.data.auth.network.dto.RefreshRequest
import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthResult
import com.example.shoppinglist.core.domain.auth.model.AuthSession
import com.example.shoppinglist.core.domain.auth.model.AuthTokens
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val json: Json,
) : AuthRepository {

    override suspend fun register(email: String, password: String): AuthResult<AuthSession> = safeCall {
        api.register(CredentialsRequest(email, password)).toDomain()
    }

    override suspend fun login(email: String, password: String): AuthResult<AuthSession> = safeCall {
        api.login(CredentialsRequest(email, password)).toDomain()
    }

    override suspend fun refresh(refreshToken: String): AuthResult<AuthTokens> = safeCall {
        api.refresh(RefreshRequest(refreshToken)).toDomain()
    }

    override suspend fun isTokenValid(accessToken: String): AuthResult<Boolean> = safeCall {
        api.check(AuthApi.bearer(accessToken)).isValid
    }

    override suspend fun recoverPassword(email: String): AuthResult<Unit> = safeCall {
        api.recover(email)
    }

    private suspend fun <T> safeCall(block: suspend () -> T): AuthResult<T> = try {
        AuthResult.Success(block())
    } catch (e: HttpException) {
        AuthResult.Failure(e.toAuthError())
    } catch (_: IOException) {
        AuthResult.Failure(AuthError.Network)
    } catch (e: SerializationException) {
        AuthResult.Failure(AuthError.Unknown(e.message))
    }

    private fun HttpException.toAuthError(): AuthError {
        val body = response()?.errorBody()?.string().orEmpty()
        val error = try {
            json.decodeFromString<ErrorResponse>(body)
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
        return error?.toDomain() ?: AuthError.Unknown("HTTP ${code()}")
    }
}
