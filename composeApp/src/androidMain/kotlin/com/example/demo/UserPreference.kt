package com.example.demo

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    val LOGIN_TIME = longPreferencesKey("login_time")
    val USER_ID = stringPreferencesKey("user_id")
}