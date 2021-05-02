package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.ActionReceiver;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickMenuListener;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.wfour.onlinestoreapp.globals.Constants.COLUM_GIRD_DEAL;
import static com.wfour.onlinestoreapp.globals.Constants.COLUM_LIST_DEAL;

/**
 * Created by Suusoft on 12/16/2016.
 */

public class ReservationAdatper extends RecyclerView.Adapter<ReservationAdatper.ViewHolder> {
    public static final int RQ_UPDATE_DEAL = 35;
    private RelativeLayout.LayoutParams layoutParams;
    private Fragment context;
    private ArrayList<DealObj> dealObjs;
    private IOnItemClickMenuListener listener;
    private String type;
    private int percent;
    private int w , h;

    public ReservationAdatper(Fragment context, ArrayList<DealObj> dealObjs, String type, IOnItemClickMenuListener listener) {
        this.context = context;
        this.dealObjs = dealObjs;
        this.listener = listener;
        this.type = type;
        initParam(context.getActivity());
    }

    private void initParam(Activity context) {
        int wDp = AppUtil.getScreenWidth(context);

        if (AppUtil.getWidthDp(context) > 600 ){
            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_item_category_margin)))/ COLUM_GIRD_DEAL);
        }else {
            w = (int) ((wDp - AppUtil.convertDpToPixel(context, context.getResources().getDimension(R.dimen.dimen_item_category_margin)))/COLUM_LIST_DEAL);
        }
        h = (int) (w * 0.3);
        layoutParams = new RelativeLayout.LayoutParams(w, h);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal_suusoft, parent, false);
        view.setLayoutParams(layoutParams);
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
    public void onBindViewHolder(final ReservationAdatper.ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final DealObj dealObj = dealObjs.get(position);
            if (dealObj != null) {
                ImageUtil.setImage(context.getActivity(), holder.imgDeal, dealObj.getImageUrl());
                holder.lblDealName.setText(dealObj.getName());
                if (dealObj.getDiscount_type() == null || dealObj.getCategory_id() == Integer.parseInt(DealCateObj.LABOR)||dealObj.getDiscount_type() != null && dealObj.getDiscount_type().isEmpty()) {
                    holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                            StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                    holder.lblDiscount.setVisibility(View.GONE);
                    holder.lblSalePercent.setVisibility(View.GONE);
                } else if (dealObj.getDiscount_type() != null && dealObj.getDiscount_type().equals(Constants.AMOUNT)) {

                    if (dealObj.getDiscount_price() != 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice() - dealObj.getDiscount_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        percent = percentPriceOldAndPriceSale(dealObj.getPrice(), dealObj.getDiscount_price() );
                        holder.lblSalePercent.setText(percent + " %");
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                        holder.lblSalePercent.setVisibility(View.VISIBLE);
                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        holder.lblSalePercent.setVisibility(View.GONE);
//
                    }
                } else if (dealObj.getDiscount_type() != null && dealObj.getDiscount_type().equals(Constants.PERCENT)) {
                    if (dealObj.getDiscount_rate() > 0) {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getSale_price(), 1)));
                        holder.lblDiscount.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        percent = percentPriceOldAndPriceNew(dealObj.getPrice(), dealObj.getSale_price());
                        holder.lblSalePercent.setText(percent + " %");
                        holder.lblSalePercent.setVisibility(View.VISIBLE);
                        holder.lblDiscount.setVisibility(View.VISIBLE);
                    } else {
                        holder.lblDealPrice.setText(String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(dealObj.getPrice(), 1)));
                        holder.lblDiscount.setVisibility(View.GONE);
                        holder.lblSalePercent.setVisibility(View.GONE);
                    }

                }

                holder.lblFavoriteQuantity.setText(String.valueOf(dealObj.getFavoriteQuantity()));
                if (dealObj.isFavorite()) {
                    holder.imgFavorite.setImageResource(R.drawable.ic_like_active);
                } else {
                    holder.imgFavorite.setImageResource(R.drawable.ic_like);
                }

                if (dealObj.getRateQuantity() > 0) {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.lblRateQuantity.setVisibility(View.VISIBLE);

                    holder.ratingBar.setRating(dealObj.getRate());
                    holder.lblRateQuantity.setText(String.valueOf(dealObj.getRateQuantity()));
                } else {
                    holder.ratingBar.setVisibility(View.GONE);
                    holder.lblRateQuantity.setVisibility(View.GONE);
                }
                if (!dealObj.getSeller_id().equals(DataStoreManager.getUser().getId())) {
                    //holder.imgMore.setVisibility(View.VISIBLE);
//                    holder.imgMore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClickSize(View view) {
//                            showPopupMenu(listener, position, holder.imgMore);
//                        }
//                    });
                } else {
                    //holder.imgMore.setVisibility(View.GONE);
                }
                if (type.equals(Constants.NO_DEALS)) {
                 //   holder.imgMore.setVisibility(View.GONE);
                } else {
                    //holder.imgMore.setVisibility(View.VISIBLE);
//                    holder.imgMore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClickSize(View view) {
//                            showPopupMenu(listener, position, holder.imgMore);
//                        }
//                    });
                }
                holder.llParentEndTime.setVisibility(View.GONE);

