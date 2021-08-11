package com.example.playgroundAug

import android.content.Intent

object DemoMode {
    private const val ENTER_DEMO_COMMAND_STRING = "enter"
    private const val EXIT_DEMO_COMMAND_STRING = "exit"
    private const val NOTIFICATION_DEMO_COMMAND_STRING = "notifications"
    private const val TIME_EXTRA_KEY = "hhmm"

    private val demoIntent: Intent
        get() = Intent("com.android.systemui.demo")

    internal val enterDemoIntent: Intent = getCommandIntent(ENTER_DEMO_COMMAND_STRING)
    internal val exitDemoIntent: Intent = getCommandIntent(EXIT_DEMO_COMMAND_STRING)

    internal val showNetworkIcon: Intent = NetworkDemo.showNetwork()
    internal val hideNetworkIcon: Intent = NetworkDemo.hideNetwork()

    internal fun toggleNotificationVisibility(isVisible: Boolean): Intent {
        val visibilityExtra = "visible" to isVisible.toString()
        return getCommandIntent(NOTIFICATION_DEMO_COMMAND_STRING, visibilityExtra)
    }

    internal fun clockIntent(time: String) = demoIntent
            .putExtra("command", "clock")
            .putExtra(TIME_EXTRA_KEY, time)

    internal fun getCommandIntent(command: String, vararg extras: Pair<String, Any>): Intent {
        return demoIntent.apply {
            putExtra("command", command)
            extras.iterator().forEachRemaining { (key, value) ->
                putExtra(key, value.toString())
            }
        }
    }
}
