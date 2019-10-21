package org.sana.simpleapp.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private Context context;
    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private int previousTotal = 0;
    private boolean loading = true;

    protected EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, Context context) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.context = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mLinearLayoutManager == null || context == null) return;

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 10) && firstVisibleItem != 0) {

            current_page++;
            onLoadMore(current_page);
            loading = true;

        }
    }

    public abstract void onLoadMore(int current_page);

}