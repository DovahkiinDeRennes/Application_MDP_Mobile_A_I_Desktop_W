package com.example.demo.content.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.demo.db.AppDatabase
import com.example.demo.db.Password
import com.example.demo.password.AddPasswordDialog
import com.example.demo.password.PasswordList
import com.example.demo.security.CryptoManager
import kotlinx.coroutines.launch
import com.example.demo.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    db: AppDatabase,
    currentUserId: String,
    navController: NavController,
) {
    val scope = rememberCoroutineScope()

    var passwords by remember { mutableStateOf<List<Password>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    var selectedPassword by remember { mutableStateOf<Password?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    // 🔄 Load data
    fun loadPasswords() {
        passwords = db.passwordQueries
            .getPasswordsForUser(currentUserId.toString())
            .executeAsList()
    }

    LaunchedEffect(currentUserId) {
        loadPasswords()
    }

    Scaffold(
        topBar = {
            TopBar(
                onProfile = {},
                onSettings = {},
                navController = navController,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->

        PasswordList(
            modifier = Modifier.padding(padding),
            passwords = passwords,
            onDelete = { password ->
                scope.launch {
                    db.passwordQueries.deletePassword(password.id)
                    loadPasswords()
                }
            },
            onEdit = { password ->
                selectedPassword = password
                showEditDialog = true
            }
        )

        // ➕ ADD PASSWORD
        if (showAddDialog) {
            AddPasswordDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { site, url, identifier, pass, note ->
                    val encrypted = CryptoManager.encrypt(pass)

                    scope.launch {
                        db.passwordQueries.insertPassword(
                            url = url,
                            identifier = identifier,
                            password = encrypted,
                            site = site,
                            note = note,
                            user_id = currentUserId.toString()
                        )

                        loadPasswords()
                        showAddDialog = false
                    }
                }
            )
        }

        // ✏️ EDIT PASSWORD
        val editingPassword = selectedPassword

        if (showEditDialog && editingPassword != null) {
            AddPasswordDialog(
                initialSite = editingPassword.site,
                initialUrl = editingPassword.url,
                initialIdentifier = editingPassword.identifier,
                initialPassword = editingPassword.password,
                initialNote = editingPassword.note,
                onDismiss = {
                    showEditDialog = false
                    selectedPassword = null
                },
                onAdd = { site, url, identifier, pass, note ->
                    val encrypted = CryptoManager.encrypt(pass)

                    scope.launch {
                        db.passwordQueries.updatePassword(
                            url = url,
                            identifier = identifier,
                            password = encrypted,
                            site = site,
                            note = note,
                            id = editingPassword.id
                        )

                        loadPasswords()
                        showEditDialog = false
                        selectedPassword = null
                    }
                }
            )
        }
    }
}