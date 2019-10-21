package org.sana.simpleapp.di.component

import android.app.Application
import android.app.Fragment

import org.sana.simpleapp.App
import org.sana.simpleapp.di.module.ActivityModule
import org.sana.simpleapp.di.module.AppModule
import org.sana.simpleapp.di.module.RestApiModule
import org.sana.simpleapp.di.module.ViewModelModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by mehdi on 19/10/2019.
 */


@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class, RestApiModule::class, ViewModelModule::class, ActivityModule::class))
@Singleton
interface AppComponent {


    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }


    fun inject(appController: App)
}