package com.wfour.onlinestoreapp.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.view.fragments.ProDriverFragment;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Suusoft on 08/12/2016.
 */

public class PassengerViewDriverActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PASSENGER_VIEW_DRIVER = 123;
    private TabLayout tabLayout;
    private FrameLayout fr_content;
    private TextViewBold btnIwanaRide;
    private ImageView btnEditImage;
    private ImageView imgSymbolAccount;
    private TextViewRegular btnReviews;
    private TextViewRegular tvNumRate;
    private CircleImageView imgAvatar;
    private UserObj userObj;
    private String idDriver;
    private RatingBar rating_bar;
    private TextViewRegular tvAddress;

    private RecentChatObj mRecentChatObj;
    private Bundle bundle;

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_passenger_view_driver);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Args.RECENT_CHAT_OBJ)) {
                mRecentChatObj = bundle.getParcelable(Args.RECENT_CHAT_OBJ);
                if (com.wfour.onlinestoreapp.network1.NetworkUtility.getInstance(getApplicationContext()).isNetworkAvailable()) {
                    getProfileDriver(mRecentChatObj.getTransportDealObj().getDriverId());
                } else {
                    AppUtil.showToast(this, R.string.msg_network_not_available);
                }

            }
        }
    }

    @Override
    void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fr_content = (FrameLayout) findViewById(R.id.fr_content);
        btnIwanaRide = (TextViewBold) findViewById(R.id.btn_iwana_ride);
        btnEditImage = (ImageView) findViewById(R.id.btn_edit_avatar);
        btnReviews = (TextViewRegular) findViewById(R.id.btn_view_reviews);
        tvNumRate = (TextViewRegular) findViewById(R.id.tv_num_rate);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        tvAddress = (TextViewRegular) findViewById(R.id.tv_address);
        //imgSymbolAccount = (ImageView) findViewById(R.id.img_symbol_account);

        btnEditImage.setVisibility(View.GONE);

        if (mRecentChatObj != null) {
            if (mRecentChatObj.getTransportDealObj() != null) {
                if (mRecentChatObj.getTransportDealObj().getTransportType().equals(TransportObj.DELIVERY)) {
                    btnIwanaRide.setText(R.string.iwanadelivery);
                } else {
                    btnIwanaRide.setText(R.string.iwanaride);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    void initControl() {
        btnIwanaRide.setOnClickListener(this);
        btnReviews.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnIwanaRide) {
            openChat();
        } else if (view == btnReviews) {
            Bundle bundle = new Bundle();
            bundle.putString(Args.USER_ID, mRecentChatObj.getTransportDealObj().getDriverId());
            bundle.putString(Args.NAME_DRIVER, mRecentChatObj.getTransportDealObj().getDriverName());
            bundle.putString(Args.I_AM, Constants.PASSENGER);
            Intent intent = new Intent(getApplicationContext(), ViewReviewsActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_PASSENGER_VIEW_DRIVER);
        } else if (view == imgAvatar) {
            showImageLarge();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PASSENGER_VIEW_DRIVER) {
            if (resultCode == ViewReviewsActivity.RESULT_CODE_VIEW_REVIEWS) {
                AppUtil.startActivity(this, PassengerViewDriverActivity.class, bundle);
                finish();
            }
        }
    }

    private void showImageLarge() {
        if (userObj != null && userObj.getAvatar() != null) {
            Dialog builder = new Dialog(self);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView imageView = new ImageView(self);
            ImageUtil.setImage(self, imageView, userObj.getAvatar());
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            if (!builder.isShowing())
                builder.show();
        }

    }

    private void openChat() {
        if (mRecentChatObj != null) {
            if (mRecentChatObj.getTransportDealObj() != null) {
                if (mRecentChatObj.getTransportDealObj().getLatLngDestination() != null) {
                    RecentChatsActivity.start(self, mRecentChatObj);
                } else {
                    Toast.makeText(self, R.string.msg_choose_destination, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(self, R.string.msg_choose_destination, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpTabLayout() {
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.me)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pro)));
        Bundle bundle = new Bundle();
        bundle.putParcelable(UserObj.DATA_USER, userObj);
        addFragmentContent(ProDriverFragment.newInstance(bundle));
//        addFragmentContent(GeneralInformationFragment.newInstance(bundle));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (!tab.getText().equals(getString(R.string.pro))) {
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(UserObj.DATA_USER, userObj);
//                    addFragmentContent(GeneralInformationFragment.newInstance(bundle));
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(UserObj.DATA_USER, userObj);
//                    addFragmentContent(ProDriverFragment.newInstance(bundle));
//                }
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    private void addFragmentContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_content, fragment).commit();
    }

    private void getProfileDriver(String idDriver) {
        ModelManager.getProfileDriver(this, idDriver, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        imgSymbolAccount.setVisibility(View.VISIBLE);
                        userObj = response.getDataObject(UserObj.class);
                        if (userObj != null) {
                            if (userObj.getProData() == null) {
                                imgSymbolAccount.setImageResource(R.drawable.ic_member);
                            } else {
                                imgSymbolAccount.setImageResource(R.drawable.ic_pro_member);
                            }
                            setData(userObj);
                            setUpTabLayout();
                        }

                    } else {
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setData(UserObj userObj) {
        setTitle(userObj.getName());
        tvAddress.setText(userObj.getAddress());
        if (userObj.getProData() == null) {
            tvNumRate.setText(String.valueOf(userObj.getRate_count()));
            rating_bar.setRating(userObj.getRate());

        } else {
//            setToolbarTitle(userObj.getProData().getname());
            tvNumRate.setText(String.valueOf(userObj.getProData().getRateCount()));
            rating_bar.setRating(userObj.getProData().getRate());
//            tvAddress.setText(userObj.getProData().getBusiness_address());
        }
        ImageUtil.setImage(this, imgAvatar, userObj.getAvatar(), 300, 300);
    }
}
