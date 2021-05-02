package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.utils.AppUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Suusoft on 10/15/2015.
 */
public class DealCateAdapter extends RecyclerView.Adapter<DealCateAdapter.ViewHolder> {

    private Activity context;
    private IOnItemClickListener clickListener;
    private ArrayList<DealCateObj> dealCateObjs;
    private boolean isHiddenDes;
    private String[] arrColorBackground;
    private int randomBefore = 0, i = 0;
    private int width, height;
    private LinearLayout.LayoutParams layoutParams;

    public boolean isHiddenDes() {
        return isHiddenDes;
    }

    public void setHiddenDes(boolean hiddenDes) {
        isHiddenDes = hiddenDes;
    }

    public DealCateAdapter(Activity context, ArrayList<DealCateObj> dealCateObjs , IOnItemClickListener clickListener ) {
        this.context = context;
        this.clickListener = clickListener;
        this.dealCateObjs = dealCateObjs;
        this.arrColorBackground = context.getResources().getStringArray(R.array.array_color);
        this.width = ((AppUtil.getScreenWidth(context) - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_item_category_margin)))/Constants.COLUM_GRID );
//        this.height = ((AppUtil.getScreenWidth(context) - AppUtil.convertDpToPixel(context, 0 )) / (Constants.COLUM_GRID ));
        this.height = (int) (width/3);
        this.layoutParams = new LinearLayout.LayoutParams(width, height);
        random = new Random();
    }

    Random random;
    private int getRandomHeight(int min, int max){
        return random.nextInt((max - min) + 0) + min;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cate_deal, parent, false);
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = getRandomHeight(1,2)*width ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final DealCateObj dealCateObj = dealCateObjs.get(position);
            if (dealCateObj != null) {
                holder.lblCate.setText(dealCateObj.getName().trim());
                holder.lblCate.setSelected(true);
                holder.tvDescription.setText(dealCateObj.getDescription());
//                holder.imgBackground.setBackgroundColor(getRandomColor());
                holder.imgCate.setImageResource(dealCateObj.getDrawable());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(position);
                    }
                });

//                if (isHiddenDes){
//                    holder.tvDescription.setVisibility(View.GONE);
//                }else{
//                    holder.tvDescription.setVisibility(View.VISIBLE);
//                }
            }
        }
    }

    private int getRandomColor(){
        if (i < arrColorBackground.length - 1) i++;
        if (i == arrColorBackground.length - 1) i = 0;
        return Color.parseColor(arrColorBackground[i]);
    }

    @Override
    public int getItemCount() {
        try {
            return dealCateObjs.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblCate, tvDescription;
        private ImageView imgCate;
        private ImageView imgBackground;

        public ViewHolder(View view) {
            super(view);
            lblCate = (TextView) view.findViewById(R.id.lbl_cate);
            imgBackground = (ImageView) view.findViewById(R.id.img_background);
            imgCate = (ImageView) view.findViewById(R.id.img_cate);
            tvDescription = (TextView) view.findViewById(R.id.lbl_description);
            tvDescription.setSelected(true);
        }
    }
}
