package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 11/13/2017.
 */

public class FavoriteAdapter  extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    private ArrayList<DealObj> dealObjs;
    private Context context;
    private RelativeLayout.LayoutParams layoutParams;
    private DealObj item;
    private int percent;
    private IOnItemClickListener clickListener;
    private int posSetected = -1;


    public FavoriteAdapter(ArrayList<DealObj> dealObjs, Activity context,  IOnItemClickListener clickListener) {
        this.dealObjs = dealObjs;
        this.context = context;
        this.clickListener = clickListener;
        initParam(context);

    }

    private void initParam(Activity context) {
        int wDp = AppUtil.getScreenWidth(context);
        int w , h;
        if (AppUtil.getWidthDp(context)> 600 ){
            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_1x)))/3.3);
        }else {
            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_1x)))/2);
        }

        h = (int) (w * 1.3);
        layoutParams = new RelativeLayout.LayoutParams(w, h);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_favorite, parent, false);
        view.setLayoutParams(layoutParams);
        return new FavoriteAdapter.ViewHolder(view);
    }

    public int percentPriceOldAndPriceNew(double priceOld, double priceNew){
        int i = (int) (((priceOld - priceNew) / priceOld ) * 100);
        return i;
    }

    public int percentPriceOldAndPriceSale(double priceOld, double priceSale){
        int i = (int) ( (priceSale / priceOld ) * 100);
        return i;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            item = dealObjs.get(position);
            if (item != null) {
                ImageUtil.setImage(context, holder.imgDeal, item.getImageUrl());
                holder.lblDealName.setText(item.getName());
                if (item.getDiscount_type() == null || item.getCategory_id() == Integer.parseInt(DealCateObj.LABOR)||item.getDiscount_type() != null && item.getDiscount_type().isEmpty()) {
                    holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(item.getPrice(), 1)));
                    holder.lblDiscount.setVisibility(View.GONE);
                    holder.lblSalePercent.setVisibility(View.GONE);
                } else if (item.getDiscount_type() != null && item.getDiscount_type().equals(Constants.AMOUNT)) {

                    if (item.getDiscount_price() != 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(item.getPrice() - item.getDiscount_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(item.getPrice(), 1)));
                        percent = percentPriceOldAndPriceSale(item.getPrice(), item.getDiscount_price() );
                        holder.lblSalePercent.setText(percent + " %");
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                        holder.lblSalePercent.setVisibility(View.VISIBLE);

                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(item.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        holder.lblSalePercent.setVisibility(View.GONE);

                    }
                } else if (item.getDiscount_type() != null && item.getDiscount_type().equals(Constants.PERCENT)) {
                    if (item.getDiscount_rate() > 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(item.getSale_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(item.getPrice(), 1)));
                        percent = percentPriceOldAndPriceNew(item.getPrice(), item.getSale_price());
                        holder.lblSalePercent.setText(percent + " %");
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                        holder.lblSalePercent.setVisibility(View.VISIBLE);
                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(item.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        holder.lblSalePercent.setVisibility(View.GONE);
                    }
                }

                if (item.getRateQuantity() > 0) {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.lblRateQuantity.setVisibility(View.VISIBLE);

                    holder.ratingBar.setRating(item.getRate());
                    holder.lblRateQuantity.setText(String.valueOf(item.getRateQuantity()));
                } else {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.lblRateQuantity.setVisibility(View.VISIBLE);
                    holder.ratingBar.setRating(0);
                    holder.lblRateQuantity.setText("0");
                }
            }

            if (posSetected >-1){
                if (position==posSetected){
                    holder.imgSelected.setVisibility(View.VISIBLE);
                    holder.relativeLayout.setBackgroundResource(R.drawable.bg_item_favorite_selected);
                }else {
                    holder.imgSelected.setVisibility(View.GONE);
                    holder.relativeLayout.setBackgroundResource(R.drawable.bg_item_favorite);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return dealObjs.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgDeal;
        private ImageView imgSelected;
        private TextView lblDealName, lblDealPrice, lblRateQuantity, lblDiscount, lblSalePercent;
        private RatingBar ratingBar;
        private RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);

            imgDeal = (ImageView) view.findViewById(R.id.img_deal);
            imgSelected = (ImageView) view.findViewById(R.id.img_selected);
            lblDealName = (TextViewRegular) view.findViewById(R.id.lbl_deal_name);
            lblDealName.setSelected(true);
            lblDealPrice = (TextViewBold) view.findViewById(R.id.lbl_price);
            lblDealPrice.setSelected(true);
            lblRateQuantity = (TextView) view.findViewById(R.id.lbl_rate_quantity);
            lblSalePercent = (TextView) view.findViewById(R.id.tv_sale_percent);
            ratingBar = (RatingBar) view.findViewById(R.id.rating);
            lblDiscount = (TextViewRegular) view.findViewById(R.id.lbl_price_old);
            lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            lblDiscount.setSelected(true);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    posSetected = getAdapterPosition();
                    clickListener.onItemClick(posSetected);
                    notifyDataSetChanged();

                }
            });
        }
    }
}
