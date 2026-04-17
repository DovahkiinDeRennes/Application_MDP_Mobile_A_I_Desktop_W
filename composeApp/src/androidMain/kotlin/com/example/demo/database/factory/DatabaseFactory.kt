package com.example.demo.database.factory

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.demo.db.AppDatabase

object DatabaseFactory {

    private var db: AppDatabase? = null

    fun create(context: Context): AppDatabase {
        if (db == null) {
            val driver = AndroidSqliteDriver(
                AppDatabase.Schema,
                context,
                "demo.db"
            )
            db = AppDatabase.Companion(driver)
        }
        return db!!
    }
}