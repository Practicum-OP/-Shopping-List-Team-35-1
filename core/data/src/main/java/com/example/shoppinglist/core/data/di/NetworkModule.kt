package com.example.shoppinglist.core.data.di

import com.example.shoppinglist.core.data.auth.AuthRepositoryImpl
import com.example.shoppinglist.core.data.auth.network.AuthApi
import com.example.shoppinglist.core.domain.auth.AuthRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://faiwlkhyssrgofauzegs.supabase.co/functions/v1/"

val networkModule = module {
    single {
        Json { ignoreUnknownKeys = true }
    }
    single {
        OkHttpClient.Builder()
            // BASIC logs only method, URL and status: no tokens, passwords or email header.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(AuthApi::class.java) }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}
