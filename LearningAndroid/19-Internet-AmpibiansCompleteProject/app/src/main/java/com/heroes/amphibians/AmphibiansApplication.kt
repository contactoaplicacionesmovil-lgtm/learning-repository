package com.heroes.amphibians

import android.app.Application
import com.heroes.amphibians.data.AppContainer
import com.heroes.amphibians.data.DefaultAppContainer

class AmphibiansApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}