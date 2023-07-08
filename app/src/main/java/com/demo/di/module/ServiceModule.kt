package com.demo.di.module

import com.demo.ui.services.ServiceManager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun serviceManager(): ServiceManager
}