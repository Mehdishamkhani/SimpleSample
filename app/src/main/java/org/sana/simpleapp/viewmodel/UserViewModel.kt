package org.sana.simpleapp.viewmodel

import android.content.Context

import org.sana.simpleapp.retrofit.RestApi
import org.sana.simpleapp.di.AppContext

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.sana.simpleapp.model.RegisterUserModel

class UserViewModel @Inject constructor(@AppContext context: Context, private val service: RestApi) : ViewModel() {

    fun registerUserData(data: RegisterUserModel): Completable {

        return service.registerUserData(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }


}
