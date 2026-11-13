package com.example.shoppinglist.feature.auth.di

import com.example.shoppinglist.core.domain.auth.interactor.Login
import com.example.shoppinglist.core.domain.auth.interactor.Logout
import com.example.shoppinglist.core.domain.auth.interactor.RecoverPassword
import com.example.shoppinglist.core.domain.auth.interactor.Register
import com.example.shoppinglist.core.domain.auth.interactor.RestoreAuthSession
import com.example.shoppinglist.feature.auth.login.LoginViewModel
import com.example.shoppinglist.feature.auth.recovery.RecoveryViewModel
import com.example.shoppinglist.feature.auth.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    factory {
        Login(
            authRepository = get(),
            sessionRepository = get(),
        )
    }

    factory {
        Register(
            authRepository = get(),
        )
    }

    factory {
        RecoverPassword(
            authRepository = get(),
        )
    }

    factory {
        RestoreAuthSession(
            authRepository = get(),
            sessionRepository = get(),
        )
    }

    factory {
        Logout(
            sessionRepository = get(),
        )
    }

    viewModel {
        LoginViewModel(
            login = get(),
            restoreAuthSession = get(),
        )
    }

    viewModel {
        RegistrationViewModel(
            register = get(),
        )
    }

    viewModel {
        RecoveryViewModel(
            recoverPassword = get(),
        )
    }
}
