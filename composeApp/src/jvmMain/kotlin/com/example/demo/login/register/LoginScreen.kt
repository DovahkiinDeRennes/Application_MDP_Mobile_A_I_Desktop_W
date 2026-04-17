package com.example.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo.ui.components.AppButton

@Composable
fun LoginScreen(
    repo: AuthRepositoryImpl,
    onGoRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit
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
                .width(300.dp) // optionnel mais propre
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Connexion")

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
                text = "Se connecter",
                onClick = {
                    scope.launch {
                        val result = repo.login(email, password)

                        result.onSuccess {
                            onLoginSuccess(it.id)
                        }.onFailure {
                            error = "Login failed"
                        }
                    }
                }
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGoRegister
            ) {
                Text("Créer un compte")
            }

            if (error.isNotEmpty()) {
                Text(
                    error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}