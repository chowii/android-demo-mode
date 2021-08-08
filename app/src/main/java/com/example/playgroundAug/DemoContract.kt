package com.example.playgroundAug

interface DemoContract {
    sealed class Actions {
        object EnableDemoMode: Actions()
        object DisableDemoMode: Actions()
        object ShowClockDialog: Actions()

        data class SetClock(val time: String): Actions()
    }
}