package com.example.flowsummation.core

import android.app.Application
import com.example.flowsummation.core.di.AppComponent
import com.example.flowsummation.core.di.AppBindModule
import com.example.flowsummation.core.di.DaggerAppComponent

class FlowSummationApplication:Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .build()
    }
}