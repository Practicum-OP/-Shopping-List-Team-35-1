package com.example.shoppinglist.core.data.auth

import com.example.shoppinglist.core.data.auth.network.AuthApi
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.core.domain.auth.model.AuthResult
import com.example.shoppinglist.core.domain.auth.model.AuthSession
import com.example.shoppinglist.core.domain.auth.model.AuthTokens
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AuthRepositoryImplTest {

    private val server = MockWebServer()
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        server.start()
        val api = Retrofit.Builder()
            .baseUrl(server.url("/functions/v1/"))
            .client(OkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AuthApi::class.java)
        repository = AuthRepositoryImpl(api, json)
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `login keeps functions prefix and maps session`() = runTest {
        server.enqueue(jsonResponse(200, """{"access_token":"a","refresh_token":"r","user_id":7}"""))

        val result = repository.login("test@gmail.com", "1234567")

        assertEquals(AuthResult.Success(AuthSession(7, AuthTokens("a", "r"))), result)
        assertEquals("/functions/v1/auth/login", server.takeRequest().url.encodedPath)
    }

    @Test
    fun `login maps 401 to InvalidCredentials`() = runTest {
        server.enqueue(jsonResponse(401, """{"code":"INVALID_CREDENTIALS","message":"wrong"}"""))

        assertEquals(AuthResult.Failure(AuthError.InvalidCredentials), repository.login("a@b.cc", "1234567"))
    }

    @Test
    fun `refresh maps token pair without user id`() = runTest {
        server.enqueue(jsonResponse(200, """{"access_token":"a2","refresh_token":"r2"}"""))

        assertEquals(AuthResult.Success(AuthTokens("a2", "r2")), repository.refresh("r1"))
    }

    @Test
    fun `check sends bearer header and treats is_valid false as success`() = runTest {
        server.enqueue(jsonResponse(200, """{"is_valid":false}"""))

        assertEquals(AuthResult.Success(false), repository.isTokenValid("expired"))
        assertEquals("Bearer expired", server.takeRequest().headers[AuthApi.AUTHORIZATION_HEADER])
    }

    @Test
    fun `recovery sends email as header`() = runTest {
        server.enqueue(jsonResponse(200, "{}"))

        assertEquals(AuthResult.Success(Unit), repository.recoverPassword("test@gmail.com"))
        assertEquals("test@gmail.com", server.takeRequest().headers[AuthApi.EMAIL_HEADER])
    }

    private fun jsonResponse(code: Int, body: String): MockResponse = MockResponse.Builder()
        .code(code)
        .addHeader("Content-Type", "application/json")
        .body(body)
        .build()
}
