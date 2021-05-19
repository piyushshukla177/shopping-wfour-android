package com.wfour.onlinestoreapp.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IObserver;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.MyProgressDialog;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;
import com.wfour.onlinestoreapp.retrofit.respone.ResponeUser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.FileUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.LocationService;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.view.activities.ChangePassWordActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.activities.PhoneCountryListActivity;
import com.wfour.onlinestoreapp.view.activities.SplashLoginActivity;
import com.wfour.onlinestoreapp.view.activities.ViewProfileActivity;
import com.wfour.onlinestoreapp.view.activities.ViewReviewsActivity;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MyAccountMyInfoFragment extends BaseFragment implements View.OnClickListener, IObserver {
    private static final String TAG = MyAccountMyInfoFragment.class.getName();
    private static final int RC_ADDRESS = 134;
    private static final int RQ_CHANGE_PASS = 234;
    public static final String LOG_OUT = "LOG_OUT";
    private static final String PARAM_AVATAR = "avatar";
    private EditText edtBusinessName, edtPhoneNumber, edtAddress, edtEmail;
    private CircleImageView imgAvatar;
    private ImageView imgEditAvatar;
    private ImageView imgSymbolAccount;
    private FrameLayout frDivider;
    private TextViewRegular btnEdit;
    private TextViewRegular tvChangePassword, tvNumRate, btnViewReviews ;
    AppCompatButton tvLogout;
    private TextView tvPhoneCode, tvEg;
    private RelativeLayout btnSave, bill_relative;
    private TextView tvFunction, tvPoint;
    private RatingBar rating_bar;
    private boolean isEdit = false;
    private String passWord;
    private String mAddress;
    private MultipartBody.Part partImage = null;
    private MultipartBody.Part partImage2 = null;
    private RequestBody name, email, avatar, address, phone, id;
    private File file;
    private String image = "";
    public static FragmentTransaction fragmentTransaction;

    private View.OnFocusChangeListener listenerFocusAddress = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocus) {
            if (isFocus) {
                MapsUtil.getAutoCompletePlaces(MyAccountMyInfoFragment.this, RC_ADDRESS);
            }
        }
    };

    public static MyAccountMyInfoFragment newInstance() {
        Bundle args = new Bundle();
        MyAccountMyInfoFragment fragment = new MyAccountMyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account_about, container, false);
    }

    private BillManagementFragment mBillManagementFragment;

    @Override
    void initUI(View view) {
        tvPoint = view.findViewById(R.id.tv_point);
        imgAvatar = view.findViewById(R.id.img_avatar);
        imgEditAvatar = view.findViewById(R.id.btn_edit_avatar);
        imgSymbolAccount = view.findViewById(R.id.img_symbol_account);
        tvNumRate = view.findViewById(R.id.tv_num_rate);
        rating_bar = view.findViewById(R.id.rating_bar);
        btnViewReviews = view.findViewById(R.id.btn_view_reviews);
        bill_relative = view.findViewById(R.id.bill_relative);

        edtBusinessName = view.findViewById(R.id.edt_bussiness_name);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_number);
        edtAddress = view.findViewById(R.id.edt_address);
        edtEmail = view.findViewById(R.id.edt_email);
        tvEg = view.findViewById(R.id.tvEg);

        btnEdit = view.findViewById(R.id.btn_edit_infomation);
        tvChangePassword = view.findViewById(R.id.tv_change_password);
          tvLogout =view.findViewById(R.id.tv_logout);
        //btnSave = view.findViewById(R.id.btn_functions);
        //tvFunction = view.findViewById(R.id.tv_btn);
        //btnSave.setVisibility(View.GONE);
        //tvFunction.setText(getString(R.string.button_save));

        frDivider = view.findViewById(R.id.fr_divider);
        tvPhoneCode = view.findViewById(R.id.tv_phone_code);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

