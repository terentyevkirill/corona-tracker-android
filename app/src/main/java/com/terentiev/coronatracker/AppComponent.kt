package com.terentiev.coronatracker

import com.terentiev.coronatracker.di.MainActivityModule
import com.terentiev.coronatracker.di.ViewModelBuilderModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        MainActivityModule::class,
        ViewModelBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<CoronaTrackerApplication> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<CoronaTrackerApplication>
}