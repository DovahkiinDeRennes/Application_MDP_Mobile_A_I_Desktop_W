package com.example.demo.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


actual object CryptoManager {

    private const val ALIAS = "password_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    private fun getKey(): SecretKey {

        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val existingKey = keyStore.getKey(ALIAS, null)
        if (existingKey != null) {
            return existingKey as SecretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val spec = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        keyGenerator.generateKey()

        return keyStore.getKey(ALIAS, null) as SecretKey
    }

    actual fun encrypt(data: String): String {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val iv = cipher.iv
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val combined = iv + encrypted

        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    actual fun decrypt(data: String): String {

        val bytes = Base64.decode(data, Base64.NO_WRAP)

        val iv = bytes.copyOfRange(0, 12)
        val encrypted = bytes.copyOfRange(12, bytes.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getKey(),
            GCMParameterSpec(128, iv)
        )

        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }

    actual fun setKey(encodedKey: String) {
    }

    actual fun generate(): String {
        TODO("Not yet implemented")
    }
}