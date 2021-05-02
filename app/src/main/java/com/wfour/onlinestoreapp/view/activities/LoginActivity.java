package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.wfour.onlinestoreapp.PacketUtility;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;
import com.wfour.onlinestoreapp.retrofit.respone.ResponeUser;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewCondensedItalic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final int RC_PERMISSIONS = 1;

    private LinearLayout layoutScreen;
    private EditText mTxtEmail;
    private ShowHidePasswordEditText mTxtPassword;
    private TextView mLblLogin, mLblSignUp, mLblGoogle, mLblFacebook;
    private TextView mLblForget, mLblNoAccount;
    private CheckBox mChkRememberMe;
    private GoogleApiClient mGoogleApiClient;

    private CallbackManager mCallbackManager;
    private RequestQueue mRequestQueue;
    private UserObj obj;
    private String text;

    private String mEmail = "", mPassword = ""; // This vars save credential from 'SignUpActivity.java'
    private boolean isFromSignUp = false;
    private Bundle bundle;

    private View mClickedView; // Keep button which was just clicked(google, facebook or login)
    private String countryCodeSelected = "";
    private TextView tvConfirm, tvPhoneCode;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(self);
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(self, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(this, this)
                .build();
        // [END build_client]
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_login_reskin);
    }

    @Override
    protected void getExtraValues() {
        super.getExtraValues();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Args.EMAIL)) {
                mEmail = bundle.getString(Args.EMAIL);
                text = bundle.getString(MainActivity.LOG_OUT);
            }
            if (bundle.containsKey(Args.PASSWORD)) {
                mPassword = bundle.getString(Args.PASSWORD);
            }
            if (bundle.containsKey(Args.IS_FROM_SIGNUP)) {
                isFromSignUp = true;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bundle = intent.getExtras();
    }

    @Override
    void initUI() {
        // Hide actionbar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        layoutScreen = findViewById(R.id.layout_screen);
        mTxtEmail = findViewById(R.id.txt_email);
        mTxtPassword = findViewById(R.id.txt_password);
        mLblLogin = findViewById(R.id.lbl_login);
        mLblForget = findViewById(R.id.lbl_forget_password);
        mLblSignUp = findViewById(R.id.lbl_signup);
//        mLblNoAccount = findViewById(R.id.lbl_no_account_yet);
        mChkRememberMe = findViewById(R.id.chk_remember_me);

        layoutScreen.getLayoutParams().height = AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this);
        Log.e("screen", layoutScreen.getLayoutParams().height + "px");
        Log.e("screen heightStatusBar", AppUtil.getStatusBarHeight(this) + "px");

        // Fill credential automatically
        if (isFromSignUp) {
            if (DataStoreManager.getUser() != null && DataStoreManager.getUser().isRememberMe()) {
                mTxtEmail.setText(mEmail);
                mTxtPassword.setText(mPassword);
            }
        } else {
            if (DataStoreManager.getUser() != null && DataStoreManager.getUser().isRememberMe()) {
                if (!DataStoreManager.getUser().getEmail().isEmpty()) {
                    mTxtEmail.setText(DataStoreManager.getUser().getEmail());
                    Log.e("EE", "EEE");
                }
                Log.e("EE", "EEE");
            }
        }
    }

    @Override
    void initControl() {
        mLblLogin.setOnClickListener(this);
//        mLblFacebook.setOnClickListener(this);
//        mLblGoogle.setOnClickListener(this);
        mLblForget.setOnClickListener(this);
        mLblSignUp.setOnClickListener(this);
//        mLblNoAccount.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        GlobalFunctions.startActivityWithoutAnimation(self, SplashLoginActivity.class);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RC_PERMISSIONS: {
                if (grantResults.length == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED
                            || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        showPermissionsReminder(RC_PERMISSIONS, true);
                    } else {
                        if (mClickedView == mLblGoogle) {
                            signInGoogle();
                        } else if (mClickedView == mLblFacebook) {
                            signInFacebook();
                        } else if (mClickedView == mLblLogin) {
                            loginUser();
                        }
                    }

                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mLblLogin) {
            mClickedView = mLblLogin;
            if (isValid()) {
                // Check permissions
                if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE}, RC_PERMISSIONS, "")) {
                    loginUser();
                }
                //loginUser();
            }
        } else if (v == mLblForget) {
            showDialogForgot();
        }
