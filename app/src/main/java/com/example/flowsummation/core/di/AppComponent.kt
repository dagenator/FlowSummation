package com.example.flowsummation.core.di

import com.example.flowsummation.presenter.MainActivity
import dagger.Component

@Component(modules = [AppBindModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}