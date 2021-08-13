package com.example.demomodetile

import androidx.lifecycle.ViewModel
import com.example.demomodetile.DemoContract.Actions
import com.example.demomodetile.DemoContract.Actions.DisableDemoMode
import com.example.demomodetile.DemoContract.Actions.EnableDemoMode
import com.example.demomodetile.DemoContract.Actions.HideNetworkIcon
import com.example.demomodetile.DemoContract.Actions.SetClock
import com.example.demomodetile.DemoContract.Actions.ShowClockDialog
import com.example.demomodetile.DemoContract.Actions.ShowNetworkIcon
import com.example.demomodetile.DemoContract.Actions.ToggleNotification
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DemoViewModel: ViewModel() {

    private var isDemoModeEnabled = false

    private val mutableActionsFlow = MutableSharedFlow<Actions>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions: Flow<Actions> = mutableActionsFlow

    fun setDemoMode(isEnabled: Boolean) {
        isDemoModeEnabled = isEnabled
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(EnableDemoMode)
        } else {
            mutableActionsFlow.tryEmit(DisableDemoMode)
        }
    }

    fun setNetworkIconVisibility(isVisible: Boolean) {
        if (isVisible) {
            mutableActionsFlow.tryEmit(ShowNetworkIcon)
        } else {
            mutableActionsFlow.tryEmit(HideNetworkIcon)
        }
    }

    fun showClockDialog() {
        mutableActionsFlow.tryEmit(ShowClockDialog)
    }

    fun setClock(time: String) {
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(SetClock(time))
        }
    }

    fun setToggleNotification(isVisible: Boolean) {
        mutableActionsFlow.tryEmit(ToggleNotification(isVisible))
    }

}
