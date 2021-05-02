package com.wfour.onlinestoreapp.widgets.onscroll;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean isLoading = false; // True if we are still waiting for the last setSoftInputMode of data to load.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotalItemCount = 0;

    private int currentPage = 1; // current page

    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private boolean isEnded; // end of list
    private boolean isCustomItemContainList = false;

    public OnLoadMoreListener mLoadMoreListener;

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


    public EndlessRecyclerOnScrollListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.e(TAG, "onScrolled " + " dx " + dx + " dy " + dy + " !isEnded " +  !isEnded  + " visibleItemCount " + recyclerView.getChildCount() );
        if (!isEnded) {

            visibleItemCount = recyclerView.getChildCount();
            getItemInfor(recyclerView);
            if (visibleItemCount == totalItemCount && !isLoading) {
                Log.e(TAG, "visibleItemCount=" + visibleItemCount + " totalItemCount = " + totalItemCount +  " !isLoading =" + !isLoading  );
                //Log.e(TAG, "return");
                return;
            }
//            if (!isCustomItemContainList){
//
//            }


            boolean loadMore = false;
//            if (isCustomItemContainList)
//                loadMore = isCustomItemContainList;
//            else
                loadMore = visibleItemCount + firstVisibleItem >= totalItemCount - 4 ;
            Log.e(TAG, "loadMore=" + loadMore + " !isLoading = " + !isLoading +  " dy =" + dy  );
            if (!isLoading && loadMore && dy > 0) {
                onLoadMore();
                //Log.e(TAG, "onLoadMore");
            }

        }

    }

    private void getItemInfor(RecyclerView recyclerView) {

        if (mLinearLayoutManager != null){
            firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            totalItemCount = mLinearLayoutManager.getItemCount();
        }else if (mGridLayoutManager != null){
            firstVisibleItem = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
            totalItemCount = mGridLayoutManager.getItemCount();
        }else if (mStaggeredGridLayoutManager != null){
            firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
            totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        }else{
            if (recyclerView.getLayoutManager() instanceof  LinearLayoutManager) {
                mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            }else if (recyclerView.getLayoutManager() instanceof  GridLayoutManager){
                mGridLayoutManager =(GridLayoutManager) recyclerView.getLayoutManager();
            }else if (recyclerView.getLayoutManager() instanceof  StaggeredGridLayoutManager){
                mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            }
        }


    }

    private void onLoadMore(){
        currentPage++;
        mLoadMoreListener.onLoadMore(currentPage);
        isLoading = true;
    }

    public void onLoadMoreComplete(){
        isLoading = false;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;

    }

    public int getCurrentPage() {
        Log.e(TAG, "getCurrentPage " + currentPage  );
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCustomItemContainList(boolean customItemContainList) {
        isCustomItemContainList = customItemContainList;
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}