//        else if (v == mLblNoAccount) {
//            GlobalFunctions.startActivityWithoutAnimation(self, SignUpActivity.class);
//            finish();
//        }
        else if (v == mLblGoogle) {
            mClickedView = mLblGoogle;

            // Check permissions
            if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS, "")) {
                signInGoogle();
            }
        } else if (v == mLblFacebook) {
            mClickedView = mLblFacebook;

            // Check permissions
            if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS, "")) {
                signInFacebook();
            }
        }
        else if (v == mLblSignUp) {
            GlobalFunctions.startActivityWithoutAnimation(self, SignUpActivity.class);
            finish();
        }
    }

    private void signInGoogle() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            // Sign out before signing in again
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                            }
                        });

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            } else {
                mGoogleApiClient.connect();
            }
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_GOOGLE_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleGoogleSignInResult(result);
            } else if (requestCode == Args.RQ_GET_PHONE_CODE) {
                countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
                tvPhoneCode.setText(countryCodeSelected);
            } else if (requestCode == Args.RQ_SMS) {
                updateProfile();
            }
        }
    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String email = "", name = "", avatar = "";
            if (acct.getEmail() != null) {
                email = acct.getEmail();
            }
            if (acct.getDisplayName() != null) {
                name = acct.getDisplayName();
            }
            if (acct.getPhotoUrl() != null) {
                avatar = acct.getPhotoUrl().toString();
            }

            if (!email.equals("") && !name.equals("")) {
                login(name, email, "", avatar);
            } else {
                Toast.makeText(self, R.string.msg_can_not_get_email, Toast.LENGTH_LONG).show();
            }
        }
    }


    private void signInFacebook() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            mCallbackManager = CallbackManager.Factory.create();

            if (AccessToken.getCurrentAccessToken() != null) {
                // Sign out then sign in again
                LoginManager.getInstance().logOut();
            }

            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    String accessToken = loginResult.getAccessToken().getToken();
                    ModelManager.getFacebookInfo(self, mRequestQueue, accessToken, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            Log.e("Splash", "onSuccess login fb1  obj -- " + object.toString());
                            JSONObject jsonObject = (JSONObject) object;

                            String email = jsonObject.optString("email");
                            String name = jsonObject.optString("name");
                            String avatar = "";
                            try {
                                avatar = jsonObject.getJSONObject("picture").getJSONObject("data").optString("url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!email.equals("")) {
                                Log.e("Splash", "onSuccess login fb2");
                                login(name, email, "", avatar);
                            } else {
                                Toast.makeText(self, R.string.msg_can_not_get_email, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError() {
                            Log.e("Splash", "onError login fb");
                            Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {
                    Log.e("Splash", "Facebook Login cancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e("Splash", "Facebook Login error");

                }
            });
            //  LoginManager.getInstance().setLoginBehavior(LoginBehavior.SSO_WITH_FALLBACK);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        String email = mTxtEmail.getText().toString().trim();
        String password = mTxtPassword.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(self, R.string.msg_email_is_required, Toast.LENGTH_SHORT).show();
            mTxtEmail.requestFocus();

            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(self, R.string.msg_password_is_required, Toast.LENGTH_SHORT).show();
            mTxtPassword.requestFocus();

            return false;
        }

        return true;
    }

    private void loginUser() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            String email = mTxtEmail.getText().toString().trim();
            final String password = mTxtPassword.getText().toString().trim();
            String imei = PacketUtility.getImei(self);

            Log.e(TAG, " imei " + imei);
            ModelManager.loginUser(self, "", email, UserObj.NORMAL,  "","1", null, password, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);

                    Log.e("hihi", new Gson().toJson(response));
                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        //userObj.setToken(response.getValueFromRoot(Args.TOKEN));
//                        userObj.setRememberMe(mChkRememberMe.isChecked());
                        userObj.setPassWord(password);
                        DataStoreManager.saveUser(userObj);
                        DataStoreManager.saveToken(response.getLogin_token());
                        mTxtEmail.setText(userObj.getEmail());
                        AddressManager.getInstance().getArray().clear();
                        AddressManager.getInstance().addItem(new Person(userObj.getName(), userObj.getPhone(), userObj.getAddress()));
                        // }

                        // Save qbId for user if it's the first time
                        userObj.setRememberMe(false);

                        gotoHomePage();
                    } else {
                        Toast.makeText(self, response.getMessageFromCode(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onError() {
                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void login(final String name, String email, String password, final String avatar) {
        Log.e("Splash", "login");
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.loginUser(self, name, email, UserObj.SOCIAL, avatar, "1", null, password, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    Log.e("Splash", "Login onSuccess");
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        obj = response.getDataObject(UserObj.class);
                        // It's remember default if users login as social
                        obj.setRememberMe(true);
                        if (obj.getPhoneNumber().equals("") || obj.getPhoneNumber() == null) {
                            Log.e(TAG, "onSuccess: không có phone");
                            showDialogPhone();
                        } else {
                            DataStoreManager.saveUser(obj);
                            gotoHomePage();
                        }
                    } else {
                        Log.e("Splash", "response onError");
                        Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                    Log.e("Splash", "Login onError");
                    Toast.makeText(self, R.string.msg_have_some_errors, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoHomePage() {
        if (bundle != null) {
            if (text != null) {
                bundle.putString(MainActivity.LOG_OUT, "logout");
                GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class, bundle);
            }
            GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class, bundle);
        } else {
            GlobalFunctions.startActivityWithoutAnimation(self, CartActivity.class);
        }


        // Close this activity
        finish();
    }

    private void showDialogForgot() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);

        final EditText edtEmail = dialog.findViewById(R.id.txt_email);
        TextView btnSubmit = dialog.findViewById(R.id.tv_submit);
        final TextView tvError = dialog.findViewById(R.id.tv_error);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                if (!StringUtil.isValidEmail(email)) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.msg_email_is_required));
                    return;
                }
                requestForgotPasswork(email);
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void requestForgotPasswork(String email) {
        ModelManager.forgotPassword(this, email, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                JSONObject jsonObject = (JSONObject) object;
                ApiResponse response = new ApiResponse(jsonObject);
                if (!response.isError()) {
                    Toast.makeText(self, getString(R.string.msg_forget_password_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void showDialogPhone() {
        Dialog dialog = DialogUtil.setDialogCustomView(self, R.layout.dialog_updatephone, true);
        tvConfirm = dialog.findViewById(R.id.tv_confirm);
        tvPhoneCode = dialog.findViewById(R.id.tv_phone_code);
        EditText editText = dialog.findViewById(R.id.txt_phone);
        tvPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalFunctions.startActivityForResult(self, PhoneCountryListActivity.class, Args.RQ_GET_PHONE_CODE);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "Your number phone is not blank!", Toast.LENGTH_SHORT).show();
                } else {
                    phone = tvPhoneCode.getText() + editText.getText().toString().replaceAll("\\s+", "");
                    if (!Patterns.PHONE.matcher(phone).matches()) {
                        AppUtil.showToast(self, R.string.validation_req_phone_valid);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(self);
                        builder.setTitle(phone);
                        builder.setMessage(R.string.confirm_phone);
                        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", phone);
                            GlobalFunctions.startActivityForResult(self, VerificationActivity.class, Args.RQ_SMS, bundle);
                            //AppUtil.startActivity(self, VerificationActivity.class, bundle);
                        });
                        builder.setNegativeButton(R.string.edit, (dialogInterface, i) -> {
//                        edUser.requestFocus();
//                        AppUtil.showKeyboard();
                            dialogInterface.dismiss();
                        });
                        builder.create().show();
                    }
                }
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    private void updateProfile() {

        ApiUtils.getAPIService().updateProfile(obj.getId(), obj.getName(), obj.getAddress(),obj.getAvatar(), phone, obj.getId()).enqueue(new Callback<ResponeUser>() {
            @Override
            public void onResponse(Call<ResponeUser> call, Response<ResponeUser> response) {
                if (response.body() != null) {
                    Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
                    if (response.body().getData() != null) {
                        DataStoreManager.saveToken(response.body().getData().getToken());
                        DataStoreManager.saveUser(response.body().getData());
                        gotoHomePage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponeUser> call, Throwable t) {
                gotoHomePage();
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }
}
