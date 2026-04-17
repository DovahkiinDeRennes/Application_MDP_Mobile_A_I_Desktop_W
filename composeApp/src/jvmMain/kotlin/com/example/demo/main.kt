package com.example.demo


import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.demo.database.factory.DatabaseFactory


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "demo") {

        val db = DatabaseFactory.create()
        val repo = AuthRepositoryImpl(db)

        App(repo,db)
    }
}