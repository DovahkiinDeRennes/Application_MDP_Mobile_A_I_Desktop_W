package com.example.demo.security

actual object PasswordHasher {

    actual fun hash(password: String): String {
        return password
    }

    actual fun verify(password: String, hashed: String): Boolean {
        return password == hashed
    }
}