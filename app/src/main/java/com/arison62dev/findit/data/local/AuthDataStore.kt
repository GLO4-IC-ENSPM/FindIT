package com.arison62dev.findit.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_data_store")

@Singleton
class AuthDataStore @Inject constructor(@ApplicationContext context: Context) {
    private val isLoggedInkey = booleanPreferencesKey("is_logged_in")
    private val userIdKey = intPreferencesKey("userId")
    val authDataStore = context.authDataStore

    val isLoggedIn: Flow<Boolean> = authDataStore.data
        .map {
            it[isLoggedInkey] ?: false
        }
    val userId : Flow<Int?> = authDataStore.data.
        map{
            it[userIdKey] ?: it[userIdKey]
        }
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        authDataStore.edit { prefernces ->
            prefernces[isLoggedInkey] = isLoggedIn
        }
    }
    suspend fun setUserId(userId: Int){
        authDataStore.edit {
            preferences ->
            preferences[userIdKey] =  userId
        }
    }
    suspend fun clearAuthPreferences() {
        authDataStore.edit { prefernces ->
            prefernces.clear()
        }

    }
}