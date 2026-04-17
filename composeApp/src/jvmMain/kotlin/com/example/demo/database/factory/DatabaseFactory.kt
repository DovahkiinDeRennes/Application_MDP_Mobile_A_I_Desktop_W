package com.example.demo.database.factory

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.demo.db.AppDatabase

object DatabaseFactory {

    fun create(): AppDatabase {
        val driver = JdbcSqliteDriver("jdbc:sqlite:demo.db")

        try {
            AppDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // table existe déjà → on ignore
        }

        return AppDatabase.Companion(driver)
    }
}