package com.example.demo.security

import org.mindrot.jbcrypt.BCrypt


actual object PasswordHasher {
    actual fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    actual fun verify(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}