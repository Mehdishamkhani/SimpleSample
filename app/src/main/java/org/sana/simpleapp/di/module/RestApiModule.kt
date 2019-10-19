package org.sana.simpleapp.di.module

import org.sana.simpleapp.utils.Connection
import org.sana.simpleapp.retrofit.RestApi

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RestApiModule {



    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Connection.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideFeedService(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }


}