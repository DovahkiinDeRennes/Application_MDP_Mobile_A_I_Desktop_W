package com.example.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment

import com.example.demo.ui.components.AppButton

@Composable
fun RegisterScreen(
    repo: AuthRepositoryImpl,
    onGoLogin: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Register")

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                text = "S'inscrire",
                onClick = {
                    scope.launch {
                        val result = repo.register(email, password)

                        result.onSuccess {
                            onGoLogin()
                        }.onFailure {
                            error = "Utilisateur déjà existant"
                        }
                    }
                }
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGoLogin
            ) {
                Text("Déjà un compte ?")
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}