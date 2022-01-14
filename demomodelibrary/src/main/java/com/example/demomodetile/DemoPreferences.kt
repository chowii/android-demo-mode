package com.example.demomodetile

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DemoPreferences(private val dataStore: DataStore<Preferences>) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun isDemoModeEnabled(): Flow<Boolean?> {
        return dataStore.data.map {
            it[DEMO_MODE_KEY].also { bool ->
                Log.d("LOG_TAG---", "DemoPreferences#isEnabled-26: $bool")
            }
        }
    }


    suspend fun setDemoMode(isEnabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[DEMO_MODE_KEY] = isEnabled
        }
    }

    fun isNetworkDemoEnabled(): Flow<Boolean?> {
        return dataStore.data.map {
            it[NETWORK_DEMO_KEY]
        }
    }

    fun setNetworkIconVisibility(isVisible: Boolean) {
        scope.launch {
            dataStore.edit {
                it[NETWORK_DEMO_KEY] = isVisible
            }
        }
    }

    fun isNotificationVisible(): Flow<Boolean?> {
        return dataStore.data.map {
            it[NOTIFICATION_DEMO_KEY]
        }
    }

    fun setNotificationIconVisibility(isVisible: Boolean) {
        scope.launch {
            dataStore.edit {
                it[NOTIFICATION_DEMO_KEY] = isVisible
            }
        }
    }

    fun getClockTime(): Flow<String?> {
        return dataStore.data.map {
            it[CLOCK_TIME_DEMO_KEY]
        }
    }

    fun setClock(clockTime: String) {
        scope.launch {
            dataStore.edit {
                it[CLOCK_TIME_DEMO_KEY] = clockTime
            }
        }
    }

    companion object {
        private val DEMO_MODE_KEY = booleanPreferencesKey("demo_key")
        private val NETWORK_DEMO_KEY = booleanPreferencesKey("network_demo_key")
        private val NOTIFICATION_DEMO_KEY = booleanPreferencesKey("notification_demo_key")
        private val CLOCK_TIME_DEMO_KEY = stringPreferencesKey("clock_demo_key")
    }
}
