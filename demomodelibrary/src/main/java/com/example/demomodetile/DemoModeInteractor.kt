package com.example.demomodetile

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.first


class DemoModeInteractor(
    private val demoPreferences: DemoPreferences = DemoPreferences(),
) {

    private val commandList = mutableListOf<Intent>()
    private var hasSentCommands = false

    suspend fun isDemoModeEnabled(context: Context): Boolean {
        return demoPreferences.isDemoModeEnabled(context).first().also {
            if (requireNotNull(it)) commandList.add(DemoMode.enterDemoIntent)
        } ?: false
    }

    fun setDemoModeEnabled(context: Context, isEnabled: Boolean) {
        demoPreferences.setDemoMode(context, isEnabled)
    }

    suspend fun syncAndSend(context: Context) {
        isNotificationsEnabled(context)
        isNetworkEnabled(context)
        getClock(context)
        sendCommand(context)
    }

    suspend fun isNotificationsEnabled(context: Context): Boolean =
        demoPreferences.isNotificationVisible(context).first()
            .also {
                if (requireNotNull(it)) commandList.add(DemoMode.toggleNotificationVisibility(it))
            } ?: false

    suspend fun isNetworkEnabled(context: Context): Boolean =
        demoPreferences.isNetworkDemoEnabled(context).first()
            .also {
                if (requireNotNull(it)) commandList.add(DemoMode.showNetworkIcon)
                else commandList.add(DemoMode.hideNetworkIcon)
            } ?: false

    suspend fun getClock(context: Context): String? =
        demoPreferences.getClockTime(context).first()
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
}

