package com.nooro.weather.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class NooroPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val PREFERRED_LOCATION = intPreferencesKey("preferred_location")
    }

    private val preferencesFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            }
            else throw exception
        }

    val preferredLocationFlow: Flow<Int?> = preferencesFlow.map { it[PreferencesKeys.PREFERRED_LOCATION] }

    suspend fun updatePreferredLocation(locationId: Int) = withContext(Dispatchers.IO) {
        dataStore.edit { it[PreferencesKeys.PREFERRED_LOCATION] = locationId }
    }

}