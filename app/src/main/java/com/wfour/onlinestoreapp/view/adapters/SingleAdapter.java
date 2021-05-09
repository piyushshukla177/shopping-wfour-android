package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.network.BaseRequest;
import com.wfour.onlinestoreapp.network.modelmanager.RequestManger;
import com.wfour.onlinestoreapp.objects.HomeObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;

import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.MyViewHolder> {
    ArrayList<ProductObj> productObjList;
    ArrayList<ProductObj> productObjListFull;
    private Activity context;
    private IMyOnClick onClick;
    private HomeObj homeObj;
    private ArrayList<HomeObj> homeObjList;
    private int w, h;

    public void initParam(Activity activity) {
        h = AppUtil.getScreenHeight(activity);
    }

    public SingleAdapter(Activity context, ArrayList<ProductObj> productObjList, IMyOnClick onClick) {
        this.context = context;
        this.productObjList = productObjList;
        this.onClick = onClick;
        initParam(context);
    }

    public void addList(ArrayList<ProductObj> dealObjList) {
        this.productObjList = dealObjList;
        this.notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_single_vertical, parent, false);

        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ProductObj productObj = productObjList.get(position);

        holder.add_fev_imageview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (DataStoreManager.getUser() != null) {
                            RequestManger.addFavorite(DataStoreManager.getUser().getId(),productObj.getId(), "deal", new BaseRequest.CompleteListener() {
                                @Override
                                public void onSuccess(com.wfour.onlinestoreapp.network.ApiResponse response) {
                                    if (!response.isError()) {
                                        AppUtil.showToast(context, response.getMessage() + "");
                                        holder.add_fev_imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_favorite));
                                    } else {
                                        AppUtil.showToast(context, response.getMessage() + "");
                                    }
                                }

                                @Override
                                public void onError(String message) {
//                                    Log.e(TAG, "onError: " + message);
                                }
                            });
                        }
                    }
                }
        );
        if(productObj.getIs_prize()==1){
            holder.tvTitle.setText(productObj.getTitle());
            holder.tvTitle.setVisibility(View.GONE);
        }
        else {
            holder.tvTitle.setText(productObj.getTitle());
            holder.tvTitle.setVisibility(View.VISIBLE);
        }
        if (productObj.getIs_prize() == 1) {
            holder.tvPrice.setBackgroundResource(R.color.colorPrimary);
            holder.tvPrice.setText(" P." + StringUtil.convertNumberToString(productObj.getPrice(), 0));
            holder.tvPrice.setGravity(Gravity.CENTER);
            holder.tvPrice.setTextColor(Color.WHITE);
        } else {
            holder.tvPrice.setBackgroundResource(R.color.transparent);
            holder.tvPrice.setText(" $" + StringUtil.convertNumberToString(productObj.getPrice(), 2));
        }

        if (productObj.getOld_price() == 0) {
            holder.tvOldPrice.setVisibility(View.GONE);
        }
        else {

            holder.tvOldPrice.setVisibility(View.VISIBLE);
            if(productObj.getIs_prize()==1){
                holder.tvOldPrice.setVisibility(View.GONE);
                holder.tvOldPrice.setText(" P." + StringUtil.convertNumberToString(productObj.getOld_price(), 0));
            }
            else {
                holder.tvOldPrice.setVisibility(View.VISIBLE);
                holder.tvOldPrice.setText(" $" + StringUtil.convertNumberToString(productObj.getOld_price(), 2));
            }

        }

        ImageUtil.setImage(context, holder.imgAvatar, productObj.getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.MyOnClick(position, productObj);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return (productObjList == null) ? 0 : productObjList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription, tvPrice, tvOldPrice;
        private CardView cardView;
        private ImageView imgAvatar,add_fev_imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cardView = itemView.findViewById(R.id.cardView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvPrice = itemView.findViewById(R.id.lbl_price);
            tvOldPrice = itemView.findViewById(R.id.lbl_price_old);
            add_fev_imageview = itemView.findViewById(R.id.add_fev_imageview);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}
