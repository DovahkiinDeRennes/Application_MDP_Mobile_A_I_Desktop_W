package com.example.demo.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import com.example.demo.UserPreferences
import com.example.demo.dataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onProfile: () -> Unit,
    onSettings: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CenterAlignedTopAppBar(

            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),

            navigationIcon = {
                Box {

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Mon profil") },
                            onClick = {
                                expanded = false
                                onProfile()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Paramètres") },
                            onClick = {
                                expanded = false
                                onSettings()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Déconnexion") },
                            onClick = {
                                expanded = false

                                scope.launch {
                                    context.dataStore.edit { prefs ->
                                        prefs.remove(UserPreferences.USER_ID)
                                        prefs.remove(UserPreferences.LOGIN_TIME)
                                    }

                                    navController.navigate("login") {
                                        popUpTo("start") { inclusive = true }
                                    }
                                }
                            }
                        )
                    }
                }
            },

            title = {
                Text("Liste des mots de passe")
            }
        )
    }
}