package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.*;
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
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.FileUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 10/01/2017.
 */

public class UpdateDealActivity extends com.wfour.onlinestoreapp.base.BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_CAMERA_PERMISSION = 1;

    public static final int REQUEST_CODE_SELECT_IMAGE = 1221;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1323;
    private static final int RC_GET_ADDRESS = 2;


    private static final int REQUEST_CODE_SELECT_ATTACHMENT = 151;


    private EditText edtPrice, edtDiscount, edtDescription, edtName, edtAddress;
    private Spinner spnTimming, spnCategory;
    private RadioButton rbStandard, rbPremium;
    private TextView tvTotal, btnUpdate, tvPercent, tvCurency, btnCancel, tvTimeOut;
    private TextViewRegular lblPrice;
    private ImageView imgChoose, imgDeal;
    private AppCompatCheckBox chkRenew;
    private TextView btnAttachment, lblFile, lblTitle;


    private View vDividerDiscount;
    private RelativeLayout rltDiscount;
    private View vDividerAddress;


    private int typeDeal;
    private String timming = "1";
    private boolean isPercenChecked;
    private LatLng latLng;
    private String categoryIdChecked = "";
    private List<DealCateObj> categorieDatas;
    private List<String> hours;
    private List<String> categories;
    private File fileAttached;

    private ArrayAdapter<String> adapterTimming;
    private ArrayAdapter<String> adapterCategory;

    private int totalPrice;

    private String discount_type;

    private DealObj item;

    private View.OnFocusChangeListener listenerFocusAddress = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus) {
                MapsUtil.getAutoCompletePlaces(UpdateDealActivity.this, RC_GET_ADDRESS);
            }
        }
    };

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_update_deal;
    }

    @Override
    protected void getExtraData(Intent intent) {
        item = intent.getExtras().getParcelable(Args.KEY_PRODUCT_OBJECT);
        if (item == null) {
            finish();
        }
    }

    @Override
    protected void initilize() {
        isPercenChecked = true;
        hours = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            hours.add(i + "");
        }

        categorieDatas = new ArrayList<>();
        categorieDatas.addAll(AppController.getInstance().getListCateForCreateNewDeals());
