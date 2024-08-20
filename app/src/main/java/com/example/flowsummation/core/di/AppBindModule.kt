package com.example.flowsummation.core.di

import android.content.Context
import com.example.flowsummation.data.Repository
import com.example.flowsummation.domain.Interactor
import dagger.Binds
import dagger.Module

@Module()
interface AppBindModule {
    @Binds
    fun bindsInteractor(impl: Repository): Interactor
}