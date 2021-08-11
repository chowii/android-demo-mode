package com.example.playgroundAug

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DemoViewModel: ViewModel() {

    private var isDemoModeEnabled = false

    private val mutableActionsFlow = MutableSharedFlow<DemoContract.Actions>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions: Flow<DemoContract.Actions> = mutableActionsFlow

    fun setDemoMode(isEnabled: Boolean) {
        isDemoModeEnabled = isEnabled
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(DemoContract.Actions.EnableDemoMode)
        } else {
            mutableActionsFlow.tryEmit(DemoContract.Actions.DisableDemoMode)
        }
    }

    fun setNetworkIconVisibility(isVisible: Boolean) {
        if (isVisible) {
            mutableActionsFlow.tryEmit(DemoContract.Actions.ShowNetworkIcon)
        } else {
            mutableActionsFlow.tryEmit(DemoContract.Actions.HideNetworkIcon)
        }
    }

    fun showClockDialog() {
        mutableActionsFlow.tryEmit(DemoContract.Actions.ShowClockDialog)
    }

    fun setClock(time: String) {
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(DemoContract.Actions.SetClock(time))
        }
    }

    fun setToggleNotification(isVisible: Boolean) {
        mutableActionsFlow.tryEmit(DemoContract.Actions.ToggleNotification(isVisible))
    }

}
