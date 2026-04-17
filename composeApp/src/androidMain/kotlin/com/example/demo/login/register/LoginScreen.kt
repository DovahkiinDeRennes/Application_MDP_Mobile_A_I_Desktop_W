package com.example.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import com.example.demo.db.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    db: AppDatabase
) {


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Bienvenue - Connexion")

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                label = { Text("Email") },
                isError = errorMessage.isNotEmpty()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                label = { Text("Mot de passe") },
                isError = errorMessage.isNotEmpty()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                enabled = !isLoading,
                onClick = {
                    scope.launch {
                        try {

                            isLoading = true
                            errorMessage = ""

                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Email et mot de passe requis"
                                return@launch
                            }

                            val user = db.userQueries
                                .getUserByEmail(email)
                                .executeAsOneOrNull()

                            if (user == null) {
                                errorMessage = "Utilisateur introuvable"
                                return@launch
                            }

                            val hashedInput = hashPassword(password)

                            if (user.password != hashedInput) {
                                errorMessage = "Mot de passe incorrect"
                                return@launch
                            }

                            // ⚠️ SAFE DATASTORE (id = String)
                            context.dataStore.edit { prefs ->
                                prefs[UserPreferences.USER_ID] = user.id
                                prefs[UserPreferences.LOGIN_TIME] = System.currentTimeMillis()
                            }

                            navController.navigate("main/${user.id}") {
                                popUpTo("login") { inclusive = true }
                            }

                        } catch (e: Exception) {
                            errorMessage = "Erreur: ${e.message}"
                            e.printStackTrace()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) {
                if (isLoading) {
                    Text("Connexion...")
                } else {
                    Text("Se connecter")
                }
            }

            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text("Créer un compte")
            }
        }
    }

}
