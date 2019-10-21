package org.sana.simpleapp.utils

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by mehdi on 19/10/2019.
 */

object Connection {

    val BASE_URL = "http://stage.achareh.ir/api/"

    fun connect(): Retrofit {

        return Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }
}
