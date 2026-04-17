package com.example.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.demo.db.AppDatabase
import com.example.demo.screen.MainScreen

@Composable
fun App(
    repo: AuthRepositoryImpl,
    db: AppDatabase
) {
    var screen by remember { mutableStateOf<Screen>(Screen.Login) }

    when (val current = screen) {

        is Screen.Login -> LoginScreen(
            repo = repo,
            onGoRegister = {
                screen = Screen.Register
            },
            onLoginSuccess = { userId ->
                screen = Screen.Home(userId)
            }
        )

        is Screen.Register -> RegisterScreen(
            repo = repo,
            onGoLogin = {
                screen = Screen.Login
            }
        )

        is Screen.Home -> MainScreen(
            db = db,
            currentUserId = current.userId,
            onLogout = {
                screen = Screen.Login
            }
        )
    }
}

sealed class Screen {

    object Login : Screen()

    object Register : Screen()

    data class Home(val userId: String) : Screen()
}