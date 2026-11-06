package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.model.AuthResult

class Register(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
    ): AuthResult<Unit> {
        return when (
            val result = authRepository.register(
                email = email.trim(),
                password = password,
            )
        ) {
            is AuthResult.Success -> {
                AuthResult.Success(Unit)
            }

            is AuthResult.Failure -> {
                result
            }
        }
    }
}
