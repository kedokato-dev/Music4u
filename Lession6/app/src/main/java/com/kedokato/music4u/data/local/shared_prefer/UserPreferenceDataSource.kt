package com.kedokato.music4u.data.local.shared_prefer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userId")

class UserPreferenceDataSource(private val context: Context) {

    companion object {
        private val USERID_KEY = longPreferencesKey("userId")
    }

    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USERID_KEY] = userId
        }
    }

    suspend fun getUserId(): Long? {
        return context.dataStore.data.map { preferences ->
            preferences[USERID_KEY]
        }.first()
    }

    fun getUserIdFlow(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERID_KEY] as Long?
        }
    }
}