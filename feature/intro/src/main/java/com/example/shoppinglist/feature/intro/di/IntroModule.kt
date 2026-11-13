package com.example.shoppinglist.feature.intro.di

import com.example.shoppinglist.feature.intro.ui.IntroViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val introModule = module {
    viewModelOf(::IntroViewModel)
}
