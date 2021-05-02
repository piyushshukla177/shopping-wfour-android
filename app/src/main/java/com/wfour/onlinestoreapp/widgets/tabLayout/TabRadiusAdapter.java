package com.wfour.onlinestoreapp.widgets.tabLayout;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/03/2017.
 */

public class TabRadiusAdapter extends RecyclerView.Adapter<TabRadiusAdapter.ViewHolder> {

    private ArrayList<ItemTabRadius> itemTabRadiuses;
    private Context context;
    private ItemTabRadius itemTabRadius;

    public TabRadiusAdapter(ArrayList<ItemTabRadius> itemTabRadiuses, Context context) {
        this.itemTabRadiuses = itemTabRadiuses;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_radius, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        itemTabRadius = itemTabRadiuses.get(position);

        holder.tvTitle.setText(itemTabRadius.getName());
        if (itemTabRadius.getId()==0){
            if (itemTabRadius.isSelected()){
                holder.imgBg.setImageResource(R.drawable.bg_tab_layout_rectangle_red);
                holder.imgIcon.setImageResource(R.drawable.bg_tab_layout_rectangle_red);

            }else {

            }


        }else if (itemTabRadius.getId()==itemTabRadiuses.size()-1){

        }


    }

    @Override
    public int getItemCount() {
        return itemTabRadiuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBg, imgIcon;
        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imgBg = (ImageView) itemView.findViewById(R.id.img_bg);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }


}
