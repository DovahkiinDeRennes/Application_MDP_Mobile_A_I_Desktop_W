package com.example.demo.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.demo.db.AppDatabase
import com.example.demo.db.Password
import com.example.demo.password.AddPasswordDialog
import com.example.demo.password.PasswordList
import com.example.demo.security.CryptoManager
import com.example.demo.topbar.TopBar


import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    db: AppDatabase,
    currentUserId: String,
    onLogout: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var passwords by remember { mutableStateOf(listOf<Password>()) }
    var showDialog by remember { mutableStateOf(false) }

    var selectedPassword by remember { mutableStateOf<Password?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    // 🔄 LOAD DATA
    fun loadPasswords() {
        passwords = db.passwordQueries
            .getPasswordsForUser(currentUserId)
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
                onLogout = onLogout
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->

        PasswordList(
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
        if (showDialog) {
            AddPasswordDialog(
                onDismiss = { showDialog = false },
                onAdd = { site, url, identifier, pass, note ->
                    val encrypted = CryptoManager.encrypt(pass)
                    scope.launch {
                        db.passwordQueries.insertPassword(
                            url = url,
                            identifier = identifier,
                            password = encrypted, // 🔐 CryptoManager ici si tu veux
                            site = site,
                            note = note,
                            user_id = currentUserId
                        )

                        loadPasswords()
                        showDialog = false
                    }
                }
            )
        }

        // ✏️ EDIT PASSWORD
        if (showEditDialog && selectedPassword != null) {
            AddPasswordDialog(
                initialSite = selectedPassword!!.site,
                initialUrl = selectedPassword!!.url,
                initialIdentifier = selectedPassword!!.identifier,
                initialPassword = selectedPassword!!.password,
                initialNote = selectedPassword!!.note,

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
                            id = selectedPassword!!.id
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