package com.example.demo.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.db.Password
import kotlinx.coroutines.launch

@Composable
fun PasswordItem(
    password: Password,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    val decryptedPassword = remember(password.password) {
        password.password // 🔐 garde CryptoManager si tu veux
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {

        // 🔹 HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = password.site,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Row {

                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier")
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 🔐 IDENTIFIANT
        Text(
            text = password.identifier,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔐 PASSWORD ROW
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (isVisible) decryptedPassword else "••••••••",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            // 👁️ SHOW/HIDE
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector = if (isVisible)
                        Icons.Default.VisibilityOff
                    else
                        Icons.Default.Visibility,
                    contentDescription = "Afficher"
                )
            }

            // 📋 COPY (JVM VERSION)
            IconButton(onClick = {
                scope.launch {
                    clipboardManager.setText(
                        AnnotatedString(decryptedPassword)
                    )
                }
            }) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copier")
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 🌐 URL
        if (password.url.isNotBlank()) {
            Text(text = password.url, fontSize = 12.sp)
        }

        // 📝 NOTE
        if (password.note.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = password.note, fontSize = 12.sp)
        }

        // 🗑️ DELETE DIALOG
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Supprimer ce mot de passe ?") },
                confirmButton = {
                    Button(onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}