package org.sana.simpleapp.viewmodel

import org.sana.simpleapp.retrofit.RestApi

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.sana.simpleapp.model.UserDetailsDataModel

/**
 * Created by mehdi on 19/10/2019.
 */

class UserViewModel @Inject constructor(private val service: RestApi) : ViewModel() {

    fun registerUserData(data: UserDetailsDataModel): Completable {
        return service.registerUserData(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }


}
