/*
 * Copyright (c) 2020 Woolworths. All rights reserved.
 */

package com.example.demomodetile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import javax.inject.Provider

class ViewModelFactory(private val viewModels: Map<Class<out ViewModel>, Provider<ViewModel>>) : Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.get() as T
    }
}
