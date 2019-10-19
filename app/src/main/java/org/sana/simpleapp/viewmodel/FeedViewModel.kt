package org.sana.simpleapp.viewmodel

import android.content.Context

import org.sana.simpleapp.retrofit.RestApi
import org.sana.simpleapp.di.AppContext
import org.sana.simpleapp.model.FeedDataModel

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.Result

class FeedViewModel @Inject
constructor(@AppContext context: Context, var service: RestApi) : ViewModel() {

    val feed: Flowable<Result<FeedDataModel>>
        get() = service.userFeed.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

}
