package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.DealCateObj;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.objects.DriverObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.SettingsObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.FileUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.LocationService;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.view.fragments.DealNewFragment;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuuSoft.com on 11/18/2017.
 */

public class NewDealActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = DealNewFragment.class.getSimpleName();
    public static final int RC_CAMERA_PERMISSION = 1;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1221;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1323;
    private static final int RC_GET_ADDRESS = 2;
    private static final int TIME_ACTIVE = 12;

    private static final int REQUEST_CODE_SELECT_ATTACHMENT = 151;

    private static final int REQUEST_CODE_BECOME_PRO = 14;
    private static final int REQUEST_CODE_BECOME_ONLY_PRO = 15;

    private TextView lblFormProduct;
    private EditText edtPrice, edtDiscount, edtDescription, edtName, edtAddress;
    private Spinner spnTimming, spnCategory;
    private RadioButton rbStandard, rbPremium;
    private TextView tvTotal, btnSubmit, tvPercent, tvCurency, btnCancel;
    private TextViewRegular lblPrice;
    private ImageView imgChoose, imgDeal;
    private AppCompatCheckBox chkRenew;
    private TextView btnAttachment, lblFile, lblTitle;


    private RelativeLayout rltParentChoseImage;
    private RelativeLayout rltParentAttachFile;
    private RelativeLayout rltParentTitle;
    private RelativeLayout rltParentTotal;
    private View vDividerDiscount;
    private RelativeLayout rltDiscount;
    private View vDividerAddress;
    private RelativeLayout rltParentAddress;
    private View vDividerDescription;
    private LinearLayout llParentDescription;
    private View vDividerDuration;
    private RelativeLayout rltDuration;
    private View vDividerTypeDeal;
    private LinearLayout llParentTypeDeal;
    private LinearLayout llParentContent;

    private LinearLayout llRoot;

    private int typeDeal;
    private String timming = "1";
    private boolean isPercenChecked;
    private LatLng latLng;
    private String categoryIdChecked = "";
    private List<DealCateObj> categorieDatas;
    private List<String> hours;
    private List<String> categories;
    private File fileAttached;
    private String pathImage = "", pathAttachFile = "";

    private ArrayAdapter<String> adapterTimming;
    private ArrayAdapter<String> adapterCategory;
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            resetFormOutCategory();

            categoryIdChecked = categorieDatas.get(position).getId();
            if (categoryIdChecked.equals(DealCateObj.TRANSPORT)) {
                showContentView(View.GONE);
                setViewWhenJobDealSelected(false);
                UserObj user = DataStoreManager.getUser();
                if (user.getDriverData() == null) {
                    processNotDriverSelected();
                } else {
                    processADriverSelected();
                }
                lblPrice.setText(getString(R.string.fee_one_hour_online));
                edtPrice.setHint(getString(R.string.fee));
            } else if (categoryIdChecked.equals(DealCateObj.LABOR)) {
                showContentView(View.VISIBLE);
                setViewWhenJobDealSelected(true);
                edtName.requestFocus();
                lblPrice.setText(getString(R.string.original_price));
            } else {
                showContentView(View.VISIBLE);
                setViewWhenJobDealSelected(false);
                edtName.requestFocus();
                edtPrice.setEnabled(true);
                edtPrice.setText("");
                lblPrice.setText(getString(R.string.original_price));
            }
            if (position == 0) {
                showContentView(View.GONE);
                edtAddress.setOnFocusChangeListener(null);
            } else {
                edtAddress.setOnFocusChangeListener(listenerFocusAddress);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private int totalPrice;

    private SettingsObj settingUtitlityObj;

    private String discount_type;


    private View.OnFocusChangeListener listenerFocusAddress = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus) {
                MapsUtil.getAutoCompletePlaces(self, RC_GET_ADDRESS);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Toolbar toolbar;

    @Override
    void inflateLayout() {
        setContentView(R.layout.fragment_deal_new);
    }

    @Override
    void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setTitle(R.string.new_deal);

        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtDiscount = (EditText) findViewById(R.id.edt_discount);
        //tvAddress = (TextView)    findViewById(R.id.tv_address);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        spnTimming = (Spinner) findViewById(R.id.spn_timming);
        spnCategory = (Spinner) findViewById(R.id.spn_title);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        imgChoose = (ImageView) findViewById(R.id.img_chose_image);
        rbStandard = (RadioButton) findViewById(R.id.rb_standard);
        rbPremium = (RadioButton) findViewById(R.id.rb_premium);
        btnSubmit = (TextView) findViewById(R.id.tv_submit);
        imgDeal = (ImageView) findViewById(R.id.img_deal);
        chkRenew = (AppCompatCheckBox) findViewById(R.id.chk_renew);
        edtName = (EditText) findViewById(R.id.edt_name);
        tvCurency = (TextView) findViewById(R.id.tv_curency);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        btnAttachment = (TextView) findViewById(R.id.tv_attachment);
        lblFormProduct = (TextView) findViewById(R.id.lbl_form_product);
        llParentContent = (LinearLayout) findViewById(R.id.ll_parent_content);
        rltParentChoseImage = (RelativeLayout) findViewById(R.id.rlt_parent_chose_image);
        rltParentAttachFile = (RelativeLayout) findViewById(R.id.rlt_parent_attach_file);
        rltParentTitle = (RelativeLayout) findViewById(R.id.rlt_parent_title);
        rltParentAddress = (RelativeLayout) findViewById(R.id.rlt_parent_address);
        rltDiscount = (RelativeLayout) findViewById(R.id.rlt_parent_discount);
        rltParentTotal = (RelativeLayout) findViewById(R.id.rlt_parent_total);
        llParentDescription = (LinearLayout) findViewById(R.id.ll_parent_description);
        rltDuration = (RelativeLayout) findViewById(R.id.rlt_parent_duration);
        llParentTypeDeal = (LinearLayout) findViewById(R.id.rg_type_deal);
        vDividerAddress = findViewById(R.id.divider_address);
        vDividerDescription = findViewById(R.id.divider_description);
        vDividerDiscount = findViewById(R.id.divider_discount);
        vDividerDuration = findViewById(R.id.divider_duration);
        vDividerTypeDeal = findViewById(R.id.divider_type_deal);
        btnCancel = (TextView) findViewById(R.id.tv_cancel);
        lblFile = (TextView) findViewById(R.id.lbl_file);
        lblTitle = (TextView) findViewById(R.id.lbl_product);
        lblPrice = (TextViewRegular) findViewById(R.id.lbl_price);

        llRoot = (LinearLayout) findViewById(R.id.ll_root);


        edtName.requestFocus();

        initControl();
        showContentView(View.GONE);

        isPercenChecked = true;
        hours = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            hours.add(i + "");
        }

        categorieDatas = new ArrayList<>();
        categorieDatas.addAll(AppController.getInstance().getListCateForCreateNewDeals());
