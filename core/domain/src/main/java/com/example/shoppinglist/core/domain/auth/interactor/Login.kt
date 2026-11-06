package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.AuthSessionRepository
import com.example.shoppinglist.core.domain.auth.model.AuthResult

class Login(
    private val authRepository: AuthRepository,
    private val sessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
    ): AuthResult<Unit> {
        return when (
            val result = authRepository.login(
                email = email.trim(),
                password = password,
            )
        ) {
            is AuthResult.Success -> {
                sessionRepository.saveSession(result.data)
                AuthResult.Success(Unit)
            }

            is AuthResult.Failure -> {
                result
            }
        }
    }
}
