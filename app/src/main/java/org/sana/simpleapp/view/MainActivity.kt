package org.sana.simpleapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.activity_main.*
import org.sana.simpleapp.model.FeedDataModel
import org.sana.simpleapp.utils.EndlessRecyclerOnScrollListener
import org.sana.simpleapp.utils.HttpHelper
import org.sana.simpleapp.viewmodel.FeedViewModel
import org.sana.simpleapp.viewmodel.ViewModelFactory
import org.sanasimpleapp.R
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

/**
 * Created by mehdi on 19/10/2019.
 */

class MainActivity : BaseActivity() {


    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userDetailsFeedAdapter: UserDetailsAdapter
    private var disposable: Disposable? = null
    var data: ArrayList<FeedDataModel> = ArrayList()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var feedViewModel: FeedViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    internal lateinit var pagination: PublishProcessor<Int>

    var requestOnWay = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        feedViewModel = ViewModelProviders.of(this, viewModelFactory)[FeedViewModel::class.java]


        userDetailsFeedAdapter = UserDetailsAdapter(data)
        list.adapter = userDetailsFeedAdapter
        linearLayoutManager = LinearLayoutManager(this)
        list.layoutManager = linearLayoutManager
        list.addOnScrollListener(getScrollListener())

        pagination = PublishProcessor.create()
        disposable = pagination.onBackpressureDrop().doOnNext { var requestOnWay = true }
                .concatMap { feedViewModel.feed }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { this.handleUserDetailsResult(it) }
                .doOnError {
                    requestOnWay = false
                    loading.visibility = View.GONE
                    HttpHelper.showError(this@MainActivity, it)

                }.subscribe()


        pagination.onNext(0)
        compositeDisposable.add(disposable!!)


    }

    private fun handleUserDetailsResult(result: Result<List<FeedDataModel>>) {

        Log.d("iserror", result.error().toString());
        if (!result.isError && result.response()!!.isSuccessful)
            result.response()?.body()?.let { userDetailsFeedAdapter.addData(it) }

        list.visibility = View.VISIBLE
        loading.visibility = View.GONE

    }

    private fun getScrollListener(): EndlessRecyclerOnScrollListener {
        return object : EndlessRecyclerOnScrollListener(linearLayoutManager, this) {
            override fun onLoadMore(page: Int) {
                if (!requestOnWay) {
                    loading.visibility = View.VISIBLE
                    pagination.onNext(page)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}

