package com.example.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.demo.db.AppDatabase
import com.example.demo.security.CryptoManager
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Composable
fun RegisterScreen(
    navController: NavController,
    db: AppDatabase
) {

    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

            Text("Inscription")

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
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMessage.isNotEmpty()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    scope.launch {

                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Email et mot de passe requis"
                            return@launch
                        }

                        val existing = db.userQueries
                            .getUserByEmail(email)
                            .executeAsOneOrNull()

                        if (existing != null) {
                            errorMessage = "Un compte existe déjà"
                            return@launch
                        }

                        // 🔐 HASH PASSWORD
                        val hashedPassword = hashPassword(password)

                        // 🔑 GENERATE AES KEY (ANDROID OK)


                        db.userQueries.insertUser(
                            id = kotlin.random.Random.nextLong().toString(),
                            email = email,
                            password = hashedPassword,
                            aes_key = ""
                        )

                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
            ) {
                Text("S'inscrire")
            }

            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("Déjà un compte ?")
            }
        }
    }
}
fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256")
        .digest(password.toByteArray())

    return bytes.joinToString("") { "%02x".format(it) }
}