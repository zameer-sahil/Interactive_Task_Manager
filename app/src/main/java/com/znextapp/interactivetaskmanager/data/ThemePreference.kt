package com.znextapp.interactivetaskmanager.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
private val Context.dataStore by preferencesDataStore(name = "settings")

val Context.dataStoreInstance
    get() = dataStore
@Singleton
class ThemePreference @Inject constructor(@ApplicationContext private val context: Context) {

    private val PRIMARY_COLOR_KEY = stringPreferencesKey("primary_color")

    fun getPrimaryColor(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[PRIMARY_COLOR_KEY] ?: "#6200EE"
        }
    }

    suspend fun savePrimaryColor(colorHex: String) {
        context.dataStore.edit { prefs ->
            prefs[PRIMARY_COLOR_KEY] = colorHex
        }
    }
}
