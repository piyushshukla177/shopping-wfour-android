package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.wfour.onlinestoreapp.widgets.textview.TextViewCondensedItalic;
import com.wfour.onlinestoreapp.PacketUtility;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.UserObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class SplashLoginActivity extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SplashLoginActivity.class.getSimpleName();

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final int RC_PERMISSIONS = 1;

    private TextView mLblLogin;
    private TextView mLblSignUp;
    private TextViewCondensedItalic tvGuide;

    private LinearLayout mLblFacebook, mLblGoogle;

    private GoogleApiClient mGoogleApiClient;

    private CallbackManager mCallbackManager;

    private RequestQueue mRequestQueue;

    private View mClickedView; // Keep button which was just clicked(google, facebook or login)

    private Bundle mBundle;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestQueue = Volley.newRequestQueue(self);
        getData();

        if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
            if (DataStoreManager.getSettingUtility() == null)
                getSettingUtility();
        }
        initGoogleApiClient();
        // Init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Init mint lib
        //Mint.initAndStartSession(this.getApplication(), "914400ea");
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_splash_reskin);
    }

    @Override
    void initUI() {
        // Hide actionbar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mLblGoogle = findViewById(R.id.lbl_google_login);
        mLblFacebook = findViewById(R.id.lbl_facebook_login);
        mLblLogin = (TextView) findViewById(R.id.lbl_login);
        mLblSignUp = (TextView) findViewById(R.id.lbl_signup);
//        tvGuide = (TextViewCondensedItalic) findViewById(R.id.tv_guide);

    }

    @Override
    void initControl() {
        mLblGoogle.setOnClickListener(this);
        mLblFacebook.setOnClickListener(this);
        mLblLogin.setOnClickListener(this);
        mLblSignUp.setOnClickListener(this);
//        tvGuide.setOnClickListener(this);
        Log.e("EE", "EE: SPLASH");
        checkLogin();
    }

    @Override
    protected void getExtraValues() {
        mBundle = getIntent().getExtras();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mBundle = intent.getExtras();
        Log.e("EE", "EE: NEW IN SPLASH");
        checkLogin();
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
            }
        }

    }
    private void getData (){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            text = bundle.getString(MainActivity.LOG_OUT);
        }
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
                            Bundle bundle = new Bundle();

                            bundle.putString(MainActivity.LOG_OUT, text);
                            GlobalFunctions.startActivityWithoutAnimation(self, LoginActivity.class, bundle);
                            finish();
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

        if (v == mLblGoogle) {
            mClickedView = mLblGoogle;

            // Check permissions
            if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS, "")) {
                signInGoogle();
            }
        } else if (v == mLblFacebook) {
            mClickedView = mLblFacebook;

            // Check permissions
            if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS, "")) {
                signInFacebook();
            }
        } else if (v == mLblLogin) {

            mClickedView = mLblLogin;

            // Check permissions
//            if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS, "")) {
            if (mBundle != null) {
                GlobalFunctions.startActivityWithoutAnimation(self, LoginActivity.class, mBundle);
            } else {
                GlobalFunctions.startActivityWithoutAnimation(self, LoginActivity.class);
            }
            finish();
//            }
        } else if (v == mLblSignUp) {
            GlobalFunctions.startActivityWithoutAnimation(self, SignUpActivity.class);
            finish();
        }
//        else if (v == tvGuide) {
//            GlobalFunctions.startActivityWithoutAnimation(self, GuideActivity.class);
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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

            if (!email.equals("")) {
                login(name, email, "", avatar, UserObj.SOCIAL, PacketUtility.getImei(self));
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
                                login(name, email, "", avatar, UserObj.SOCIAL, PacketUtility.getImei(self));
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

    private void login(String name, String email, String password, String avatar, final String loginMethod, String imei) {
        Log.e("Splash", "login");
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {
            ModelManager.login(self, mRequestQueue, email, password, name, avatar, loginMethod, imei, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    Log.e("Splash", "Login onSuccess");
                    JSONObject jsonObject = (JSONObject) object;
                    ApiResponse response = new ApiResponse(jsonObject);
                    if (!response.isError()) {
                        UserObj userObj = response.getDataObject(UserObj.class);
                        userObj.setToken(response.getValueFromRoot(Args.TOKEN));
                        // It's remember default if users login as social
                        userObj.setRememberMe(true);

                        // Save qbId for user if it's the first time
//                        if (userObj.getQb_id() == 0) {
//                            if (SharedPreferencesUtil.hasQbUser()) {
//                                userObj.setQb_id(SharedPreferencesUtil.getQbUser().getId());
////                                userObj.setQb_id(80813);
//                            }
//                        }

                        // Save user to preference
                        userObj.setRememberMe(false);
                        DataStoreManager.saveUser(userObj);

                        gotoHomePage();

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
        if (mBundle == null) {
            mBundle = new Bundle();
        }

//        Log.e("EEEE", "EE: " + mBundle.getString(Args.KEY_ID_DEAL));
        GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class, mBundle);
        Log.e("Splash", "gotoHomePage");
        // Close this activity
        finish();
    }

    private void checkLogin() {
        // check login and remember
        if (DataStoreManager.getUser() != null && DataStoreManager.getUser().getToken() != null) {
            if (!DataStoreManager.getUser().getToken().equals("")) {
                // Check permissions
//                if (GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.ACCESS_FINE_LOCATION}, RC_PERMISSIONS, "")) {

                gotoHomePage();
//                }
            }
        }
    }

    private void getSettingUtility() {
        ModelManager.getSettingUtility(this, new ModelManagerListener() {
            @Override
            public void onSuccess(Object object) {
                org.json.JSONObject jsonObject = (JSONObject) object;
                ApiResponse apiResponse = new ApiResponse(jsonObject);
                if (!apiResponse.isError()) {
                    DataStoreManager.saveSettingUtility(jsonObject.toString());
                } else {
                    Log.e(TAG, "GET SETTING UTILITY ERROR!");
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "GET SETTING UTILITY ERROR!");
            }
        });
    }

    @Override
    public void onBackPressed() {
        gotoHomePage();
        // super.onBackPressed();
    }
}