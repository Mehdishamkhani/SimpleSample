package org.sana.simpleapp.di.module


import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.sana.simpleapp.view.MainActivity
import org.sana.simpleapp.view.MapActivity
import org.sana.simpleapp.view.RegisterActivity

/**
 * Created by mehdi on 19/10/2019.
 */


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeRegisterActivity(): RegisterActivity


    @ContributesAndroidInjector
    abstract fun contributeMapsActivity(): MapActivity
}