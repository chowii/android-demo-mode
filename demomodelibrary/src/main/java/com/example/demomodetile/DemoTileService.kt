package com.example.demomodetile

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DemoTileService : TileService() {

    @Inject
    lateinit var demoModeInteractor: DemoModeInteractor

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onTileAdded() {
        super.onTileAdded()
        scope.launch {
            withContext(Dispatchers.IO) {
               val isActive = demoModeInteractor.isDemoModeEnabled()
                if (isActive) {
                    qsTile.state = Tile.STATE_ACTIVE
                } else {
                    qsTile.state = Tile.STATE_INACTIVE
                }
            }
            qsTile.updateTile()
        }

    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d("LOG_TAG---", "DemoTileService#onStartListening-22: ")
        val context = this@DemoTileService
        qsTile.icon = Icon.createWithResource(context, R.drawable.ic_demo_mode)
        scope.launch {
            withContext(Dispatchers.IO) {
                val isActive = demoModeInteractor.isDemoModeEnabled()
                val tileState = if (isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                setQsTile(tileState, isActive)
            }
            qsTile.updateTile()
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d("LOG_TAG---", "DemoTileService#onStopListening-57: ")
    }

    override fun onClick() {
        super.onClick()
        scope.launch {
            withContext(Dispatchers.IO) {
                val isActive = demoModeInteractor.isDemoModeEnabled()
                val tileState = if (!isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                setQsTile(tileState, !isActive)
            }
            qsTile.updateTile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private suspend fun setQsTile(state: Int, isEnabled: Boolean) {
        qsTile.state = state
        demoModeInteractor.setDemoModeEnabled(isEnabled)
        demoModeInteractor.syncAndSend(this)
    }
}
