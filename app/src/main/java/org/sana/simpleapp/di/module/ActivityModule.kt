package org.sana.simpleapp.di.module


import org.sana.simpleapp.view.MainActivity
import org.sana.simpleapp.view.MapsActivity
import org.sana.simpleapp.view.RegisterActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeRegisterActivity(): RegisterActivity


    @ContributesAndroidInjector
    abstract fun contributeMapsActivity(): MapsActivity
}