package com.wfour.onlinestoreapp.widgets.tabLayout;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/03/2017.
 */

public class TabLayoutRadius extends LinearLayout {
    private Context mContext;
    private RecyclerView rvTab;
    private TabRadiusAdapter adapter;
    private ArrayList<ItemTabRadius> itemTabRadiuses;

    public TabLayoutRadius(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public void setListItemRadius(ArrayList<ItemTabRadius> itemTabRadiuses){
        this.itemTabRadiuses = itemTabRadiuses;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.tablayout_radius, this);
            rvTab = (RecyclerView) findViewById(R.id.rv_item_tab);
            adapter = new TabRadiusAdapter(itemTabRadiuses, mContext);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvTab.setLayoutManager(manager);
            rvTab.setAdapter(adapter);
            rvTab.setHasFixedSize(true);

        }
    }
}
