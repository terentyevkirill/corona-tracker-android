package com.terentiev.coronatracker

import android.app.Application
import android.content.Context
import dagger.Module

@Module
class AppModule {
    fun providesControl(application: Application): Context {
        return application.applicationContext
    }
}

