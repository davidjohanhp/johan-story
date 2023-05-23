package com.example.proyekstory.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val UID_KEY = stringPreferencesKey("uid")
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")

    fun getUserToken(): Flow<String> = dataStore.data.map { it[TOKEN_KEY] ?: "Not Set" }
    fun getUID(): Flow<String> = dataStore.data.map { it[UID_KEY] ?: "Not Set" }
    fun getName(): Flow<String> = dataStore.data.map { it[NAME_KEY] ?: "Not Set" }
    fun getEmail(): Flow<String> = dataStore.data.map { it[EMAIL_KEY] ?: "Not Set" }

    suspend fun saveLoginSession(userToken: String, userUID: String, userName:String, userEmail: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = userToken
            preferences[UID_KEY] = userUID
            preferences[NAME_KEY] = userName
            preferences[EMAIL_KEY] = userEmail
        }
    }

    suspend fun deleteLoginSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}