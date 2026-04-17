package com.example.demo.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.demo.*
import kotlinx.coroutines.flow.first
import com.example.demo.db.AppDatabase
import com.example.demo.content.screen.MainScreen

@Composable
fun AppNavigation(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {

        composable("start") {
            CheckLogin(navController)
        }

        composable("login") {
            LoginScreen(navController, db)
        }

        composable("register") {
            RegisterScreen(navController, db)
        }

        composable("main/{userId}") { backStackEntry ->

            val userId = backStackEntry.arguments
                ?.getString("userId")
                ?: ""

            MainScreen(db, userId, navController)
        }
    }
}
@Composable
fun CheckLogin(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        val prefs = context.dataStore.data.first()
        val userId = prefs[UserPreferences.USER_ID]
        val loginTime = prefs[UserPreferences.LOGIN_TIME] ?: 0L

        val expired = System.currentTimeMillis() - loginTime > 30 * 60 * 1000

        if (userId != null && !expired) {
            navController.navigate("main/$userId") {
                popUpTo("start") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("start") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Chargement...")
    }
}