package org.sana.simpleapp.retrofit

import org.sana.simpleapp.model.FeedDataModel

import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.ResponseBody
import org.sana.simpleapp.model.RegisterUserModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by mehdi on 19/10/2019.
 */

interface RestApi {


    @get:GET("karfarmas/address")
    val userFeed: Flowable<Result<List<FeedDataModel>>>


    @POST("karfarmas/address")
    fun registerUserData(@Body data: RegisterUserModel): Completable

}