//        categorieDatas.remove(6);
//        categorieDatas.remove(0);
//        DealCateObj dealCateObj = new DealCateObj(DealCateObj.OTHER, getString(R.string.other_deal), getString(R.string.des_other));
//        categorieDatas.add(dealCateObj);
        categories = new ArrayList<>();
        for (DealCateObj item : categorieDatas) {
            categories.add(item.getName());
        }
    }

    @Override
    protected void initView() {
        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtDiscount = (EditText) findViewById(R.id.edt_discount);
        //tvAddress = (TextView) findViewById(R.id.tv_address);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        spnTimming = (Spinner) findViewById(R.id.spn_timming);
        spnCategory = (Spinner) findViewById(R.id.spn_title);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        imgChoose = (ImageView) findViewById(R.id.img_chose_image);
        rbStandard = (RadioButton) findViewById(R.id.rb_standard);
        rbPremium = (RadioButton) findViewById(R.id.rb_premium);
        btnUpdate = (TextView) findViewById(R.id.tv_submit);
        imgDeal = (ImageView) findViewById(R.id.img_deal);
        chkRenew = (AppCompatCheckBox) findViewById(R.id.chk_renew);
        edtName = (EditText) findViewById(R.id.edt_name);
        tvCurency = (TextView) findViewById(R.id.tv_curency);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        btnAttachment = (TextView) findViewById(R.id.tv_attachment);
        btnCancel = (TextView) findViewById(R.id.tv_cancel);
        rltDiscount = (RelativeLayout) findViewById(R.id.rlt_parent_discount);
        vDividerAddress = findViewById(R.id.divider_address);
        vDividerDiscount = findViewById(R.id.divider_discount);
        lblFile = (TextView) findViewById(R.id.lbl_file);
        lblTitle = (TextView) findViewById(R.id.lbl_product);
        tvTimeOut = (TextView) findViewById(R.id.tv_time_out);
        edtName.requestFocus();

    }

    @Override
    protected void onViewCreated() {
        imgChoose.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
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
        edtAddress.setOnFocusChangeListener(listenerFocusAddress);

        initData();
        if (item != null) {
            setToolbarTitle(item.getName());
            setData(item);
        }

    }

    private void initData() {
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
                spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        categoryIdChecked = categorieDatas.get(position).getId();
                        if (categoryIdChecked.equals(DealCateObj.LABOR)) {
                            setViewWhenJobDealSelected(true);
                        } else {
                            setViewWhenJobDealSelected(false);
                            edtPrice.setEnabled(true);
//                            edtPrice.setText("");
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnUpdate) {
            submit();
        } else if (view == imgChoose) {
            GlobalFunctions.showConfirmationDialog(self, getString(R.string.title_chose_image),
                    getString(R.string.from_gallery), getString(R.string.from_camera), true, new IConfirmation() {
                        @Override
                        public void onPositive() {
                            AppUtil.pickImage(UpdateDealActivity.this, REQUEST_CODE_SELECT_IMAGE);
                        }

                        @Override
                        public void onNegative() {
                            if (GlobalFunctions.isMarshmallow()) {
                                if (self.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    AppUtil.captureImage(UpdateDealActivity.this, REQUEST_CODE_CAPTURE_IMAGE);
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
                                }
                            } else {
                                AppUtil.captureImage(UpdateDealActivity.this, REQUEST_CODE_CAPTURE_IMAGE);
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
            AppUtil.pickImage(this, REQUEST_CODE_SELECT_ATTACHMENT);
        } else if (view == btnCancel) {
            finish();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        timming = adapterView.getItemAtPosition(i).toString();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppUtil.captureImage(UpdateDealActivity.this, REQUEST_CODE_CAPTURE_IMAGE);
                } else {
                    showPermissionReminder();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == -1) {
                if (data.getData() != null)
                    AppUtil.setImageFromUri(imgDeal, data.getData());
            }
        } else if (requestCode == RC_GET_ADDRESS) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(self, data);
                latLng = place.getLatLng();
                AppUtil.fillAddress(this, edtAddress, place);
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

        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                Bitmap bitmap = ImageUtil.decodeBitmapFromBitmap(photo, 300, 300);
                imgDeal.setImageBitmap(bitmap);
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

    private void submit() {

        UserObj user = DataStoreManager.getUser();
        if (user.getProData() != null) {
            String name = edtName.getText().toString();
            String price = edtPrice.getText().toString().trim().replaceAll(",","");
            String discount = edtDiscount.getText().toString().trim().replaceAll(",","");
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

            if (Float.parseFloat(price) == 0) {
                Toast.makeText(self, getString(R.string.msg_price_is_zero), Toast.LENGTH_LONG).show();
                return;
            }

//            if (latLng == null) {
//                Toast.makeText(self, getString(R.string.address_is_required), Toast.LENGTH_LONG).show();
//                return;
//            }
            if (latLng != null) {
                item.setLatitude(latLng.latitude);
                item.setLongitude(latLng.longitude);
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
            if (imgDeal.getDrawable() != null) {
                image = new DataPart("deal.png", AppUtil.getFileDataFromDrawable(this, imgDeal.getDrawable()), DataPart.TYPE_IMAGE);
            }

            if (fileAttached != null) {
                file = new DataPart(fileAttached.getName(), FileUtil.read(fileAttached), "file");
            }

            if (user.getBalance() < totalPrice) {
                showDialogBuyCredit();
                return;
            }


            ModelManager.createDeal(self, categoryIdChecked, item.getId(), name, price, discount_type, discountValue, address, null, description, item.getLatitude(),
                    item.getLongitude(), typeDeal, renew, image, file, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(object.toString());
                                ApiResponse response = new ApiResponse(jsonObject);
                                if (!response.isError()) {
                                    DealObj dealObj = response.getDataObject(DealObj.class);
//                                    UserObj user = DataStoreManager.getUser();
//                                    user.setBalance(user.getBalance() - totalPrice);
//                                    DataStoreManager.saveUser(user);

//                                    String notify = String.format(getString(R.string.msg_deal_create_success), DateTimeUtil.getEndAtFromNow(Integer.parseInt(timming)));

                                    DataStoreManager.saveUpdateDeal(true);
                                    Toast.makeText(self, getString(R.string.msg_update_success), Toast.LENGTH_LONG).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
                                    Intent intent = new Intent();
                                    intent.putExtras(bundle);
                                    setResult(Args.RC_UPDATE_DEAL, intent);

//                                    Intent intent = new Intent(getApplicationContext(), DealDetailActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putParcelable(Args.KEY_DEAL_OBJECT, dealObj);
//                                    intent.putExtras(bundle);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
                                    finish();

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
        }
    }

    private void showDialogBuyCredit() {
        GlobalFunctions.showConfirmationDialog(this, getString(R.string.msg_not_enought_credits),
                getString(R.string.button_buy_credits), getString(R.string.button_promotions), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Args.IS_NEED_FINISH, true);
                        GlobalFunctions.startActivityForResult(UpdateDealActivity.this, BuyCreditsActivity.class, Args.RQ_BUY_CREDIT, bundle);
                    }

                    @Override
                    public void onNegative() {
                        AppUtil.showToast(self, R.string.msg_promotion_is_developping);
                    }
                });
    }

    private void setData(DealObj item) {
        for (int i = 0; i < categorieDatas.size(); i++) {
            if (i > 0)
                if (item.getCategory_id() == Integer.parseInt(categorieDatas.get(i).getId())) {
                    spnCategory.setSelection(i);
                    break;
                }
        }
        btnAttachment.setText(item.getAttachment());
        edtName.setText(item.getName());
        edtPrice.setText(String.valueOf(item.getPrice()));


        edtAddress.setText(item.getAddress());
        ImageUtil.setImage(this, imgDeal, item.getImage());

        edtDescription.setText(item.getDescription());
        if (item.getOnline_duration() > 0) {

            for (int i = 0; i < hours.size(); i++) {
                if (item.getOnline_duration() == Integer.parseInt(hours.get(i))) {
                    spnTimming.setSelection(i);
                    spnTimming.setEnabled(false);
                    break;
                }
            }
            tvTimeOut.setVisibility(View.GONE);
            spnTimming.setVisibility(View.VISIBLE);
        } else {
            tvTimeOut.setVisibility(View.VISIBLE);
            spnTimming.setVisibility(View.GONE);
        }
        if (item.getIs_premium() == 0) {
            rbStandard.setChecked(true);
            rbPremium.setChecked(false);
            rbPremium.setEnabled(false);
            rbStandard.setEnabled(false);
        } else {
            rbStandard.setChecked(false);
            rbPremium.setChecked(true);
            rbPremium.setEnabled(false);
            rbStandard.setEnabled(false);
        }
        chkRenew.setChecked(item.isRenew());

        if (item.getDiscount_type().equals(Constants.PERCENT)) {
            setStateForButton(true);
            edtDiscount.setText(String.valueOf(item.getDiscount_rate()));
        } else if (item.getDiscount_type().equals(Constants.AMOUNT)) {
            setStateForButton(false);
            edtDiscount.setText(String.valueOf(item.getDiscount_price()));
        }
    }
}
