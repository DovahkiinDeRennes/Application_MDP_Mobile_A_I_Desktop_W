package com.example.demo.security

actual object CryptoManager {

    actual fun encrypt(data: String): String = data

    actual fun decrypt(data: String): String = data
}