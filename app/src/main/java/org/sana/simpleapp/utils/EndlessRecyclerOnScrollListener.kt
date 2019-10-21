package org.sana.simpleapp.utils

import android.content.Context

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener protected constructor(private val mLinearLayoutManager: LinearLayoutManager?, private val context: Context?) : RecyclerView.OnScrollListener() {
    private var current_page = 1
    private var previousTotal = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (mLinearLayoutManager == null || context == null) return

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = mLinearLayoutManager.itemCount
        val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + 10 && firstVisibleItem != 0) {

            current_page++
            onLoadMore(current_page)
            loading = true

        }
    }

    abstract fun onLoadMore(current_page: Int)

    companion object {
        const val TAG = "@EndlessRecyclerOnScrollListener"
    }

}