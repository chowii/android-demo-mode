package com.example.demomodetile

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.example.demomodetile.di.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class DemoTileService : TileService() {

    @Inject
    lateinit var demoModeInteractor: DemoModeInteractor

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        inject(this)
    }

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d("LOG_TAG---", "DemoTileService#onTileAdded-12: ")
        scope.launch {
            val isActive = demoModeInteractor.isDemoModeEnabled(this@DemoTileService)
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
        val context = this@DemoTileService
        qsTile.icon = Icon.createWithResource(context, R.drawable.ic_demo_mode)
        scope.launch {
            val isActive = demoModeInteractor.isDemoModeEnabled(context)
            val tileState = if (isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            setQsTile(tileState, isActive)
            demoModeInteractor.syncAndSend(context)
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
            val context = this@DemoTileService
            val isActive = demoModeInteractor.isDemoModeEnabled(context)
            Log.d("LOG_TAG---", "DemoTileService#onClick-28: ${qsTile.state}")
            val tileState = if (!isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            setQsTile(tileState, !isActive)
            qsTile.updateTile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private suspend fun setQsTile(state: Int, isEnabled: Boolean) {
        qsTile.state = state
        demoModeInteractor.setDemoModeEnabled(isEnabled)
        demoModeInteractor.syncAndSend(this)
    }
}
