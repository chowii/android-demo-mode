package com.example.demomodetile

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DemoPreferences {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "demo_prefs")
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun isDemoModeEnabled(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map {
            it[DEMO_MODE_KEY].also { bool ->
                Log.d("LOG_TAG---", "DemoPreferences#isEnabled-26: $bool")
            }
        }
    }


    fun setDemoMode(context: Context, isEnabled: Boolean) {
        scope.launch {
            context.dataStore.edit { prefs ->
                prefs[DEMO_MODE_KEY] = isEnabled
            }
        }
    }

    fun isNetworkDemoEnabled(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map {
            it[NETWORK_DEMO_KEY]
        }
    }

    fun setNetworkIconVisibility(context: Context, isVisible: Boolean) {
        scope.launch {
            context.dataStore.edit {
                it[NETWORK_DEMO_KEY] = isVisible
            }
        }
    }

    fun isNotificationVisible(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map {
            it[NOTIFICATION_DEMO_KEY]
        }
    }

    fun setNotificationIconVisibility(context: Context, isVisible: Boolean) {
        scope.launch {
            context.dataStore.edit {
                it[NOTIFICATION_DEMO_KEY] = isVisible
            }
        }
    }

    fun getClockTime(context: Context): Flow<String?> {
        return context.dataStore.data.map {
            it[CLOCK_TIME_DEMO_KEY]
        }
    }

    fun setClock(context: Context, clockTime: String) {
        scope.launch {
            context.dataStore.edit {
                it[CLOCK_TIME_DEMO_KEY] = clockTime
            }
        }
    }

    fun getScope(): CoroutineScope = scope
    companion object {
        private val DEMO_MODE_KEY = booleanPreferencesKey("demo_key")
        private val NETWORK_DEMO_KEY = booleanPreferencesKey("network_demo_key")
        private val NOTIFICATION_DEMO_KEY = booleanPreferencesKey("notification_demo_key")
        private val CLOCK_TIME_DEMO_KEY = stringPreferencesKey("clock_demo_key")
    }
}