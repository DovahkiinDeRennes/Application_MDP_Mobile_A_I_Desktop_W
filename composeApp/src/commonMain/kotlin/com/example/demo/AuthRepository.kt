package com.example.demo

import com.example.demo.db.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserModel>
    suspend fun register(email: String, password: String): Result<UserModel>
}

