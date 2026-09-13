package com.viraj.spendwise.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val IS_BIOMETRIC_ENABLED = booleanPreferencesKey("is_biometric_enabled")
        val USER_PIN = androidx.datastore.preferences.core.stringPreferencesKey("user_pin")
    }

    val isDarkTheme: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME]
    }

    val isBiometricEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_BIOMETRIC_ENABLED] ?: false
    }

    val userPin: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_PIN]
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = enabled
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_BIOMETRIC_ENABLED] = enabled
        }
    }

    suspend fun setUserPin(pin: String?) {
        dataStore.edit { preferences ->
            if (pin == null) {
                preferences.remove(USER_PIN)
            } else {
                preferences[USER_PIN] = pin
            }
        }
    }
}
