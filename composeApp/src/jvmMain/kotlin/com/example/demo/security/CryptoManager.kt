package com.example.demo.security

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

actual object CryptoManager {

    private lateinit var key: SecretKeySpec

    actual fun setKey(encodedKey: String) {
        val decoded = Base64.getDecoder().decode(encodedKey)
        key = SecretKeySpec(decoded, "AES")
    }

    actual fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        val encrypted = cipher.doFinal(data.toByteArray())

        return Base64.getEncoder().encodeToString(iv + encrypted)
    }

    actual fun decrypt(data: String): String {
        val bytes = Base64.getDecoder().decode(data)

        val iv = bytes.copyOfRange(0, 12)
        val encrypted = bytes.copyOfRange(12, bytes.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))

        return String(cipher.doFinal(encrypted))
    }

    actual fun generate(): String {
        val key = KeyGenerator.getInstance("AES")
            .apply { init(256) }
            .generateKey()

        return Base64.getEncoder().encodeToString(key.encoded)
    }
}