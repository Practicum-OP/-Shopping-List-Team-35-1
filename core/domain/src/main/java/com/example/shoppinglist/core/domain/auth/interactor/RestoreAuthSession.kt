package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.AuthSessionRepository
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthResult

class RestoreAuthSession(
    private val authRepository: AuthRepository,
    private val sessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(): AuthResult<Boolean> {
        val session = sessionRepository.getSession()
            ?: return AuthResult.Success(false)

        when (
            val checkResult = authRepository.isTokenValid(
                accessToken = session.tokens.accessToken,
            )
        ) {
            is AuthResult.Success -> {
                if (checkResult.data) {
                    return AuthResult.Success(true)
                }
            }

            is AuthResult.Failure -> {
                return checkResult
            }
        }

        return when (
            val refreshResult = authRepository.refresh(
                refreshToken = session.tokens.refreshToken,
            )
        ) {
            is AuthResult.Success -> {
                sessionRepository.saveSession(
                    session.copy(
                        tokens = refreshResult.data,
                    ),
                )

                AuthResult.Success(true)
            }

            is AuthResult.Failure -> {
                if (
                    refreshResult.error ==
                    AuthError.InvalidRefreshToken
                ) {
                    sessionRepository.clearSession()
                    AuthResult.Success(false)
                } else {
                    refreshResult
                }
            }
        }
    }
}
