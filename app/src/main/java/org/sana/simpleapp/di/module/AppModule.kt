package org.sana.simpleapp.di.module

import android.app.Application
import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder


import org.sana.simpleapp.di.AppContext

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

/**
 * Created by mehdi on 19/10/2019.
 */



@Module
class AppModule {


    @Provides
    @Singleton
    @AppContext
    fun provideContext(application: Application): Context {
        return application
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }




}