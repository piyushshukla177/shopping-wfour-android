package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 10/15/2015.
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder> {
    public static final int RQ_UPDATE_DEAL = 35;
    private Fragment context;
    private ArrayList<DealObj> dealObjs;
    private String type;
    private int percent;
    private RelativeLayout.LayoutParams layoutParams;
    private int w , h, wDp;

    public DealAdapter(Fragment context, ArrayList<DealObj> dealObjs, String type) {
        this.context = context;
        this.dealObjs = dealObjs;
        this.type = type;
        initParam(context.getActivity());

    }

    private void initParam(Activity context) {
         wDp = AppUtil.getScreenHeight(context);
//
//        if (AppUtil.getWidthDp(context) > 600 ){
//            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_item_category_margin)))/ COLUM_GIRD_DEAL);
//        }else {
//            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_item_category_margin)))/COLUM_LIST_DEAL);
//        }
//        h = (int) (w * 0.3);
//        layoutParams = new RelativeLayout.LayoutParams(w, h);
    }

    private int dpToPx(int dp){
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int)px;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal_update, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        params.height= (int) (wDp * 0.4);
        cardView.setLayoutParams(params);

       // view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final DealObj dealObj = dealObjs.get(position);
            if (dealObj != null) {
                ImageUtil.setImage(context.getActivity(), holder.imgDeal, dealObj.getImageUrl());
                holder.lblDealName.setText(dealObj.getName());
                if (dealObj.getDiscount_type() == null || dealObj.getCategory_id() == Integer.parseInt(DealCateObj.LABOR)||dealObj.getDiscount_type() != null && dealObj.getDiscount_type().isEmpty()) {
                    holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                    holder.lblDiscount.setVisibility(View.GONE);
                    //holder.lblSalePercent.setVisibility(View.GONE);
                } else if (dealObj.getDiscount_type() != null && dealObj.getDiscount_type().equals(Constants.AMOUNT)) {

                    if (dealObj.getDiscount_price() != 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice() - dealObj.getDiscount_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        percent = percentPriceOldAndPriceSale(dealObj.getPrice(), dealObj.getDiscount_price() );
                        //holder.lblSalePercent.setText(percent + " %");
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                        //holder.lblSalePercent.setVisibility(View.VISIBLE);

                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        //holder.lblSalePercent.setVisibility(View.GONE);

                    }
                } else if (dealObj.getDiscount_type() != null && dealObj.getDiscount_type().equals(Constants.PERCENT)) {
                    if (dealObj.getDiscount_rate() > 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getSale_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        percent = percentPriceOldAndPriceNew(dealObj.getPrice(), dealObj.getSale_price());
                        //holder.lblSalePercent.setText(percent + " %");
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                        //holder.lblSalePercent.setVisibility(View.VISIBLE);
                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value), StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        //holder.lblSalePercent.setVisibility(View.GONE);
                    }
                }
//                int left = dpToPx(24);
//                int top = dpToPx(12);
//                int right = dpToPx(24);
//                int bottom = dpToPx(12);
//
//                int spanCount = 2;
//                boolean isFirt2Item  = position < spanCount;
//                boolean isLast2Item = position > getItemCount() - spanCount;
//
//                if(isFirt2Item){
//                    top = dpToPx(24);
//                }
//                if(isLast2Item){
//                    bottom = dpToPx(24);
//                }
//
//                boolean isLeftSide= (position +1 ) % spanCount !=0;
//                boolean isRightSide = !isLeftSide;
//
//                if(isLeftSide){
//                    right = dpToPx(12);
//                }
//                if(isRightSide){
//                    left = dpToPx(12);
//                }
//
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)holder.cardView.getLayoutParams();
//                layoutParams.setMargins(left, top, right, bottom);
//                holder.cardView.setLayoutParams(layoutParams);
//                holder.lblFavoriteQuantity.setText(String.valueOf(dealObj.getFavoriteQuantity()));
//                if (dealObj.isFavorite()) {
//                    holder.imgFavorite.setImageResource(R.drawable.ic_like_active);
//                } else {
//                    holder.imgFavorite.setImageResource(R.drawable.ic_like);
//                }
//
//                if (dealObj.getRateQuantity() > 0) {
//                    holder.ratingBar.setVisibility(View.VISIBLE);
//                    holder.lblRateQuantity.setVisibility(View.VISIBLE);
//
//                    holder.ratingBar.setRating(dealObj.getRate());
//                    holder.lblRateQuantity.setText(String.valueOf(dealObj.getRateQuantity()));
//                } else {
//                    holder.ratingBar.setVisibility(View.GONE);
//                    holder.lblRateQuantity.setVisibility(View.GONE);
//                }
//
//                if (type.equals(Constants.SEARCH_MINE)) {
//                    if (dealObj.isOnline()) {
//                        holder.llParentEndTime.setVisibility(View.VISIBLE);
//                        long time = Long.parseLong(dealObj.getOnline_started()) + (dealObj.getOnline_duration() * 3600);
//                        String datetime = DateTimeUtil.convertTimeStampToDate(time, "HH:mm, EEE dd-MM-yyyy");
//                        holder.tvEndTime.setText(datetime);
//                    } else {
//                        holder.llParentEndTime.setVisibility(View.VISIBLE);
//                        holder.tvEndTime.setText(context.getContext().getString(R.string.msg_expired));
//                    }
//                } else {
//                    holder.llParentEndTime.setVisibility(View.GONE);
//                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dealObj.setPositionInList(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("position", String.valueOf(position));
                        bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
                        bundle.putString(Args.KEY_ID_DEAL, dealObj.getId());
                        Log.e("DealAdapter", "onClick: "+dealObj.getId() );
                        Intent intent = new Intent(context.getActivity(), DealDetailActivity.class);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivityForResult(intent, RQ_UPDATE_DEAL);
                    }
                });

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
       // private ImageView imgFavorite;
       // private TextView  lblFavoriteQuantity, lblRateQuantity, lblSalePercent;
        private TextView lblDealName, lblDealPrice,lblDiscount;
        private CardView cardView;
