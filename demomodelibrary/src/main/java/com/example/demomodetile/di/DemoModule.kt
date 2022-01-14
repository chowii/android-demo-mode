package com.example.demomodetile.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.example.demomodetile.DemoModeInteractor
import com.example.demomodetile.DemoPreferences
import com.example.demomodetile.DemoViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module
class DemoModule(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "demo_prefs")

    @Provides
    fun provideDataStore(): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideDemoModeInteractor(
        demoPreferences: DemoPreferences
    ) = DemoModeInteractor(
        demoPreferences
    )

    @Provides
    @Singleton
    fun provideDemoPreferences(dataStore: DataStore<Preferences>) = DemoPreferences(dataStore)

    @Provides
    @IntoMap
    @ViewModelKey(DemoViewModel::class)
    fun provideDemoModeViewModel(
        demoModeInteractor: DemoModeInteractor
    ): ViewModel = DemoViewModel(
        context.applicationContext as Application,
        demoModeInteractor
    )

    @Provides
    fun provideViewModelFactory(
        viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(viewModels)
    }
}