//                if (type.equals(Constants.BOUGH)||type.equals(Constants.SOLD)) {
//                    if (dealObj.isOnline()) {
//                        holder.llParentEndTime.setVisibility(View.VISIBLE);
//                        long time = Long.parseLong(dealObj.getOnline_started()) + (dealObj.getOnline_duration() * 3600);
//                        String datetime = DateTimeUtil.convertTimeStampToDate(time, "HH:mm, EEE dd-MM-yyyy");
//                        holder.tvEndTime.setText(datetime);
//                    } else {
//                        holder.llParentEndTime.setVisibility(View.VISIBLE);
//                        holder.tvEndTime.setText(context.getContext().getString(R.string.msg_expired));
//                    }
//
//                } else {
//                    holder.llParentEndTime.setVisibility(View.GONE);
//                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dealObj.setPositionInList(position);
                        Bundle bundle = new Bundle();
                        dealObj.isHideEndTime(true);
                        bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
                        Intent intent = new Intent(context.getActivity(), DealDetailActivity.class);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivityForResult(intent, RQ_UPDATE_DEAL);
                    }
                });
            }
        }
    }

    private void showPopupMenu(final IOnItemClickMenuListener listener, final int position, ImageView view) {
        PopupMenu popupMenu = new PopupMenu(context.getActivity(), view);
        popupMenu.inflate(R.menu.menu_deal);
        if (type.equals(Constants.SOLD)) {
            MenuItem item = popupMenu.getMenu().findItem(R.id.action_pay);
            item.setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_rate) {
                    listener.onClickRate(position);
                } else if (item.getItemId() == R.id.action_pay) {
                    listener.onClickPay(position);
                }
                return true;
            }
        });
        popupMenu.show();

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

        private ImageView imgDeal, imgFavorite, imgMore;
        private TextView lblDealName, lblDealPrice, lblFavoriteQuantity, lblRateQuantity, lblDiscount, lblSalePercent;
        private RatingBar ratingBar;

        private LinearLayout llParentEndTime;
        private TextViewRegular tvEndTime;

        public ViewHolder(View view) {
            super(view);
            imgDeal = (ImageView) view.findViewById(R.id.img_deal);
            imgFavorite = (ImageView) view.findViewById(R.id.img_favorite);
            lblDealName = (TextViewRegular) view.findViewById(R.id.lbl_deal_name);
            lblDealName.setSelected(true);
            lblDealPrice = (TextView) view.findViewById(R.id.lbl_price);
            lblDealPrice.setSelected(true);
            lblSalePercent = (TextView) view.findViewById(R.id.tv_sale_percent);
            lblFavoriteQuantity = (TextViewRegular) view.findViewById(R.id.lbl_favorite_quantity);
            lblRateQuantity = (TextView) view.findViewById(R.id.lbl_rate_quantity);
            ratingBar = (RatingBar) view.findViewById(R.id.rating);
            lblDiscount = (TextViewRegular) view.findViewById(R.id.lbl_price_old);
            lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            lblDiscount.setSelected(true);
            lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            imgMore = (ImageView) view.findViewById(R.id.img_more);
            llParentEndTime = (LinearLayout) view.findViewById(R.id.ll_parent_end_time);
            tvEndTime = (TextViewRegular) view.findViewById(R.id.tv_end_time);
            //imgMore.setVisibility(View.VISIBLE);
            ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2)
                    .setColorFilter(ratingBar.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NetworkUtility.getInstance(view.getContext()).isNetworkAvailable()) {
                        favorite(view.getContext(), dealObjs.get(getAdapterPosition()));
                        ActionReceiver.sendBroadcast(context.getContext(), ActionReceiver.ACTION_FAVORITE);
                    } else {
                        AppUtil.showToast(view.getContext(), R.string.msg_network_not_available);
                    }
                }
            });
        }




        private void favorite(final Context context, final DealObj item) {
            ModelManager.favorite(context, item.getId(), ModelManager.FAVORITE_TYPE_DEAL, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        item.setFavorite(!item.isFavorite());
                        if (item.isFavorite()) {
                            item.setFavoriteQuantity(item.getFavoriteQuantity() + 1);
                            Toast.makeText(context, context.getString(R.string.favorited), Toast.LENGTH_LONG).show();
                        } else {
                            item.setFavoriteQuantity(item.getFavoriteQuantity() - 1);
                            Toast.makeText(context, context.getString(R.string.unfavorited), Toast.LENGTH_LONG).show();
                        }
                        notifyDataSetChanged();
//                    sendBroastcast();
                    } else {
                        Toast.makeText(context, response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

    }
}
