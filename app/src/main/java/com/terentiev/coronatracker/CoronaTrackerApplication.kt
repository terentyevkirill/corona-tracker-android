package com.terentiev.coronatracker

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class CoronaTrackerApplication : DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}