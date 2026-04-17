package com.example.demo.security

expect object CryptoManager {
    fun encrypt(data: String): String
    fun decrypt(data: String): String

    fun setKey(encodedKey: String)


    fun generate(): String

}