//        categorieDatas.remove(1);
//        categorieDatas.remove(7);
//        categorieDatas.remove(6);
//        categorieDatas.remove(0);
//        DealCateObj dealCateObj = new DealCateObj(DealCateObj.OTHER, getString(R.string.other_deal), getString(R.string.des_other));
//        categorieDatas.add(0, new DealCateObj("", getString(R.string.chose_category)));
//        categorieDatas.add(dealCateObj);
        categories = new ArrayList<>();
        for (DealCateObj item : categorieDatas) {
            categories.add(item.getName());
        }
    }

    @Override
    void initControl() {
        imgChoose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        //tvAddress.setOnClickListener(this);
        tvPercent.setOnClickListener(this);
        tvCurency.setOnClickListener(this);
        spnTimming.setOnItemSelectedListener(this);
        rbPremium.setOnCheckedChangeListener(this);
        rbStandard.setOnCheckedChangeListener(this);
        btnAttachment.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        edtAddress.setTag(edtAddress.getKeyListener());
        edtAddress.setKeyListener(null);
        // edtAddress.setOnClickListener(this);
//        edtAddress.setOnFocusChangeListener(listenerFocusAddress);


        getData();
    }

    private void getData() {
        if (hours != null && categorieDatas != null) {
            if (adapterTimming == null) {
                adapterTimming = new ArrayAdapter<>(self, R.layout.simple_spinner_item, hours);
                adapterTimming.setDropDownViewResource(R.layout.simple_spinner_item);
                spnTimming.setAdapter(adapterTimming);
            }

            // spinner category
            if (adapterCategory == null) {
                adapterCategory = new ArrayAdapter<>(self, R.layout.simple_spinner_item, categories);
                adapterCategory.setDropDownViewResource(R.layout.simple_spinner_item);
                spnCategory.setAdapter(adapterCategory);
                spnCategory.setSelection(0, false);
                spnCategory.setOnItemSelectedListener(onItemSelectedListener);

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtil.captureImage(self, REQUEST_CODE_CAPTURE_IMAGE);
                } else {
                    showPermissionReminder();
                }
            }
        }
    }

    private void showPermissionReminder() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_remind_user_grant_camera_permission),
                getString(R.string.allow), getString(R.string.no), true, new IConfirmation() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPositive() {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                    }

                    @Override
                    public void onNegative() {
                    }
                });
    }

    private void showContentView(int isVisible) {
        llParentContent.setVisibility(isVisible);
        lblFormProduct.setVisibility(isVisible);
        rltParentChoseImage.setVisibility(isVisible);
        rltParentAttachFile.setVisibility(isVisible);
        rltParentTitle.setVisibility(isVisible);
        rltParentAddress.setVisibility(isVisible);
        rltDiscount.setVisibility(isVisible);
        llParentDescription.setVisibility(isVisible);
        rltDuration.setVisibility(isVisible);
        llParentTypeDeal.setVisibility(isVisible);
        vDividerAddress.setVisibility(isVisible);
        vDividerDescription.setVisibility(isVisible);
        vDividerDiscount.setVisibility(isVisible);
        vDividerDuration.setVisibility(isVisible);
        vDividerTypeDeal.setVisibility(isVisible);
        btnCancel.setVisibility(View.GONE);
        rltParentTotal.setVisibility(isVisible);
        chkRenew.setVisibility(isVisible);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void resetSpinerCategory() {
//        spnCategory.setOnItemSelectedListener(null);
        spnCategory.setSelection(0, false);
//        spnCategory.setOnItemSelectedListener(onItemSelectedListener);
//        showContentView(View.GONE);
    }

    private void processNotDriverSelected() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.you_are_not_driver), getString(R.string.yes), getString(R.string.no),
                true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        startActivityForResult(new Intent(self, BecomeAProActivity.class), REQUEST_CODE_BECOME_PRO);
                    }

                    @Override
                    public void onNegative() {
                        resetSpinerCategory();
                        AppUtil.showToast(self, R.string.msg_register_driver);
                    }
                });
    }

    private void processADriverSelected() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.do_you_want_to_start_driving_or_delivering_now),
                getString(R.string.yes), getString(R.string.no), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        GlobalFunctions.showConfirmationDialog(self, getString(R.string.once_you_click_the_confirm_button_your_deal_will_be_active_for_12_hours),
                                getString(R.string.confirm), getString(R.string.canceled), true, new IConfirmation() {
                                    @Override
                                    public void onPositive() {
                                        settingUtitlityObj = DataStoreManager.getSettingUtility();
                                        if (settingUtitlityObj == null) {
                                            ModelManager.getSettingUtility(self, new ModelManagerListener() {
                                                @Override
                                                public void onSuccess(Object object) {
                                                    org.json.JSONObject jsonObject = (JSONObject) object;
                                                    ApiResponse apiResponse = new ApiResponse(jsonObject);
                                                    if (!apiResponse.isError()) {
                                                        settingUtitlityObj = apiResponse.getDataObject(SettingsObj.class);
                                                        if (settingUtitlityObj != null) {
                                                            edtPrice.setText(settingUtitlityObj.getDriver_online_rate());
                                                            edtPrice.setEnabled(false);
                                                            llParentContent.setVisibility(View.VISIBLE);
                                                            btnCancel.setVisibility(View.VISIBLE);
                                                            vDividerDiscount.setVisibility(View.GONE);
                                                            rltDiscount.setVisibility(View.GONE);
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                        } else {
                                            edtPrice.setText(settingUtitlityObj.getDriver_online_rate());
                                            edtPrice.setEnabled(false);
                                            llParentContent.setVisibility(View.VISIBLE);
                                            rltDiscount.setVisibility(View.GONE);
                                            vDividerDiscount.setVisibility(View.GONE);
                                            vDividerAddress.setVisibility(View.GONE);
                                            btnCancel.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onNegative() {
                                        resetSpinerCategory();
                                    }
                                });
                    }

                    @Override
                    public void onNegative() {
                        resetSpinerCategory();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == -1) {
                if (data.getData() != null){
                    AppUtil.setImageFromUri(imgDeal, data.getData());
                   // pathImage = data.getData().getPath();
                    pathImage = Environment.getExternalStorageDirectory().getPath() + "/Download/download.jpg";
                    Log.e("path image", pathImage);
                }

            }
        } else if (requestCode == RC_GET_ADDRESS) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(self, data);
                latLng = place.getLatLng();
                AppUtil.fillAddress(self, edtAddress, place);
                edtDescription.requestFocus();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(self, data);
                Log.e("DealNewFragment", status.getStatusMessage());
                Toast.makeText(self, "error", Toast.LENGTH_SHORT).show();
            } else if (resultCode == 0) {
                edtDescription.requestFocus();
                // The user canceled the operation.
            }
        } else if (requestCode == REQUEST_CODE_SELECT_ATTACHMENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    String filePath = FileUtil.getPath(self, uri);
                    pathAttachFile = filePath;
                    Log.e("path attach", pathAttachFile);
                    String extention = MimeTypeMap.getFileExtensionFromUrl(filePath);
                    if (extention.equals("jpg") || extention.equals("png")) {
                        if (filePath != null) {
                            fileAttached = new File(filePath);
                            if (fileAttached != null) {
                                if (fileAttached.length() > FileUtil.MAX_FILE_SIZE) {
                                    Toast.makeText(self, getString(R.string.file_is_max), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                btnAttachment.setText(fileAttached.getName());
                            }
                        }
                    } else {
                        Toast.makeText(self, getString(R.string.msg_file_is_not_format), Toast.LENGTH_LONG).show();
                    }

                }

            }

        } else if (requestCode == REQUEST_CODE_BECOME_PRO) {
            if (resultCode == BecomeAProActivity.RESULT_CODE_BECOME_PRO) {
                if (DataStoreManager.getUser().getDriverData() != null) {
                    processADriverSelected();
                } else {
                    resetSpinerCategory();
                }
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                Bitmap bitmap = ImageUtil.decodeBitmapFromBitmap(photo, 300, 300);
                imgDeal.setImageBitmap(bitmap);
            } else {
                AppUtil.setImageFromUri(imgDeal, data.getData());
            }

        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnSubmit) {
            submit();
        } else if (view == imgChoose) {
            GlobalFunctions.showConfirmationDialog(self, getString(R.string.title_chose_image), getString(R.string.from_gallery), getString(R.string.from_camera),
                    true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            AppUtil.pickImage(self, REQUEST_CODE_SELECT_IMAGE);
                        }

                        @Override
                        public void onNegative() {
                            if (GlobalFunctions.isMarshmallow()) {
                                if (self.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    AppUtil.captureImage(self, REQUEST_CODE_CAPTURE_IMAGE);
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                                }
                            } else {
                                AppUtil.captureImage(self, REQUEST_CODE_CAPTURE_IMAGE);
                            }
                        }
                    });

        } else if (view == edtAddress) {
            MapsUtil.getAutoCompletePlaces(this, RC_GET_ADDRESS);
        } else if (view == tvPercent) {
            setStateForButton(true);
        } else if (view == tvCurency) {
            setStateForButton(false);
        } else if (view == btnAttachment) {
            //AppUtil.showFileChooser(this, REQUEST_CODE_SELECT_ATTACHMENT);
            AppUtil.pickImage(self, REQUEST_CODE_SELECT_ATTACHMENT);
        } else if (view == btnCancel) {
            llParentContent.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            resetSpinerCategory();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtil.hideSoftKeyboard((Activity) self);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                               long l) {
        timming = adapterView.getItemAtPosition(position).toString();
        getTotalPrice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        if (b) {
            if (id == R.id.rb_premium) {
                typeDeal = 1;
                getTotalPrice();
                rbStandard.setChecked(false);
            } else {
                typeDeal = 0;
                getTotalPrice();
                rbPremium.setChecked(false);
            }
        }
    }


    private void setStateForButton(boolean isPercent) {
        if (isPercent) {
            isPercenChecked = true;
            tvPercent.setTextColor(AppUtil.getColor(self, R.color.white));
            tvPercent.setBackgroundResource(R.drawable.bg_primary_radius_left);
            tvCurency.setTextColor(AppUtil.getColor(self, R.color.textColorPrimary));
            tvCurency.setBackgroundResource(R.drawable.bg_white_radius_right);
        } else {
            isPercenChecked = false;
            tvPercent.setTextColor(AppUtil.getColor(self, R.color.textColorPrimary));
            tvPercent.setBackgroundResource(R.drawable.bg_white_radius_left);
            tvCurency.setTextColor(AppUtil.getColor(self, R.color.white));
            tvCurency.setBackgroundResource(R.drawable.bg_primary_radius_right);
        }
    }

    private void submit() {
        if (categoryIdChecked.equals(DealCateObj.TRANSPORT)) {
            if (settingUtitlityObj != null) {
                int price = Integer.parseInt(settingUtitlityObj.getDriver_online_rate());
                if (price * TIME_ACTIVE < DataStoreManager.getUser().getBalance()) {
                    ModelManager.activateDriverMode(self, Constants.DURATION_BUYING, TIME_ACTIVE, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            org.json.JSONObject jsonObject = (JSONObject) object;
                            ApiResponse apiResponse = new ApiResponse(jsonObject);
                            if (!apiResponse.isError()) {
                                LocationService.start(self, LocationService.REQUEST_LOCATION);

                                String notify = String.format(getString(R.string.msg_deal_create_success), DateTimeUtil.getEndAtFromNow(TIME_ACTIVE));
                                Toast.makeText(self, notify, Toast.LENGTH_LONG).show();
                                llParentContent.setVisibility(View.GONE);
                                btnCancel.setVisibility(View.GONE);

                                UserObj userObj = DataStoreManager.getUser();
                                userObj.getDriverData().setAvailable(DriverObj.DRIVER_AVAILABLE);
                                DataStoreManager.saveUser(userObj);

                                gotoChat("", "");
//                                spnCategory.setSelection(0);
                                resetSpinerCategory();

                            } else {
                                Toast.makeText(self, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } else {
                    showDialogBuyCredit();
                }
            }

        } else {
            UserObj user = DataStoreManager.getUser();
            if (user.getProData() != null) {
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString().trim().replaceAll(",", "");
                String discount = edtDiscount.getText().toString().trim().replaceAll(",", "");
                String address = edtAddress.getText().toString();
                String description = edtDescription.getText().toString();

                int renew = chkRenew.isChecked() ? 1 : 0;

                if (categoryIdChecked.isEmpty()) {
                    Toast.makeText(self, getString(R.string.chose_category), Toast.LENGTH_LONG).show();
                    return;
                }

                if (StringUtil.isEmpty(name)) {
                    Toast.makeText(self, getString(R.string.title_required), Toast.LENGTH_LONG).show();
                    return;
                }

                if (StringUtil.isEmpty(price)) {
                    Toast.makeText(self, getString(R.string.price_required), Toast.LENGTH_LONG).show();
                    return;
                }

                if (Integer.parseInt(price) == 0) {
                    Toast.makeText(self, getString(R.string.msg_price_is_zero), Toast.LENGTH_LONG).show();
                    return;
                }

                if (latLng == null || address.isEmpty()) {
                    Toast.makeText(self, getString(R.string.address_is_required), Toast.LENGTH_LONG).show();
                    return;
                }

                if (StringUtil.isEmpty(discount)) {
                    discount = "0";
                }

                float discountValue;
                float check_discount;
                float priceValue = Float.parseFloat(price);

                if (isPercenChecked) {
                    discount_type = Constants.PERCENT;
                    check_discount = priceValue * Float.parseFloat(discount) / 100;
                } else {
                    discount_type = Constants.AMOUNT;
                    check_discount = Float.parseFloat(discount);
                }
                discountValue = Float.parseFloat(discount);
                if (check_discount >= priceValue) {
                    Toast.makeText(self, getString(R.string.msg_discount_need_smaller_price), Toast.LENGTH_LONG).show();
                    return;
                }

                DataPart image = null, file = null;
                if (imgDeal.getDrawable() != null && imgDeal.getDrawable() instanceof BitmapDrawable) {
                    image = new DataPart("deal.png", AppUtil.getFileDataFromDrawable(self, imgDeal.getDrawable()), DataPart.TYPE_IMAGE);
                } else {
                    Toast.makeText(self, getString(R.string.msg_chose_image_deal), Toast.LENGTH_LONG).show();
                    return;
                }

                if (fileAttached != null) {
                    file = new DataPart(fileAttached.getName(), FileUtil.read(fileAttached), "file");
                }

                if (user.getBalance() < totalPrice) {
                    showDialogBuyCredit();
                    return;
                }

                ModelManager.createDeal(self, categoryIdChecked, null, name, price, discount_type, discountValue, address, timming, description, latLng.latitude,
                        latLng.longitude, typeDeal, renew, image, file, new ModelManagerListener() {
                            @Override
                            public void onSuccess(Object object) {
                                Log.e("createdeal", object.toString());
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(object.toString());
                                    ApiResponse response = new ApiResponse(jsonObject);
                                    if (!response.isError()) {
                                        UserObj user = DataStoreManager.getUser();
                                        user.setBalance(user.getBalance() - totalPrice);
                                        DataStoreManager.saveUser(user);

                                        String notify = String.format(getString(R.string.msg_deal_create_success), DateTimeUtil.getEndAtFromNow(Integer.parseInt(timming)));
                                        Toast.makeText(self, notify, Toast.LENGTH_LONG).show();
                                        resetForm();

                                        DealObj dealObj = response.getDataObject(DealObj.class);
                                        showDialogSuccess(dealObj);

                                    } else {

                                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                    e.getStackTrace();
                                }

                            }

                            @Override
                            public void onError() {
                                Toast.makeText(self, "onError", Toast.LENGTH_SHORT).show();
                            }
                        });
//                File fileobj  = new File(pathImage);
//                if (fileobj.exists()){
//                    Log.e("eeeeeeeeeeeeee","da co");
//                }else{
//                    Log.e("eeeeeeeeeeeeee","ko co file");
//
//                }
//
//                RequestManger.createDeal(self, categoryIdChecked, null, name, price, discount_type, discountValue, address, timming, description, latLng.latitude,
//                        latLng.longitude, typeDeal, renew, pathImage, pathAttachFile, new BaseRequest.CompleteListener() {
//
//                            @Override
//                            public void onSuccess(com.suusoft.ecommerce.network.ApiResponse response) {
//                                JSONObject jsonObject = null;
//                                if (!response.isError()) {
//                                    UserObj user = DataStoreManager.getUser();
//                                    user.setBalance(user.getBalance() - totalPrice);
//                                    DataStoreManager.saveUser(user);
//
//                                    String notify = String.format(getString(R.string.msg_deal_create_success), DateTimeUtil.getEndAtFromNow(Integer.parseInt(timming)));
//                                    Toast.makeText(self, notify, Toast.LENGTH_LONG).show();
//                                    resetForm();
//
//                                    DealObj dealObj = response.getDataObject(DealObj.class);
//                                    showDialogSuccess(dealObj);
//
//                                } else {
//
//                                    Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onError(String message) {
//
//                            }
//                        });

            } else {
                showDialogBecomePro();
            }

        }

    }

    private void showDialogSuccess(final DealObj dealObj) {
        final Dialog dialogSuccess = DialogUtil.setDialogCustomView(self, R.layout.dialog_success, false);
        TextView btnCreateAnother = (TextView) dialogSuccess.findViewById(R.id.btn_create_another);
        TextView btnViewYourDeal = (TextView) dialogSuccess.findViewById(R.id.btn_view_deal);
        dialogSuccess.show();

        btnCreateAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.dismiss();
                resetFormOutCategory();
            }
        });

        btnViewYourDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.dismiss();
//                DealObj dealObj = null;
//
//                String dealId = "";String dealName ="";
//                try {
//                    dealObj = new GsonBuilder().create().fromJson(jsonObject.getJSONObject("data").toString(),DealObj.class);
//                    dealObj = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), DealObj.class);
//                    dealId = jsonObject.getJSONObject("data").getString("id");
//                    dealName = jsonObject.getJSONObject("data").getString("name");
//                    Log.e(TAG, "showDialogSuccess jsonObject " + jsonObject.toString());
//                    Log.e(TAG, "showDialogSuccess dealObj " + new Gson().toJson(dealObj).toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                gotoChat(dealObj);
//                gotoChat(dealId, dealName);
            }
        });

    }

    private void gotoChat(DealObj dealObj) {
        RecentChatObj recentChatObj;
        if (categoryIdChecked.equals(DealCateObj.TRANSPORT)) {
            UserObj userObj = DataStoreManager.getUser();
            userObj.getDriverData().setAvailable(DriverObj.DRIVER_AVAILABLE);
            DataStoreManager.saveUser(userObj);

            TransportDealObj transportDealObj = new TransportDealObj();
            transportDealObj.setDriverId(DataStoreManager.getUser().getId());
            recentChatObj = new RecentChatObj(transportDealObj, null, null);
        } else {
            DealObj dealObj1 = dealObj;
            dealObj1.setSeller_id(DataStoreManager.getUser().getId());
            dealObj1.setIs_online(DealObj.DEAL_ACTIVE);
            recentChatObj = new RecentChatObj(null, dealObj1, null);
        }

        ChatActivityReskin2.start(self, null, recentChatObj);
        Log.e(TAG, "gotoChat mRecentChatObj " + new Gson().toJson(recentChatObj));
    }

    private void gotoChat(String dealId, String dealName) {
        RecentChatObj recentChatObj;
        if (categoryIdChecked.equals(DealCateObj.TRANSPORT)) {
            UserObj userObj = DataStoreManager.getUser();
            userObj.getDriverData().setAvailable(DriverObj.DRIVER_AVAILABLE);
            DataStoreManager.saveUser(userObj);

            TransportDealObj transportDealObj = new TransportDealObj();
            transportDealObj.setDriverId(DataStoreManager.getUser().getId());
            recentChatObj = new RecentChatObj(transportDealObj, null, null);
        } else {
            DealObj dealObj = new DealObj();
            dealObj.setId(dealId);
            dealObj.setName(dealName);
            dealObj.setSeller_id(DataStoreManager.getUser().getId());
            dealObj.setIs_online(DealObj.DEAL_ACTIVE);
            recentChatObj = new RecentChatObj(null, dealObj, null);
        }

        ChatActivityReskin2.start(self, null, recentChatObj);
        Log.e(TAG, "gotoChat mRecentChatObj " + new Gson().toJson(recentChatObj));
    }

    private void showDialogBuyCredit() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_not_enought_credits),
                getString(R.string.button_buy_credits), getString(R.string.button_promotions), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Args.IS_NEED_FINISH, true);
                        GlobalFunctions.startActivityForResult(self, BuyCreditsActivity.class, Args.RQ_BUY_CREDIT, bundle);
                    }

                    @Override
                    public void onNegative() {
                        AppUtil.showToast(self, R.string.msg_promotion_is_developping);
                    }
                });
    }


    private void showDialogBecomePro() {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.upgrade_pro_account),
                getString(R.string.become), getString(R.string.button_cancel), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Args.IS_NEED_FINISH, true);
                        startActivityForResult(new Intent(self, BecomeAProActivity.class), REQUEST_CODE_BECOME_ONLY_PRO);
                    }

                    @Override
                    public void onNegative() {
//                        AppUtil.showToast(self, R.string.msg_promotion_is_developping);
                    }
                });
    }

    private void resetForm() {
        edtPrice.setText("");
        edtDiscount.setText("");
        edtDescription.setText("");
        edtName.setText("");
        edtAddress.setText("");
        imgDeal.setImageResource(R.drawable.bg_image_default);
        resetSpinerCategory();
        btnAttachment.setText(getString(R.string.select_file));
        spnTimming.setSelection(0);
        chkRenew.setChecked(false);
        rbStandard.setChecked(true);
        fileAttached = null;
        setStateForButton(true);

    }

    private void resetFormOutCategory() {
        edtPrice.setText("");
        edtDiscount.setText("");
        edtDescription.setText("");
        edtName.setText("");
        edtAddress.setText("");
        imgDeal.setImageResource(R.drawable.bg_image_default);
        btnAttachment.setText(getString(R.string.select_file));
        spnTimming.setSelection(0);
        chkRenew.setChecked(false);
        rbStandard.setChecked(true);
        fileAttached = null;
        setStateForButton(true);
    }

    private void setViewWhenJobDealSelected(boolean isSelected) {
        edtPrice.setHint(getString(R.string.price));
        if (isSelected) {
            lblFile.setText(getString(R.string.picture_your_self));
            lblTitle.setText(getString(R.string.type_of_job));
            edtName.setHint(getString(R.string.profession));
            edtPrice.setHint(getString(R.string.my_hourly_fee));
            edtDescription.setHint(getString(R.string.newdeal_description_hint));
            rltDiscount.setVisibility(View.GONE);
            vDividerDiscount.setId(View.GONE);
            vDividerAddress.setVisibility(View.GONE);
        } else {
            lblFile.setText(getString(R.string.attachment));
            lblTitle.setText(getString(R.string.title_field));
            edtName.setHint(getString(R.string.title));
            edtPrice.setHint(getString(R.string.original_price));
            edtDescription.setHint(getString(R.string.description));
            rltDiscount.setVisibility(View.VISIBLE);
            vDividerDiscount.setId(View.VISIBLE);
            vDividerAddress.setVisibility(View.VISIBLE);

        }
    }

    private void getTotalPrice() {

        int priceOfStandard = Integer.parseInt(DataStoreManager.getSettingUtility().getDeal_online_rate());
        int priceOfPremium = Integer.parseInt(DataStoreManager.getSettingUtility().getPremium_deal_online_rate());
        if (typeDeal == 1) {
            totalPrice = Integer.parseInt(timming) * priceOfPremium;
        } else {
            totalPrice = Integer.parseInt(timming) * priceOfStandard;
        }

        tvTotal.setText(String.format(getString(R.string.value_of_total), totalPrice + ""));

    }
}
