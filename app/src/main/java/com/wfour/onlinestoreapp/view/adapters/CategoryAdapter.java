package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.MyOnClickCategory;
import com.wfour.onlinestoreapp.objects.Category;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

    private ArrayList<Category> listData;
    private Activity context;
    private MyOnClickCategory onclick;
    private int w,h;

    public CategoryAdapter(Activity context, ArrayList<Category> listData, MyOnClickCategory onclick){
        this.context = context;
        this.listData = listData;
        this.onclick = onclick;
        initParam(context);
    }

    public void addList(ArrayList<Category> listData) {
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    private void initParam(Activity context) {
        //h = AppUtil.getScreenHeight(context);
        w = AppUtil.getScreenHeight(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
//
//        CardView cardView = view.findViewById(R.id.cardView);
//        ViewGroup.LayoutParams params = cardView.getLayoutParams();
////        params.height= (int) (h * 0.2);
//        params.width = (int) (w * 0.25);
//        cardView.setLayoutParams(params);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Category item = listData.get(position);
        holder.tvName.setText(item.getName());
        ImageUtil.setImage(context, holder.imgAvatar, item.getImage());

//      holder.imgAvatar.setImageResource(item.getImage());
        Log.d( "onBindViewHolder", item.getImage()+ "");
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onclick != null){
//                    onclick.onClick(item, position);
//                }
//            }
//        });
        holder.lnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onclick != null){
                    onclick.onClick(item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (listData == null) ? 0: listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private CardView cardView;
        private ImageView imgAvatar;
        private LinearLayout lnl;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            //cardView = itemView.findViewById(R.id.cardView);
            imgAvatar = itemView.findViewById(R.id.imv_avatar);
            lnl = itemView.findViewById(R.id.lnl);
        }
    }
}
