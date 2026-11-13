package com.example.shoppinglist.feature.auth.model

import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthResult
import kotlinx.coroutines.CancellationException

internal suspend fun <T> authOperation(
    block: suspend () -> AuthResult<T>,
): AuthResult<T> {
    return try {
        block()
    } catch (exception: CancellationException) {
        throw exception
    } catch (_: Exception) {
        AuthResult.Failure(
            AuthError.Unknown(),
        )
    }
}
