package com.terentiev.coronatracker.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module

@Module
internal abstract class ViewModelBuilderModule {
    internal abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory):
            ViewModelProvider.Factory
}