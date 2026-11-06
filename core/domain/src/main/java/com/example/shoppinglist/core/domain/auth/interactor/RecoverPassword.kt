package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthRepository
import com.example.shoppinglist.core.domain.auth.model.AuthResult

class RecoverPassword(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
    ): AuthResult<Unit> {
        return authRepository.recoverPassword(
            email = email.trim(),
        )
    }
}
