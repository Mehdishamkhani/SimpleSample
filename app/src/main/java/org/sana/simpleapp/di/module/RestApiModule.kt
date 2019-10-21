package org.sana.simpleapp.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.sana.simpleapp.retrofit.BasicAuthInterceptor
import org.sana.simpleapp.retrofit.RestApi
import org.sana.simpleapp.utils.Connection
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by mehdi on 19/10/2019.
 */


@Module
class RestApiModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Connection.BASE_URL)
                .client(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor("09822222222", "sana1234"))
                        .connectTimeout(60,TimeUnit.SECONDS)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .build())
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