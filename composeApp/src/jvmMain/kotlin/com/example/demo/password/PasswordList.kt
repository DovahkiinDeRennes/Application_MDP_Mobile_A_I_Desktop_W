package com.example.demo.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.demo.db.Password
import com.example.demo.security.CryptoManager


@Composable
fun PasswordList(
    passwords: List<Password>,
    onDelete: (Password) -> Unit,
    onEdit: (Password) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = passwords,
            key = { it.id }
        ) { password ->

            val decryptedPassword = CryptoManager.decrypt(password.password)

            val decryptedItem = password.copy(
                password = decryptedPassword
            )

            PasswordItem(
                password = decryptedItem,
                onDelete = { onDelete(password) },
                onEdit = { onEdit(password) }
            )
        }
    }
}