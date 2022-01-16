package com.example.demomodetile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demomodetile.DemoContract.Actions
import com.example.demomodetile.DemoContract.Actions.DisableDemoMode
import com.example.demomodetile.DemoContract.Actions.EnableDemoMode
import com.example.demomodetile.DemoContract.Actions.HideNetworkIcon
import com.example.demomodetile.DemoContract.Actions.SetClock
import com.example.demomodetile.DemoContract.Actions.ShowClockDialog
import com.example.demomodetile.DemoContract.Actions.ShowNetworkIcon
import com.example.demomodetile.DemoContract.Actions.ToggleNotification
import com.example.demomodetile.DemoContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    application: Application,
    private val demoModeInteractor: DemoModeInteractor
): AndroidViewModel(application) {

    init {
        initDemoState()
    }

    private var isDemoModeEnabled = false

    private val mutableActionsFlow = MutableSharedFlow<Actions>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions: Flow<Actions> = mutableActionsFlow

    private val mutableViewState = MutableLiveData<ViewState>()
    val viewState = mutableViewState

    fun setDemoMode(isEnabled: Boolean) {
        isDemoModeEnabled = isEnabled
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(EnableDemoMode)
        } else {
            mutableActionsFlow.tryEmit(DisableDemoMode)
        }
        demoModeInteractor.setDemoModeEnabled(isEnabled)
        mutableViewState.emitNewState(isEnabled = isEnabled)
    }

    fun setNetworkIconVisibility(isHidden: Boolean) {
        if (!isDemoModeEnabled) return
        if (isHidden) {
            mutableActionsFlow.tryEmit(ShowNetworkIcon)
        } else {
            mutableActionsFlow.tryEmit(HideNetworkIcon)
        }
        demoModeInteractor.setNetworkIconVisibility(isHidden)
        mutableViewState.emitNewState(isNetworkHidden = isHidden)
    }

    fun showClockDialog() {
        if (!isDemoModeEnabled) return
        mutableActionsFlow.tryEmit(ShowClockDialog)
    }

    fun setClock(time: String) {
        if (!isDemoModeEnabled) return
        if (isDemoModeEnabled) {
            mutableActionsFlow.tryEmit(SetClock(time))
            demoModeInteractor.setClock(time)
            mutableViewState.emitNewState(clock = time)
        }
    }

    fun setToggleNotification(isHidden: Boolean) {
        if (!isDemoModeEnabled) return
        mutableActionsFlow.tryEmit(ToggleNotification(isHidden))
        demoModeInteractor.setNotificationIconVisibility(isHidden)
        mutableViewState.emitNewState(isNotificationHidden = isHidden)
    }

    internal fun initDemoState() {
        viewModelScope.launch {
            isDemoModeEnabled = demoModeInteractor.isDemoModeEnabled()
            val clockTime = demoModeInteractor.getClock()?.also { setClock(it) }
            val isNetworkHidden = demoModeInteractor.isNetworkHidden()
            val isNotificationHidden = demoModeInteractor.isNotificationsHidden()
            mutableViewState.emitNewState(
                isDemoModeEnabled,
                clockTime.orEmpty(),
                isNetworkHidden = isNetworkHidden,
                isNotificationHidden = isNotificationHidden
            )
            withContext(Dispatchers.Main) {
                demoModeInteractor.sendCommand(getApplication<Application>())
            }
        }
    }

    private fun MutableLiveData<ViewState>.emitNewState(
        isEnabled: Boolean = mutableViewState.value?.isEnabled ?: false,
        clock: String = mutableViewState.value?.clock.orEmpty(),
        isNetworkHidden: Boolean = mutableViewState.value?.isNetworkHidden ?: false,
        isNotificationHidden: Boolean = mutableViewState.value?.isNotificationHidden ?: false,
    ) {
        value = ViewState(
            isEnabled,
            clock,
            isNetworkHidden,
            isNotificationHidden,
        )
    }
}
