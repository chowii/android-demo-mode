package com.example.demomodetile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class DemoViewModel(
    private val demoModeInteractor: DemoModeInteractor
): ViewModel() {

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
        viewModelScope.launch {
            demoModeInteractor.setDemoModeEnabled(isEnabled)
        }
    }

    fun setNetworkIconVisibility(isVisible: Boolean) {
        if (!isDemoModeEnabled) return
        if (isVisible) {
            mutableActionsFlow.tryEmit(ShowNetworkIcon)
        } else {
            mutableActionsFlow.tryEmit(HideNetworkIcon)
        }
        demoModeInteractor.setNetworkIconVisibility(isVisible)
    }

    fun showClockDialog() {
        mutableActionsFlow.tryEmit(ShowClockDialog)
    }

    fun setClock(time: String) {
        if (!isDemoModeEnabled) return
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(SetClock(time))
            demoModeInteractor.setClock(time)
        }
    }

    fun setToggleNotification(isVisible: Boolean) {
        if (!isDemoModeEnabled) return
        mutableActionsFlow.tryEmit(ToggleNotification(isVisible))
        demoModeInteractor.setNotificationIconVisibility(isVisible)
    }

}