//        passWord = DataStoreManager.getUser().getPassWord();
        bill_relative.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        if (DataStoreManager.getUser() != null) {
                            if (mBillManagementFragment == null) {
                                mBillManagementFragment = BillManagementFragment.newInstance();
                            }

                            fragmentTransaction.replace(R.id.frl_main, mBillManagementFragment).commit();
                            getActivity().setTitle(R.string.bill);
                        } else {
//                    AppUtil.showToast(self, "No Account");
                            showLogout();
                        }
                    }
                }
        );
        initRemoveActionBar(view);
    }

    private void showLogout() {
        DialogUtil.showAlertDialog(self, R.string.notification, R.string.you_need_login, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                goLogin();
            }
        });
    }

    public void initRemoveActionBar(View view) {
        MaterialToolbar toolbars = getActivity().findViewById(R.id.toolbar);
        toolbars.setVisibility(View.GONE);
    }

    @Override
    void initControl() {
        enableEdit(false);
        imgEditAvatar.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        btnViewReviews.setOnClickListener(this);
        //btnSave.setOnClickListener(this);
        tvPhoneCode.setOnClickListener(this);
//        edtAddress.setTag(edtAddress.getKeyListener());
////        edtAddress.setKeyListener(null);
//        edtAddress.setOnClickListener(this);
//        edtAddress.setOnFocusChangeListener(listenerFocusAddress);

        frDivider.setVisibility(View.VISIBLE);


        setData(DataStoreManager.getUser());
        edtEmail.setEnabled(false);
        edtBusinessName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    edtEmail.setFocusable(false);
                    edtPhoneNumber.requestFocus();
                }
                return false;
            }
        });
        if (com.wfour.onlinestoreapp.network1.NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            getProfile();
        } else {
            AppUtil.showToast(getActivity(), R.string.msg_network_not_available);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnEdit) {
            isEdit = !isEdit;
            if (DataStoreManager.getUser() != null) {
                if (isEdit) {
//                if (DataStoreManager.getUser().getAvatar().startsWith("http://iwanadeal.com") || DataStoreManager.getUser().getAvatar().isEmpty()) {
                    imgEditAvatar.setVisibility(View.VISIBLE);
//
//                } else {
//                    imgEditAvatar.setVisibility(View.GONE);
//
//                }
//                edtAddress.setOnFocusChangeListener(listenerFocusAddress);
                    //btnSave.setVisibility(View.VISIBLE);
                    btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_accent);
                    edtBusinessName.requestFocus();
                    tvEg.setVisibility(View.VISIBLE);
                } else {
                    setData(DataStoreManager.getUser());
                    imgEditAvatar.setVisibility(View.GONE);
                    //btnSave.setVisibility(View.GONE);
                    btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                    //edtAddress.setOnFocusChangeListener(null);
                    tvEg.setVisibility(View.GONE);
                }
                enableEdit(isEdit);
            } else {
                showDialogLogout();
            }
        }
        /*
        else if (view == btnSave) {
            if (com.wfour.onlinestoreapp.network1.NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
                if (isEdit) {
                    if (isValid()) {
                        submit();
//                        updateProfile();
                    }
                } else {
                    AppUtil.showToast(getActivity(), R.string.msg_enable_edit);
                }

            } else {
                AppUtil.showToast(getActivity(), R.string.msg_no_network);
            }

        }
        */
        else if (view == tvChangePassword) {
            Intent intent = new Intent(getActivity(), ChangePassWordActivity.class);
            startActivityForResult(intent, RQ_CHANGE_PASS);
        }
        else if(view == tvLogout){
            if (DataStoreManager.getUser() != null) {
                logout();
            } else {
                AppUtil.showToast(self, "No Account");
            }
        }
        else if (view == imgEditAvatar) {
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
//          AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);

        } else if (view == btnViewReviews) {
//            Bundle bundle = new Bundle();
//            bundle.putString(Args.USER_ID, "");
//            bundle.putString(Args.NAME_DRIVER,"");
//            bundle.putString(Args.I_AM, Constants.SELLER);
//            Intent intent = new Intent(getApplicationContext(), ViewReviewsActivity.class);
//            intent.putExtras(bundle);
            AppUtil.startActivity(getActivity(), ViewReviewsActivity.class);
        } else if (view == tvPhoneCode) {
            Intent intent = new Intent(getActivity(), PhoneCountryListActivity.class);
            startActivityForResult(intent, Args.RQ_GET_PHONE_CODE);
        } else if (view == edtAddress) {
            MapsUtil.getAutoCompletePlaces(this, RC_ADDRESS);
        }
    }


    private void showDialogLogout() {
        DialogUtil.showAlertDialog(self, R.string.notification, R.string.you_need_login, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                goLogin();
            }


        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(self, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void goLogin() {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.LOG_OUT, "logout");
        AppUtil.startActivity(self, SplashLoginActivity.class, bundle);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", "resultCode = " + resultCode);
        if (requestCode == RQ_CHANGE_PASS) {
            if (resultCode == Activity.RESULT_OK) {
                String pass = data.getStringExtra(ChangePassWordActivity.PASS);
                UserObj userObj = DataStoreManager.getUser();
                userObj.setPassWord(pass);
                DataStoreManager.saveUser(userObj);

            }
        }
        if (requestCode == AppUtil.PICK_IMAGE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                AppUtil.setImageFromUri(imgAvatar, data.getData());
                image = FileUtil.getPath(self, data.getData()).trim();
                Log.e(TAG, "onActivityResult: " + image);
                file = new File(image);
                parseMultipart();
            }
        } else if (requestCode == Args.RQ_GET_PHONE_CODE && resultCode == Activity.RESULT_OK) {
            String countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
            tvPhoneCode.setText(countryCodeSelected);
        }


    }

    private boolean isValid() {
        String bussinessName = edtBusinessName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        if (StringUtil.isEmpty(bussinessName)) {
            AppUtil.showToast(getActivity(), R.string.msg_fill_full_name);
            edtBusinessName.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(phone)) {
            AppUtil.showToast(getActivity(), R.string.msg_phone_is_required);
            edtPhoneNumber.requestFocus();
            return false;
        }
        if (StringUtil.isEmpty(address)) {
            AppUtil.showToast(getActivity(), R.string.msg_address_is_required);
            edtAddress.requestFocus();
            return false;
        }
        return true;
    }

    private void enableEdit(boolean isEdit) {
        edtBusinessName.setEnabled(isEdit);
        edtAddress.setEnabled(isEdit);
        edtPhoneNumber.setEnabled(isEdit);
        tvPhoneCode.setEnabled(isEdit);
    }

    private void setData(UserObj userObj) {
        if (DataStoreManager.getUser() != null) {
            if (userObj.getProData() == null) {
                imgSymbolAccount.setVisibility(View.GONE);
                imgSymbolAccount.setImageResource(R.drawable.ic_member);
//            tvNumRate.setText(String.valueOf(userObj.getRate_count()));
//            rating_bar.setRating(userObj.getRate());
            } else {
                imgSymbolAccount.setVisibility(View.GONE);
                imgSymbolAccount.setImageResource(R.drawable.ic_pro_member);
//            tvNumRate.setText(String.valueOf(userObj.getProData().getRateCount()));
//            rating_bar.setRating(userObj.getProData().getRate());
            }

            tvNumRate.setText(String.valueOf(userObj.getTotal_rate_count()));
            rating_bar.setRating(userObj.getAvg_rate());
            ImageUtil.setImage(getActivity(), imgAvatar, userObj.getAvatar(), 600, 600);
            edtBusinessName.setText(userObj.getName());
            edtPhoneNumber.setText(userObj.getPhoneNumber());
            tvPoint.setText(getString(R.string.point) + StringUtil.convertNumberToString(DataStoreManager.getUser().getPoint(), 0));

            if (userObj.getPhoneCode().isEmpty()) {
                getCountryCode();
            } else {
                tvPhoneCode.setText(userObj.getPhoneCode());
            }
            edtAddress.setText(userObj.getAddress());
            edtEmail.setText(userObj.getEmail());
//        if (userObj.isSecured()) {
            tvChangePassword.setText(getString(R.string.change_pass));
//        } else {
//            tvChangePassword.setText(getString(R.string.create_pass));
//        }
        }
    }


    private void getCountryCode() {
        String[] rl = getResources().getStringArray(R.array.CountryCodes);
        // int curPosition = AppUtil.getCurentPositionCountryCode(getActivity());
        int curPosition = 0;
        String phoneCode = rl[curPosition].split(",")[0];
        tvPhoneCode.setText(phoneCode);
    }

    private void parseMultipart() {
        if (!image.isEmpty()) {
            File fileImage1 = new File(image);
            RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage1);
            partImage = MultipartBody.Part.createFormData(PARAM_AVATAR, fileImage1.getName(), fileReqBody1);
        }
    }

    private void save() {
        MyProgressDialog myProgressDialog = new MyProgressDialog(self);
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
        if (file != null) {
            ApiUtils.getAPIService().updateProfile(id, name, address, partImage, phone, email).enqueue(new Callback<ResponeUser>() {
                @Override
                public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                    myProgressDialog.cancel();
                    if (response.body().isSuccess(self)) {
                        if (response.body() != null) {
                            UserObj userObj = response.body().getData();
                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                            enableEdit(false);
                            ImageUtil.setImage(getActivity(), MainActivity.imgAvatar, userObj.getAvatar());
                            //MainActivity.tvName.setText(userObj.getName());
                            //MainActivity.tvMoney.setText(getString(R.string.point) + StringUtil.convertNumberToString(DataStoreManager.getUser().getPoint(), 0));
                            btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                            isEdit = false;
                            //btnSave.setVisibility(View.GONE);
                            imgEditAvatar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeUser> call, Throwable t) {
                    myProgressDialog.cancel();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            ApiUtils.getAPIService().updateProfile(id, name, address, avatar, phone, email).enqueue(new Callback<ResponeUser>() {
                @Override
                public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                    myProgressDialog.cancel();
                    if (response.body().isSuccess(self)) {
                        if (response.body() != null) {
                            UserObj userObj = response.body().getData();
                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                            enableEdit(false);
                            ImageUtil.setImage(getActivity(), MainActivity.imgAvatar, userObj.getAvatar());
                            //MainActivity.tvName.setText(userObj.getName());
                            //MainActivity.tvMoney.setText(getString(R.string.point) + StringUtil.convertNumberToString(DataStoreManager.getUser().getPoint(), 0));
                            btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                            isEdit = false;
                            //btnSave.setVisibility(View.GONE);
                            imgEditAvatar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponeUser> call, Throwable t) {
                    myProgressDialog.cancel();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

        }
    }


    private void submit() {
        String sid = DataStoreManager.getUser().getId();
        String savartar = DataStoreManager.getUser().getAvatar();
        String semail = DataStoreManager.getUser().getEmail();
        String sname = edtBusinessName.getText().toString().trim();
        String saddress = edtAddress.getText().toString().trim();
        String sphone = edtPhoneNumber.getText().toString().trim() + " " + tvPhoneCode.getText().toString().trim();

        id = RequestBody.create(MediaType.parse("text/plain"), sid);
        name = RequestBody.create(MediaType.parse("text/plain"), sname);
        address = RequestBody.create(MediaType.parse("text/plain"), saddress);
        avatar = RequestBody.create(MediaType.parse("text/plain"), savartar);
        phone = RequestBody.create(MediaType.parse("text/plain"), sphone);
        email = RequestBody.create(MediaType.parse("text/plain"), semail);
        save();
    }

    private void updateProfile() {
        String id = DataStoreManager.getUser().getId();
        final String passWord = DataStoreManager.getUser().getPassWord();
        String email = DataStoreManager.getUser().getEmail();
        final String bussinessName = edtBusinessName.getText().toString().trim();
        final String address = edtAddress.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String phoneCode = tvPhoneCode.getText().toString().trim();
        final String phone = phoneCode + " " + phoneNumber;
        DataPart avatar = null;
        if (imgAvatar.getDrawable() != null) {
            avatar = new DataPart("avatar.png", AppUtil.getFileDataFromDrawable(getActivity(), imgAvatar.getDrawable()), DataPart.TYPE_IMAGE);
        }

        ModelManager.updateProfile(self, "", id, bussinessName, address, phone, email, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    ApiResponse response = new ApiResponse(jsonObject);
                    Log.e("hihi", " 2" + response.toString());

                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        //userObj.setToken(DataStoreManager.getUser().getToken());
                        //userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                        userObj.setName(bussinessName);
                        userObj.setAddress(address);
                        userObj.setPhone(phone);
                        userObj.setPassWord(passWord);
                        DataStoreManager.saveUser(userObj);
                        AppController.getInstance().setUserUpdated(true);
                        enableEdit(false);
                        btnEdit.setBackgroundResource(R.drawable.bg_pressed_radius_grey);
                        isEdit = false;
                        //btnSave.setVisibility(View.GONE);
                        imgEditAvatar.setVisibility(View.GONE);
                        AppUtil.showToast(getActivity(), R.string.msg_update_success);
                        setData(userObj);
                        getActivity().setTitle(userObj.getName());
                        ((MainActivity) getActivity()).updateMenuLeftHeader();

                        AddressManager.getInstance().getArray().clear();
                        AddressManager.getInstance().addItem(new Person(bussinessName, phone, address));
                        Log.e("hihi", "3: " + userObj.toString());
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

    private void getProfile() {
        if (DataStoreManager.getUser() != null) {
            String id = DataStoreManager.getUser().getId();
            final String passWord = DataStoreManager.getUser().getPassWord();

            String address = DataStoreManager.getUser().getAddress();
            String phone = DataStoreManager.getUser().getPhone();
            String name = DataStoreManager.getUser().getName();

            ModelManager.getProfile(getActivity(), id, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        ApiResponse response = new ApiResponse(jsonObject);

                        if (!response.isError()) {
                            UserObj userObj = response.getDataObject(UserObj.class);
                            userObj.setToken(DataStoreManager.getUser().getToken());
                            userObj.setPassWord(passWord);
                            userObj.setRememberMe(DataStoreManager.getUser().isRememberMe());
                            DataStoreManager.saveUser(userObj);
                            AppController.getInstance().setUserUpdated(true);
                            setData(userObj);
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
    }


    @Override
    public void update() {
        setData(DataStoreManager.getUser());
    }

    private void logout() {
        showDialogLogout2();
    }

    private void showDialogLogout2() {
        DialogUtil.showAlertDialog(self, R.string.log_out, R.string.you_wanto_logout, new DialogUtil.IDialogConfirm() {
            @Override
            public void onClickOk() {
                requestLogout();
            }
        });
    }

    private void requestLogout() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            final MyProgressDialog progressDialog = new MyProgressDialog(self);
            progressDialog.show();
            processBeforeLoggingOut(progressDialog);

        } else {
            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void processBeforeLoggingOut(MyProgressDialog progressDialog) {
        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getDriverData() != null) {
            if (DataStoreManager.getUser().getDriverData().isAvailable()) {
                LocationService.start(self, LocationService.STOP_REQUESTING_LOCATION);

                // Deactivate driver's mode before logging out
                ModelManager.activateDriverMode(self, Constants.OFF, 0, new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        DataStoreManager.removeUser();
        AddressManager.getInstance().getArray().clear();
        AppController.getInstance().setUserUpdated(true);
        Bundle bundle = new Bundle();
        bundle.putString(LOG_OUT, "logout");
//        AppUtil.startActivity(self, SplashLoginActivity.class, bundle);
//        finish();
        //ImageUtil.setImage(self, imgAvatar, null);
        imgAvatar.setImageDrawable(null);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GlobalFunctions.updateBalance event) {
        getProfile();
    }
    public  HomeFragment mHomeFragment;

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.nav_main);
                    if (mBottomNavigationView != null) {

                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        if (mHomeFragment == null) {
                            mHomeFragment = HomeFragment.newInstance(Args.TYPE_OF_CATEGORY_ALLS);
                        }
                        fragmentTransaction.replace(R.id.frl_main, mHomeFragment).commit();

                        mBottomNavigationView.setSelectedItemId(R.id.home_menu);
                    } else {
                        Intent intent = new Intent(self, MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
