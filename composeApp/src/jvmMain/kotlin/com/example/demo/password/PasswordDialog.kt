package com.example.demo.password

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation

import com.example.demo.colors.All.FondDialogPassword
import com.example.demo.colors.ButtonColors.FondButtonWhite
import com.example.demo.colors.ButtonColors.TexteButton
import com.example.demo.colors.ButtonColors.TexteButtonBlack
import com.example.demo.colors.InputColors.cursorColor
import com.example.demo.colors.InputColors.focusedBorderColor
import com.example.demo.colors.InputColors.focusedLabelColor
import com.example.demo.colors.InputColors.focusedTextColor
import com.example.demo.colors.InputColors.unfocusedBorderColor
import com.example.demo.colors.InputColors.unfocusedLabelColor
import com.example.demo.colors.InputColors.unfocusedTextColor

@Composable
fun AddPasswordDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String) -> Unit,

    initialSite: String = "",
    initialUrl: String = "",
    initialIdentifier: String = "",
    initialPassword: String = "",
    initialNote: String = ""
) {

    var site by remember { mutableStateOf(initialSite) }
    var url by remember { mutableStateOf(initialUrl) }
    var identifier by remember { mutableStateOf(initialIdentifier) }
    var password by remember { mutableStateOf(initialPassword) }
    var note by remember { mutableStateOf(initialNote) }

    val isEditMode = initialSite.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor = FondDialogPassword,

        title = {
            Text(if (isEditMode) "Modifier un mot de passe" else "Ajouter un mot de passe")
        },

        text = {
            Column {

                // 🔹 SITE
                OutlinedTextField(
                    value = site,
                    onValueChange = { site = it },
                    label = { Text("Site") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = focusedTextColor,
                        unfocusedTextColor = unfocusedTextColor,
                        focusedLabelColor = focusedLabelColor,
                        unfocusedLabelColor = unfocusedLabelColor,
                        cursorColor = cursorColor,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor
                    )
                )

                // 🔹 URL
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = focusedTextColor,
                        unfocusedTextColor = unfocusedTextColor,
                        focusedLabelColor = focusedLabelColor,
                        unfocusedLabelColor = unfocusedLabelColor,
                        cursorColor = cursorColor,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor
                    )
                )

                // 🔹 IDENTIFIANT
                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text("Identifiant") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = focusedTextColor,
                        unfocusedTextColor = unfocusedTextColor,
                        focusedLabelColor = focusedLabelColor,
                        unfocusedLabelColor = unfocusedLabelColor,
                        cursorColor = cursorColor,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor
                    )
                )

                // 🔐 PASSWORD
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = focusedTextColor,
                        unfocusedTextColor = unfocusedTextColor,
                        focusedLabelColor = focusedLabelColor,
                        unfocusedLabelColor = unfocusedLabelColor,
                        cursorColor = cursorColor,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor
                    )
                )

                // 📝 NOTE
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = focusedTextColor,
                        unfocusedTextColor = unfocusedTextColor,
                        focusedLabelColor = focusedLabelColor,
                        unfocusedLabelColor = unfocusedLabelColor,
                        cursorColor = cursorColor,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor
                    )
                )
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    if (site.isNotBlank() && password.isNotBlank()) {
                        onAdd(site, url, identifier, password, note)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FondButtonWhite,
                    contentColor = TexteButtonBlack
                )
            ) {
                Text(if (isEditMode) "Modifier" else "Ajouter")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = TexteButton)
            }
        }
    )
}