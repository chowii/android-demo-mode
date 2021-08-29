package com.example.demomodetile.di

import android.content.Context
import com.example.demomodetile.DemoActivity
import com.example.demomodetile.DemoTileService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DemoModule::class,]
)
interface DemoComponent {
    fun inject(activity: DemoActivity)
    fun inject(service: DemoTileService)
}
