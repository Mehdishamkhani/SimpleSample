package org.sana.simpleapp.retrofit

import io.reactivex.Completable
import io.reactivex.Flowable
import org.sana.simpleapp.model.UserDetailsDataModel
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by mehdi on 19/10/2019.
 */

interface RestApi {


    @get:GET("karfarmas/address")
    val userFeed: Flowable<Result<List<UserDetailsDataModel>>>


    @POST("karfarmas/address")
    fun registerUserData(@Body data: UserDetailsDataModel): Completable

}
