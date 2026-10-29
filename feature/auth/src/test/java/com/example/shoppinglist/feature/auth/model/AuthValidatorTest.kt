package com.example.shoppinglist.feature.auth.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `empty field has no error`() {
        assertNull(AuthValidator.validateEmail(""))
        assertNull(AuthValidator.validatePassword(""))
        assertNull(AuthValidator.validatePasswordRepeat(password = "1234567", passwordRepeat = ""))
    }

    @Test
    fun `email is checked for format`() {
        assertEquals(AuthFieldError.EmailInvalid, AuthValidator.validateEmail("test@"))
        assertNull(AuthValidator.validateEmail("test@gmail.com"))
    }

    @Test
    fun `password must be longer than 6 characters`() {
        assertEquals(AuthFieldError.PasswordTooShort, AuthValidator.validatePassword("123456"))
        assertNull(AuthValidator.validatePassword("1234567"))
    }

    @Test
    fun `password repeat must match password`() {
        assertEquals(
            AuthFieldError.PasswordsMismatch,
            AuthValidator.validatePasswordRepeat(password = "1234567", passwordRepeat = "1234568"),
        )
        assertNull(AuthValidator.validatePasswordRepeat(password = "1234567", passwordRepeat = "1234567"))
    }
}
