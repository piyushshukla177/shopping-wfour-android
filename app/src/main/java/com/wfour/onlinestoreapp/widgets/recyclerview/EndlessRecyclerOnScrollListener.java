package com.wfour.onlinestoreapp.widgets.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    public enum LayoutManagerType {
        LINEAR_LAYOUT,
        GRID_LAYOUT,
        STAGGERED_LAYOUT
    }

    private boolean isLoading = false; // True if we are still waiting for the last set of data to load.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1; // current page

    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private LayoutManagerType mLayoutType;

    private boolean isEnded; // end of list

    private OnLoadMoreListener mLoadMoreListener;

    /**
     * Interface definition for a callback to be invoked when list reaches the
     * last item (the user load more items in the list)
     */
    public interface OnLoadMoreListener {
        /**
         * Called when the list reaches the last item (the last item is visible
         * to the user)
         */
        public void onLoadMore(int page);
    }


    public EndlessRecyclerOnScrollListener(OnLoadMoreListener onLoadMoreListener, RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) layoutManager;
            mLayoutType = LayoutManagerType.LINEAR_LAYOUT;
        } else if (layoutManager instanceof GridLayoutManager) {
            mGridLayoutManager = (GridLayoutManager) layoutManager;
            mLayoutType = LayoutManagerType.GRID_LAYOUT;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            mLayoutType = LayoutManagerType.STAGGERED_LAYOUT;

        }
        mLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isEnded) {
            visibleItemCount = recyclerView.getChildCount();
            getItemInfor();

            if (visibleItemCount == totalItemCount && !isLoading) {
                Log.d(TAG, "return");
                onLoadMore();
                return;
            }

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!isLoading && loadMore) {
                onLoadMore();

            }

        }

    }

    private void getItemInfor() {
        if (mLayoutType == LayoutManagerType.LINEAR_LAYOUT) {
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        } else if (mLayoutType == LayoutManagerType.GRID_LAYOUT) {
            totalItemCount = mGridLayoutManager.getItemCount();
            firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
        } else if (mLayoutType == LayoutManagerType.STAGGERED_LAYOUT) {
            totalItemCount = mStaggeredGridLayoutManager.getItemCount();
            firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
        }
    }

    private void onLoadMore() {
        current_page++;
        isLoading = true;
        mLoadMoreListener.onLoadMore(current_page);
    }

    public void onLoadMoreComplete() {
        isLoading = false;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrentPage(int page) {
        current_page = page;
    }
}