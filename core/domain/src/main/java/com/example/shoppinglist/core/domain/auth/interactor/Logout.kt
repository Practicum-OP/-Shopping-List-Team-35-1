package com.example.shoppinglist.core.domain.auth.interactor

import com.example.shoppinglist.core.domain.auth.AuthSessionRepository

class Logout(
    private val sessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke() {
        sessionRepository.clearSession()
    }
}
