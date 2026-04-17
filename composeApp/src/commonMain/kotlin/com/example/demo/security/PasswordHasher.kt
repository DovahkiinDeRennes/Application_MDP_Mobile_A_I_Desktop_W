package com.example.demo.security

expect object PasswordHasher {
    fun hash(password: String): String
    fun verify(password: String, hashed: String): Boolean
}