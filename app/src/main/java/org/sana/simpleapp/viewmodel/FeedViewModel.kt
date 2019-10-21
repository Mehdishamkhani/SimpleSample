package org.sana.simpleapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.sana.simpleapp.di.AppContext
import org.sana.simpleapp.model.FeedDataModel
import org.sana.simpleapp.retrofit.RestApi
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

/**
 * Created by mehdi on 19/10/2019.
 */

class FeedViewModel @Inject
constructor(var service: RestApi) : ViewModel() {

    val feed: Flowable<Result<List<FeedDataModel>>>
        get() = service.userFeed.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

}
