package com.example.demomodetile.di

import android.content.Context
import com.example.demomodetile.DemoActivity
import com.example.demomodetile.DemoTileService

private var demoComponent: DemoComponent? = null

fun DemoActivity.inject(context: Context) {
    buildDemoComponent(context).inject(this)
}

fun DemoTileService.inject(context: Context) {
    buildDemoComponent(context).inject(this)
}

private fun buildDemoComponent(context: Context): DemoComponent {
    if (demoComponent == null) {
        demoComponent = DaggerDemoComponent.builder()
            .demoModule(DemoModule(context))
            .build()
    }
    return demoComponent!!
}
