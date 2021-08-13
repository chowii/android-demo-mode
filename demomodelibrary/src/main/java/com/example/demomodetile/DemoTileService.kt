package com.example.demomodetile

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DemoTileService : TileService() {

    private lateinit var demoPreferences: DemoModeInteractor
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        demoPreferences = DemoModeInteractor(DemoPreferences())
    }

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d("LOG_TAG---", "DemoTileService#onTileAdded-12: ")
        scope.launch {
            val isActive = demoPreferences.isDemoModeEnabled(this@DemoTileService)
            if (isActive) {
                qsTile.state = Tile.STATE_ACTIVE
            } else {
                qsTile.state = Tile.STATE_INACTIVE
            }
            qsTile.updateTile()
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        Log.d("LOG_TAG---", "DemoTileService#onTileRemoved-17: ")
    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d("LOG_TAG---", "DemoTileService#onStartListening-22: ")
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_demo_mode)
        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d("LOG_TAG---", "DemoTileService#onStopListening-57: ")
    }

    override fun onClick() {
        super.onClick()
        scope.launch {
            val context = this@DemoTileService
            val isActive = demoPreferences.isDemoModeEnabled(context)
            Log.d("LOG_TAG---", "DemoTileService#onClick-28: ${qsTile.state}")
            if (isActive) {
                setQsTile(Tile.STATE_INACTIVE, false)
            } else {
                setQsTile(Tile.STATE_ACTIVE, true)
            }
            demoPreferences.syncAndSend(context)
            qsTile.updateTile()
        }
    }

    private fun setQsTile(state: Int, isEnabled: Boolean) {
        qsTile.state = state
        demoPreferences.setDemoModeEnabled(this, isEnabled)
    }
}
