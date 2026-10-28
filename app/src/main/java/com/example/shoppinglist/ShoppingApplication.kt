package com.example.shoppinglist

import android.app.Application
import com.example.shoppinglist.core.data.di.dataModule
import com.example.shoppinglist.core.data.di.networkModule
import com.example.shoppinglist.di.appModule
import com.example.shoppinglist.feature.auth.di.authModule
import com.example.shoppinglist.feature.intro.di.introModule
import com.example.shoppinglist.feature.listdetail.di.listDetailModule
import com.example.shoppinglist.feature.lists.di.listsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ShoppingApplication)

            modules(
                appModule,
                dataModule,
                networkModule,
                introModule,
                authModule,
                listDetailModule,
                listsModule,
            )
        }
    }
}
