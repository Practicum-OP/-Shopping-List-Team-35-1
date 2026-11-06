package com.example.shoppinglist.core.data.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val KEY_ALIAS = "shopping_list_auth_session"
private const val TRANSFORMATION = "AES/GCM/NoPadding"
private const val AUTH_TAG_LENGTH_BITS = 128

class SessionCipher {

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateKey(),
        )

        val encrypted = cipher.doFinal(
            value.toByteArray(Charsets.UTF_8),
        )

        val iv = Base64.encodeToString(
            cipher.iv,
            Base64.NO_WRAP,
        )

        val payload = Base64.encodeToString(
            encrypted,
            Base64.NO_WRAP,
        )

        return "$iv:$payload"
    }

    fun decrypt(value: String): String {
        val parts = value.split(':', limit = 2)

        require(parts.size == 2) {
            "Invalid encrypted session format"
        }

        val iv = Base64.decode(
            parts[0],
            Base64.NO_WRAP,
        )

        val encrypted = Base64.decode(
            parts[1],
            Base64.NO_WRAP,
        )

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateKey(),
            GCMParameterSpec(
                AUTH_TAG_LENGTH_BITS,
                iv,
            ),
        )

        return String(
            cipher.doFinal(encrypted),
            Charsets.UTF_8,
        )
    }

    @Synchronized
    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }

        val existingKey = keyStore.getKey(
            KEY_ALIAS,
            null,
        ) as? SecretKey

        if (existingKey != null) {
            return existingKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE,
        )

        val specification = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(
                KeyProperties.ENCRYPTION_PADDING_NONE,
            )
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(specification)

        return keyGenerator.generateKey()
    }
}
