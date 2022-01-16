package com.example.demomodetile

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DemoModeInteractor @Inject constructor(
    private val demoPreferences: DemoPreferences,
) {

    private val commandList = mutableListOf<Intent>()
    private var hasSentCommands = false

    suspend fun isDemoModeEnabled(): Boolean {
        return demoPreferences.isDemoModeEnabled().first()?.also {
            if (it) commandList.add(DemoMode.enterDemoIntent)
            else commandList.add(DemoMode.exitDemoIntent)
        } ?: false
    }

    fun setDemoModeEnabled(isEnabled: Boolean) {
        demoPreferences.setDemoMode(isEnabled)
    }

    suspend fun syncAndSend(context: Context) {
        commandList.clear()
        hasSentCommands = false
        isDemoModeEnabled()
        isNotificationsHidden()
        isNetworkHidden()
        getClock()
        sendCommand(context)
    }

    suspend fun isNotificationsHidden(): Boolean =
        demoPreferences.isNotificationHidden().first()
            ?.also {
                commandList.add(DemoMode.toggleNotificationVisibility(it))
            } ?: false

    suspend fun isNetworkHidden(): Boolean =
        demoPreferences.isNetworkIconHidden().first()
            ?.also {
                if (it) commandList.add(DemoMode.showNetworkIcon)
                else commandList.add(DemoMode.hideNetworkIcon)
            } ?: false

    suspend fun getClock(): String? =
        demoPreferences.getClockTime().first()
            .also {
                it?.let { clock ->
                    commandList.add(DemoMode.clockIntent(clock))
                }
            }

    fun sendCommand(context: Context) {
        if (!hasSentCommands) {
            commandList.forEach {
                context.sendBroadcast(it)
            }
            hasSentCommands = true
        }
    }

    fun setNetworkIconVisibility(isVisible: Boolean) {
        demoPreferences.setNetworkIconVisibility(isVisible)
    }

    fun setNotificationIconVisibility(isVisible: Boolean) {
        demoPreferences.setNotificationIconVisibility(isVisible)
    }

    fun setClock(time: String) {
        demoPreferences.setClock(time)
    }
}

