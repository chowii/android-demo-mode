package com.example.playgroundAug

interface DemoContract {
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