//        private RatingBar ratingBar;
//        private LinearLayout llParentEndTime;
//        private TextViewRegular tvEndTime;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            imgDeal = (ImageView) view.findViewById(R.id.img_deal);
            //imgFavorite = (ImageView) view.findViewById(R.id.img_favorite);
            lblDealName = (TextViewRegular) view.findViewById(R.id.lbl_deal_name);
            lblDealName.setSelected(true);
            lblDealPrice = (TextViewBold) view.findViewById(R.id.lbl_price);
            lblDealPrice.setSelected(true);
            //lblFavoriteQuantity = (TextViewRegular) view.findViewById(R.id.lbl_favorite_quantity);
            //lblRateQuantity = (TextView) view.findViewById(R.id.lbl_rate_quantity);
            //lblSalePercent = (TextView) view.findViewById(R.id.tv_sale_percent);
            //ratingBar = (RatingBar) view.findViewById(R.id.rating);
            lblDiscount = (TextViewRegular) view.findViewById(R.id.lbl_price_old);
            lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            lblDiscount.setSelected(true);
            //llParentEndTime = (LinearLayout) view.findViewById(R.id.ll_parent_end_time);
            //tvEndTime = (TextViewRegular) view.findViewById(R.id.tv_end_time);
            //((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2)
                    //.setColorFilter(ratingBar.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

//            imgFavorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClickSize(View view) {
//                    if (NetworkUtility.getInstance(view.getContext()).isNetworkAvailable()) {
//                        favorite(view.getContext(), dealObjs.get(getAdapterPosition()));
//                        ActionReceiver.sendBroadcast(context.getContext(), ActionReceiver.ACTION_FAVORITE);
//                    } else {
//                        AppUtil.showToast(view.getContext(), R.string.msg_network_not_available);
//                    }
//                }
//            });
        }

//        private void favorite(final Context context, final DealObj item) {
//            ModelManager.favorite(context, item.getId(), ModelManager.FAVORITE_TYPE_DEAL, new ModelManagerListener() {
//                @Override
//                public void onSuccess(Object object) {
//                    JSONObject jsonObject = (JSONObject) object;
//                    ApiResponse response = new ApiResponse(jsonObject);
//                    if (!response.isError()) {
//                        item.setFavorite(!item.isFavorite());
//                        if (item.isFavorite()) {
//                            item.setFavoriteQuantity(item.getFavoriteQuantity() + 1);
//                            Toast.makeText(context, context.getString(R.string.favorited), Toast.LENGTH_LONG).show();
//                        } else {
//                            item.setFavoriteQuantity(item.getFavoriteQuantity() - 1);
//                            Toast.makeText(context, context.getString(R.string.unfavorited), Toast.LENGTH_LONG).show();
//                        }
//                        notifyDataSetChanged();
////                    sendBroastcast();
//                    } else {
//                        Toast.makeText(context, response.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
//        }


    }


}
