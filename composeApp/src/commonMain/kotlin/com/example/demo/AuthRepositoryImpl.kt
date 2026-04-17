package com.example.demo

import com.example.demo.db.AppDatabase
import com.example.demo.security.CryptoManager
import kotlin.random.Random
import com.example.demo.security.PasswordHasher


class AuthRepositoryImpl(
    private val db: AppDatabase
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserModel> {

        val user = db.userQueries.getUserByEmail(email).executeAsOneOrNull()

        return if (user != null && PasswordHasher.verify(password, user.password)) {

            CryptoManager.setKey(user.aes_key)

            Result.success(UserModel(user.id, user.email))

        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun register(email: String, password: String): Result<UserModel> {

        val existing = db.userQueries.getUserByEmail(email).executeAsOneOrNull()
        if (existing != null) {
            return Result.failure(Exception("User already exists"))
        }

        val id = Random.nextLong().toString()
        val hashedPassword = PasswordHasher.hash(password)

        val aesKey = CryptoManager.generate() // 👈 IMPORTANT

        db.userQueries.insertUser(
            id = id,
            email = email,
            password = hashedPassword,
            aes_key = aesKey
        )

        return Result.success(UserModel(id, email))
    }
}

data class UserModel(
    val id: String,
    val email: String
)

