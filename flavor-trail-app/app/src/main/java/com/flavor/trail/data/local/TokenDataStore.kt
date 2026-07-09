package com.flavor.trail.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.flavor.trail.util.TokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

class TokenDataStore(private val context: Context) : TokenProvider {

    private val TOKEN_KEY = stringPreferencesKey("jwt_token")

    override suspend fun getToken(): String {
        return context.tokenDataStore.data
            .map { it[TOKEN_KEY] ?: "" }
            .first()
    }

    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        context.tokenDataStore.edit {
            it.remove(TOKEN_KEY)
        }
    }
}