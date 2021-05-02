package com.wfour.onlinestoreapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.*;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.adapters.FavoriteAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.wfour.onlinestoreapp.view.adapters.DealAdapter.RQ_UPDATE_DEAL;

/**
 * Created by Suusoft on 11/01/2017.
 */

public class FragmentFavorite extends com.wfour.onlinestoreapp.base.BaseFragment implements IOnItemClickListener, View.OnClickListener {

    private static String TAG = FragmentFavorite.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvNodata;
    private ArrayList<DealObj> dealObjs;
    private TextView btnGetItNow, tvCountFavorite;
    private FavoriteAdapter adapter;
    private GridLayoutManager manager;
    private LinearLayout linearLayout;
    private DealObj dealObj;
    private int posSelected = -1;
    private ProgressBar progressBar;


    public static FragmentFavorite newInstance() {
        Bundle args = new Bundle();
        FragmentFavorite fragment = new FragmentFavorite();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_chil_home;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        initControl(view);

    }

    private void initControl(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        tvNodata = (TextView) view.findViewById(R.id.tv_nodata);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_list);

        tvCountFavorite = (TextView) view.findViewById(R.id.tv_count_favorite);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvData);
        btnGetItNow  = (TextView) view.findViewById(R.id.btn_get_it_now);
        dealObjs = new ArrayList<>();
        if (AppUtil.getWidthDp(getActivity()) > 600 ){
            manager = new GridLayoutManager(self, 2);
        }else {
            manager = new GridLayoutManager(self, 1);
        }

        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new FavoriteAdapter(dealObjs, getActivity(), this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        btnGetItNow.setOnClickListener(this);

    }

    @Override
    protected void getData() {
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (NetworkUtility.isNetworkAvailable()){
            ModelManager.getDealList(self, null, null , "",  Constants.SEARCH_FAVORIES, null, null, AppController.getInstance().getLatMyLocation(),
                    AppController.getInstance().getLongMyLocation(), null, null, 1, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            Log.e(TAG, "onSuccess " + object.toString() );
                            JSONObject jsonObject = (JSONObject) object;
                            ApiResponse response = new ApiResponse(jsonObject);
                            dealObjs.clear();
                            if (!response.isError()) {
                                Log.e(TAG, "onSuccess response success" );
                                dealObjs.addAll(response.getDataList(DealObj.class));

                                adapter.notifyDataSetChanged();
                                showList();
                            } else {
                                AppUtil.showToast(self, response.getMessage() );
                                showNodata();
                            }

                            if (dealObjs.size()>9){
                                tvCountFavorite.setText( dealObjs.size()+"");
                            }else{
                                tvCountFavorite.setText( "0" + dealObjs.size());
                            }


                        }

                        @Override
                        public void onError() {
                            tvCountFavorite.setText(0);
                            showNodata();
                            AppUtil.showToast(self,  R.string.msg_have_some_errors );
                        }
                    });
        }else {
            progressBar.setVisibility(View.GONE);

            AppUtil.showToast(self, R.string.msg_network_not_available);
        }


    }

    private void showList(){
        if (dealObjs.size()>0){
            linearLayout.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
        }else {
            tvCountFavorite.setText("0");
            linearLayout.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showNodata(){
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        tvNodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        posSelected = position;
        dealObj = dealObjs.get(posSelected);
        dealObj.setPositionInList(posSelected);
        Bundle bundle = new Bundle();
        bundle.putString(Args.KEY_ID_DEAL, dealObj.getId());
        bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
        Intent intent = new Intent(self, DealDetailActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FragmentFavorite.this.startActivityForResult(intent, RQ_UPDATE_DEAL);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == DealAdapter.RQ_UPDATE_DEAL) {
//            Log.e(TAG, "requestCode == RQ_UPDATE_DEAL");
//            if (resultCode == DealDetailActivity.RC_UPDATE_DEAL) {
//                Log.e(TAG, "resultCode == DealDetailActivity.RC_UPDATE_DEAL");
//                dealObjs.clear();
//                adapter.notifyDataSetChanged();
//                getData();
//            }
//        }
    }

    @Override
    public void onClick(View v) {
//        if (posSelected>-1){
//            dealObj = dealObjs.get(posSelected);
//            dealObj.setPositionInList(posSelected);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
//            Intent intent = new Intent(self, DealDetailActivity.class);
//            intent.putExtras(bundle);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            FragmentFavorite.this.startActivityForResult(intent, RQ_UPDATE_DEAL);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume" );
        if (DataStoreManager.getUpdateDeal()){
            dealObjs.clear();
            adapter.notifyDataSetChanged();
            getData();
            DataStoreManager.saveUpdateDeal(false);
        }
    }
}
