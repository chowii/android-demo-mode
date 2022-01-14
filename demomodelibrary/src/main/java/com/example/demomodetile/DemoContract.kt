package com.example.demomodetile

interface DemoContract {

    data class ViewState(
        val isEnabled: Boolean,
        val clock: String?,
        val isNetworkHidden: Boolean,
        val isNotificationHidden: Boolean,
    )

    sealed class Actions {
        object EnableDemoMode: Actions()
        object DisableDemoMode: Actions()

        object ShowNetworkIcon: Actions()
        object HideNetworkIcon: Actions()

        object ShowClockDialog: Actions()

        data class ToggleNotification(val isVisible: Boolean) : Actions()
        data class SetClock(val time: String): Actions()
    }
}