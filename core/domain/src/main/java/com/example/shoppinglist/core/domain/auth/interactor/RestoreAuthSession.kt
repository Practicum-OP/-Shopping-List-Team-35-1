package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.AuthSessionRepository
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthResult
import com.example.shoppinglist.core.domain.auth.model.AuthSession

class RestoreAuthSession(
    private val authRepository: AuthRepository,
    private val sessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(): AuthResult<Boolean> {
        val session = sessionRepository.getSession()

        return if (session == null) {
            AuthResult.Success(false)
        } else {
            restoreSession(session)
        }
    }

    private suspend fun restoreSession(
        session: AuthSession,
    ): AuthResult<Boolean> {
        val result = authRepository.isTokenValid(
            accessToken = session.tokens.accessToken,
        )

        return when (result) {
            is AuthResult.Success -> {
                if (result.data) {
                    AuthResult.Success(true)
                } else {
                    refreshSession(session)
                }
            }

            is AuthResult.Failure -> {
                result
            }
        }
    }

    private suspend fun refreshSession(
        session: AuthSession,
    ): AuthResult<Boolean> {
        val result = authRepository.refresh(
            refreshToken = session.tokens.refreshToken,
        )

        return when (result) {
            is AuthResult.Success -> {
                sessionRepository.saveSession(
                    session.copy(
                        tokens = result.data,
                    ),
                )

                AuthResult.Success(true)
            }

            is AuthResult.Failure -> {
                handleRefreshFailure(result)
            }
        }
    }

    private suspend fun handleRefreshFailure(
        failure: AuthResult.Failure,
    ): AuthResult<Boolean> {
        return if (
            failure.error == AuthError.InvalidRefreshToken
        ) {
            sessionRepository.clearSession()
            AuthResult.Success(false)
        } else {
            failure
        }
    }
}
