package com.wfour.onlinestoreapp.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.activities.ChatActivityReskin2;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.ViewUtil;

/**
 * Created by Suusoft on 11/24/2016.
 */

public class DealAboutFragment extends BaseFragment implements View.OnClickListener {

    private static final int RC_ACTIVATE_DEAL = 1;
    private static final String TAG = DealAboutFragment.class.getSimpleName();

    private ImageView imgDeal, imgFavorite;
    private TextView tvAddress, tvFavoriteCount, tvPrice, tvOldPrice, tvName, btnDeal, tvAbout, tvFileName, tvNumReviews, tvEndTime, btnBuy;
    private RatingBar rbDeal;
    private ProductObj item;
    private LinearLayout llParentTime;
    public static final String TITLE = "TITLE";
    public static final String PRICE = "PRICE";
    public static final String IMAGE = "IMAGE";
    private int count =0;

    public static DealAboutFragment newInstance(ProductObj item) {
        Bundle args = new Bundle();
        DealAboutFragment fragment = new DealAboutFragment();
        fragment.item = item;
        fragment.setArguments(args);
        return fragment;

    }
    public  interface Communication{
        public void Passdata(String data);
    }
    Communication mCommunucation;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_deal_about;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        imgDeal = (ImageView) view.findViewById(R.id.img_deal);
        tvPrice = (TextView) view.findViewById(R.id.lbl_price);
        rbDeal = (RatingBar) view.findViewById(R.id.rating);
        tvName = view.findViewById(R.id.tvName);
        tvPrice = view.findViewById(R.id.tvPrice);
        ViewUtil.setRatingbarColor(rbDeal);
    }

    @Override
    protected void getData() {
        if (item != null) {
            ImageUtil.setImage(self, imgDeal, item.getImage());
            Log.d("hahahaha", "newInstance: "+ item.getPrice());
            tvName.setText(item.getTitle());
            tvPrice.setText(String.valueOf(item.getPrice()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_ACTIVATE_DEAL) {
            if (resultCode == Activity.RESULT_OK) {
                // Update deal status(active or inactive) after coming back from Chat screen
                if (item != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        if (bundle.containsKey(Args.IS_ACTIVATED_DEAL)) {
                            //item.setIs_online(bundle.getBoolean(Args.IS_ACTIVATED_DEAL) ? DealObj.DEAL_ACTIVE : DealObj.DEAL_INACTIVE);

                            // Refresh deal object in parent activity
                            //((DealDetailActivity) getActivity()).getItem().setIs_online(bundle.getBoolean(Args.IS_ACTIVATED_DEAL) ? DealObj.DEAL_ACTIVE : DealObj.DEAL_INACTIVE);
                            //((DealDetailActivity) getActivity()).updateOptionsMenu();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnBuy) {
//        } else if (view == tvAddress) {
//            gotoMap();
////        }
////        else if (view == imgFavorite) {
////            favorite();
//        } else if (view == tvFileName) {
//            showFile();
        }
    }

    private void gotoChatForResult(RecentChatObj obj) {
        //Log.e(TAG, "gotoChatForResult mRecentChatObj " + new Gson().toJson(obj));
        Intent intent = new Intent(self, ChatActivityReskin2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Args.RECENT_CHAT_OBJ, obj);
        startActivityForResult(intent, RC_ACTIVATE_DEAL);
    }


    public void setData() {

    }


}
