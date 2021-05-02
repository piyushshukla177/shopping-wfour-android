package com.wfour.onlinestoreapp.view.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.StringUtil;

/**
 * Created by Suusoft on 11/07/2017.
 */

public class FragmentChidChatAboutDeal extends com.wfour.onlinestoreapp.base.BaseFragment {

    private DealObj mDealObj;
    private TextView tvNameDealCate, lblSalePercent, lblDealPrice, lblDiscount, lblRateQuantity;
    private RatingBar ratingBar;
    private String categoryId, nameCateDeal;
    private int percent;

    public static FragmentChidChatAboutDeal newInstance(DealObj mDealObj) {
        Bundle args = new Bundle();
        FragmentChidChatAboutDeal fragment = new FragmentChidChatAboutDeal();
        fragment.setArguments(args);
        fragment.mDealObj = mDealObj;
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.layout_child_about_of_deal;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        ratingBar = (RatingBar) view.findViewById(R.id.rating);
        tvNameDealCate = (TextView) view.findViewById(R.id.lbl_deal_name);
        lblSalePercent  = (TextView) view.findViewById(R.id.tv_sale_percent);
        lblDealPrice   = (TextView) view.findViewById(R.id.lbl_price);
        lblDiscount = (TextView) view.findViewById(R.id.lbl_price_old);
        lblRateQuantity  = (TextView) view.findViewById(R.id.lbl_rate_quantity);
        lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lblDiscount.setSelected(true);
        tvNameDealCate.setSelected(true);
        lblDealPrice.setSelected(true);

        setData();
    }

    public int percentPriceOldAndPriceNew(double priceOld, double priceNew){
        int i = (int) (((priceOld - priceNew) / priceOld ) * 100);
        return i;
    }

    public int percentPriceOldAndPriceSale(double priceOld, double priceSale){
        int i = (int) ( (priceSale / priceOld ) * 100);
        return i;
    }

    private void setData() {
        if (mDealObj!=null){
            nameCateDeal = getNameCate();
            tvNameDealCate.setText(nameCateDeal);
            tvNameDealCate.setSelected(true);


            if (mDealObj.getDiscount_type() == null || mDealObj.getCategory_id() == Integer.parseInt(DealCateObj.LABOR)||mDealObj.getDiscount_type() != null && mDealObj.getDiscount_type().isEmpty()) {
                lblDealPrice.setText(String.format(self.getString(R.string.dollar_value), StringUtil.convertNumberToString(mDealObj.getPrice(), 1)));
                lblDiscount.setVisibility(View.GONE);
                lblSalePercent.setVisibility(View.GONE);
            } else if (mDealObj.getDiscount_type() != null && mDealObj.getDiscount_type().equals(Constants.AMOUNT)) {

                if (mDealObj.getDiscount_price() != 0) {
                    lblDealPrice.setText(String.format(self.getString(R.string.dollar_value),
                            StringUtil.convertNumberToString(mDealObj.getPrice() - mDealObj.getDiscount_price(), 1)));
                    lblDiscount.setText(String.format(self.getString(R.string.dollar_value),
                            StringUtil.convertNumberToString(mDealObj.getPrice(), 1)));
                    percent = percentPriceOldAndPriceSale(mDealObj.getPrice(), mDealObj.getDiscount_price() );
                    lblSalePercent.setText(percent + " %");
                    lblDiscount.setVisibility(View.VISIBLE);
                    lblSalePercent.setVisibility(View.VISIBLE);

                } else {
                    lblDealPrice.setText(String.format(self.getString(R.string.dollar_value), StringUtil.convertNumberToString(mDealObj.getPrice(), 1)));
                    lblDiscount.setVisibility(View.GONE);
                    lblSalePercent.setVisibility(View.GONE);

                }
            } else if (mDealObj.getDiscount_type() != null && mDealObj.getDiscount_type().equals(Constants.PERCENT)) {
                if (mDealObj.getDiscount_rate() > 0) {
                    lblDealPrice.setText(String.format(self.getString(R.string.dollar_value),
                            StringUtil.convertNumberToString(mDealObj.getSale_price(), 1)));
                    lblDiscount.setText(String.format(self.getString(R.string.dollar_value),
                            StringUtil.convertNumberToString(mDealObj.getPrice(), 1)));
                    percent = percentPriceOldAndPriceNew(mDealObj.getPrice(), mDealObj.getSale_price());
                    lblSalePercent.setText(percent + " %");
                    lblDiscount.setVisibility(View.VISIBLE);
                    lblSalePercent.setVisibility(View.VISIBLE);
                } else {
                    lblDealPrice.setText(String.format(self.getString(R.string.dollar_value), StringUtil.convertNumberToString(mDealObj.getPrice(), 1)));
                    lblDiscount.setVisibility(View.GONE);
                    lblSalePercent.setVisibility(View.GONE);
                }
            }

            if (mDealObj.getRateQuantity() > 0) {
                ratingBar.setVisibility(View.VISIBLE);
                lblRateQuantity.setVisibility(View.VISIBLE);

                ratingBar.setRating(mDealObj.getRate());
                lblRateQuantity.setText(String.valueOf(mDealObj.getRateQuantity()));
            } else {
                ratingBar.setVisibility(View.GONE);
                lblRateQuantity.setVisibility(View.GONE);
            }

            if (mDealObj.getRateQuantity() > 0) {
                ratingBar.setVisibility(View.VISIBLE);
                lblRateQuantity.setVisibility(View.VISIBLE);

                ratingBar.setRating(mDealObj.getRate());
                lblRateQuantity.setText(String.valueOf(mDealObj.getRateQuantity()));
            } else {
                ratingBar.setVisibility(View.GONE);
                lblRateQuantity.setVisibility(View.GONE);
            }
        }
    }

    private String getNameCate(){
        categoryId = String.valueOf(mDealObj.getCategory_id());
        if (categoryId.equals(DealCateObj.FOOD_AND_BEVERAGES))
            return getString(R.string.food_and_beverages);
        else if (categoryId.equals(DealCateObj.LABOR))
            return getString(R.string.labor);
        else if (categoryId.equals(DealCateObj.TRAVEL))
            return getString(R.string.travel_hotel);
        else if (categoryId.equals(DealCateObj.SHOPPING))
            return getString(R.string.shopping);
        else
            return getString(R.string.other_deals);
    }

    @Override
    protected void getData() {

    }
}
