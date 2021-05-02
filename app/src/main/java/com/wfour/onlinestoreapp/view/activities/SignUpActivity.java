package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.Country;
import com.wfour.onlinestoreapp.objects.DataPart;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;
import com.wfour.onlinestoreapp.widgets.edittext.CustomEditText;
import com.wfour.onlinestoreapp.widgets.tabLayout.TabIndicatorLine;
import com.wfour.onlinestoreapp.widgets.textview.TextViewCondensedItalic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = SignUpActivity.class.getSimpleName();
    private static final int RC_ADDRESS = 134;

    private LinearLayout llSignUp;
    private RelativeLayout rlSignUp;
    private TabIndicatorLine tabIndicatorLine;
    private TextInputLayout layoutFullName, layoutEmail, layoutPassword, layoutRetypePassword,
            layoutAddress, layoutZipcode, layoutGenre, layoutBirthday;
    private LinearLayout llStep1, llStep2, llStep3, layoutPhoneNumber;

    private CircleImageView imgAvatar;
//  private ImageView imgSignUp;
    private EditText mTxtFullName, mTxtEmail, mTxtPhoneNumber, mTxtPassword, mTxtRetypePassword,
            mTxtZipcode;
    private CustomEditText mTxtAddress, mTxtGenre, mTxtBrithday;
    private String mFullName, mEmail, mPassword, mRetypePassword,
            mAddress, mZipcode, mPhoneNumber,
            mGender, mAvatar, mBirthday,phone;
    private DataPart avatar = null;

    private TextView mLblCreateAccount;
    private TextViewCondensedItalic mLblAlreadyAMember;
    private CheckBox mChkRememberMe;
    private TextView  tvEg;
    private boolean mIsRegistered;
    private Dialog dialogGender;
    private RadioButton rbFemale, rbMale;
    private AppCompatSpinner spinnerCountryCodes;
    private TextView tvPhoneCode;

    private String countryCodeSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_sign_up_reskin_scroll);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    void initUI() {
        // Hide actionbar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        llSignUp = (LinearLayout) findViewById(R.id.ll_sign_up);
        rlSignUp = (RelativeLayout) findViewById(R.id.rl_sign_up);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
//        imgSignUp = (ImageView) findViewById(R.id.img_sign_up);
        tabIndicatorLine = (TabIndicatorLine) findViewById(R.id.tab_indicator_line);
        mLblCreateAccount = (TextView) findViewById(R.id.lbl_create_account);
        mLblAlreadyAMember = (TextViewCondensedItalic) findViewById(R.id.lbl_already_a_member);
        mChkRememberMe = (CheckBox) findViewById(R.id.chk_remember_me);
        llSignUp.getLayoutParams().height = AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this);

        llStep1 = (LinearLayout) findViewById(R.id.llStep1);
        llStep2 = (LinearLayout) findViewById(R.id.llStep2);
        llStep3 = (LinearLayout) findViewById(R.id.llStep3);
        tvEg = findViewById(R.id.tvEg);
        mTxtPhoneNumber = (EditText) findViewById(R.id.txt_phone);
        mTxtFullName = (EditText) findViewById(R.id.txt_full_name);
        mTxtEmail = (EditText) findViewById(R.id.txt_email);
        mTxtPassword = (EditText) findViewById(R.id.txt_password);
        mTxtRetypePassword = (EditText) findViewById(R.id.txt_retype_password);
        mTxtZipcode = (EditText) findViewById(R.id.txt_zipcode);
        mTxtGenre = (CustomEditText) findViewById(R.id.txt_genre);
        mTxtAddress = (CustomEditText) findViewById(R.id.txt_address);
        mTxtAddress.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mTxtBrithday = (CustomEditText) findViewById(R.id.txt_birthday);

        layoutFullName = (TextInputLayout) findViewById(R.id.layout_full_name);
        layoutEmail = (TextInputLayout) findViewById(R.id.layout_email);
        layoutPassword = (TextInputLayout) findViewById(R.id.layout_password);
        layoutRetypePassword = (TextInputLayout) findViewById(R.id.layout_retype_password);
        layoutAddress = (TextInputLayout) findViewById(R.id.layout_address);
        layoutZipcode = (TextInputLayout) findViewById(R.id.layout_zipcode);
//      layoutGenre             = (TextInputLayout) findViewById(R.id.layout_genre);
//      layoutBirthday          = (TextInputLayout) findViewById(R.id.layout_birthday);
        layoutPhoneNumber = (LinearLayout) findViewById(R.id.layout_phonenumber);
        spinnerCountryCodes = (AppCompatSpinner) findViewById(R.id.countryCode);
        tvPhoneCode = (TextView) findViewById(R.id.tv_phone_code);

        //mTxtAddress.setDrawableClickListener(clickListenerAddress);

        mTxtGenre.setDrawableClickListener(clickListenerGenre);

        mTxtBrithday.setDrawableClickListener(clickListenerBirthday);

        showScreenStep1();
       // setupCountryCodes();
        //getCountryCode();
    }

    private CustomEditText.DrawableClickListener clickListenerAddress = new CustomEditText.DrawableClickListener() {
        @Override
        public void onClick(DrawablePosition target) {
            switch (target) {
                case RIGHT:
                    //Do something here
                    MapsUtil.getAutoCompletePlaces(self, RC_ADDRESS);

                    break;

                default:
                    break;
            }
        }
    };

    private void setupCountryCodes() {
        ArrayList<Country> countries = getCountries();
        if (countries != null) {
            ArrayAdapter<Country> myAdapter = new ArrayAdapter<Country>(this, R.layout.item_country_spinner, countries);
            spinnerCountryCodes.setAdapter(myAdapter);
        }
    }

    private ArrayList<Country> getCountries() {
        ArrayList<Country> toReturn = new ArrayList<>();

        try {
            JSONArray countrArray = new JSONArray(readEncodedJsonString());
            toReturn = new ArrayList<>();
            for (int i = 0; i < countrArray.length(); i++) {
                JSONObject jsonObject = countrArray.getJSONObject(i);
                String countryName = jsonObject.getString("name");
                String countryDialCode = jsonObject.getString("dial_code");
                String countryCode = jsonObject.getString("code");
                Country country = new Country(countryCode, countryName, countryDialCode);
                toReturn.add(country);
            }
            Collections.sort(toReturn, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private String readEncodedJsonString() throws java.io.IOException {
        String base64 = "W3sibmFtZSI6IkFmZ2hhbmlzdGFuIiwiZGlhbF9jb2RlIjoiKzkzIiwiY29kZSI6IkFGIn0seyJuYW1lIjoiQWxiYW5pYSIsImRpYWxfY29kZSI6IiszNTUiLCJjb2RlIjoiQUwifSx7Im5hbWUiOiJBbGdlcmlhIiwiZGlhbF9jb2RlIjoiKzIxMyIsImNvZGUiOiJEWiJ9LHsibmFtZSI6IkFtZXJpY2FuU2Ftb2EiLCJkaWFsX2NvZGUiOiIrMSA2ODQiLCJjb2RlIjoiQVMifSx7Im5hbWUiOiJBbmRvcnJhIiwiZGlhbF9jb2RlIjoiKzM3NiIsImNvZGUiOiJBRCJ9LHsibmFtZSI6IkFuZ29sYSIsImRpYWxfY29kZSI6IisyNDQiLCJjb2RlIjoiQU8ifSx7Im5hbWUiOiJBbmd1aWxsYSIsImRpYWxfY29kZSI6IisxIDI2NCIsImNvZGUiOiJBSSJ9LHsibmFtZSI6IkFudGFyY3RpY2EiLCJkaWFsX2NvZGUiOiIrNjcyIiwiY29kZSI6IkFRIn0seyJuYW1lIjoiQW50aWd1YSBhbmQgQmFyYnVkYSIsImRpYWxfY29kZSI6IisxMjY4IiwiY29kZSI6IkFHIn0seyJuYW1lIjoiQXJnZW50aW5hIiwiZGlhbF9jb2RlIjoiKzU0IiwiY29kZSI6IkFSIn0seyJuYW1lIjoiQXJtZW5pYSIsImRpYWxfY29kZSI6IiszNzQiLCJjb2RlIjoiQU0ifSx7Im5hbWUiOiJBcnViYSIsImRpYWxfY29kZSI6IisyOTciLCJjb2RlIjoiQVcifSx7Im5hbWUiOiJBdXN0cmFsaWEiLCJkaWFsX2NvZGUiOiIrNjEiLCJjb2RlIjoiQVUifSx7Im5hbWUiOiJBdXN0cmlhIiwiZGlhbF9jb2RlIjoiKzQzIiwiY29kZSI6IkFUIn0seyJuYW1lIjoiQXplcmJhaWphbiIsImRpYWxfY29kZSI6Iis5OTQiLCJjb2RlIjoiQVoifSx7Im5hbWUiOiJCYWhhbWFzIiwiZGlhbF9jb2RlIjoiKzEgMjQyIiwiY29kZSI6IkJTIn0seyJuYW1lIjoiQmFocmFpbiIsImRpYWxfY29kZSI6Iis5NzMiLCJjb2RlIjoiQkgifSx7Im5hbWUiOiJCYW5nbGFkZXNoIiwiZGlhbF9jb2RlIjoiKzg4MCIsImNvZGUiOiJCRCJ9LHsibmFtZSI6IkJhcmJhZG9zIiwiZGlhbF9jb2RlIjoiKzEgMjQ2IiwiY29kZSI6IkJCIn0seyJuYW1lIjoiQmVsYXJ1cyIsImRpYWxfY29kZSI6IiszNzUiLCJjb2RlIjoiQlkifSx7Im5hbWUiOiJCZWxnaXVtIiwiZGlhbF9jb2RlIjoiKzMyIiwiY29kZSI6IkJFIn0seyJuYW1lIjoiQmVsaXplIiwiZGlhbF9jb2RlIjoiKzUwMSIsImNvZGUiOiJCWiJ9LHsibmFtZSI6IkJlbmluIiwiZGlhbF9jb2RlIjoiKzIyOSIsImNvZGUiOiJCSiJ9LHsibmFtZSI6IkJlcm11ZGEiLCJkaWFsX2NvZGUiOiIrMSA0NDEiLCJjb2RlIjoiQk0ifSx7Im5hbWUiOiJCaHV0YW4iLCJkaWFsX2NvZGUiOiIrOTc1IiwiY29kZSI6IkJUIn0seyJuYW1lIjoiQm9saXZpYSwgUGx1cmluYXRpb25hbCBTdGF0ZSBvZiIsImRpYWxfY29kZSI6Iis1OTEiLCJjb2RlIjoiQk8ifSx7Im5hbWUiOiJCb3NuaWEgYW5kIEhlcnplZ292aW5hIiwiZGlhbF9jb2RlIjoiKzM4NyIsImNvZGUiOiJCQSJ9LHsibmFtZSI6IkJvdHN3YW5hIiwiZGlhbF9jb2RlIjoiKzI2NyIsImNvZGUiOiJCVyJ9LHsibmFtZSI6IkJyYXppbCIsImRpYWxfY29kZSI6Iis1NSIsImNvZGUiOiJCUiJ9LHsibmFtZSI6IkJyaXRpc2ggSW5kaWFuIE9jZWFuIFRlcnJpdG9yeSIsImRpYWxfY29kZSI6IisyNDYiLCJjb2RlIjoiSU8ifSx7Im5hbWUiOiJCcnVuZWkgRGFydXNzYWxhbSIsImRpYWxfY29kZSI6Iis2NzMiLCJjb2RlIjoiQk4ifSx7Im5hbWUiOiJCdWxnYXJpYSIsImRpYWxfY29kZSI6IiszNTkiLCJjb2RlIjoiQkcifSx7Im5hbWUiOiJCdXJraW5hIEZhc28iLCJkaWFsX2NvZGUiOiIrMjI2IiwiY29kZSI6IkJGIn0seyJuYW1lIjoiQnVydW5kaSIsImRpYWxfY29kZSI6IisyNTciLCJjb2RlIjoiQkkifSx7Im5hbWUiOiJDYW1ib2RpYSIsImRpYWxfY29kZSI6Iis4NTUiLCJjb2RlIjoiS0gifSx7Im5hbWUiOiJDYW1lcm9vbiIsImRpYWxfY29kZSI6IisyMzciLCJjb2RlIjoiQ00ifSx7Im5hbWUiOiJDYW5hZGEiLCJkaWFsX2NvZGUiOiIrMSIsImNvZGUiOiJDQSJ9LHsibmFtZSI6IkNhcGUgVmVyZGUiLCJkaWFsX2NvZGUiOiIrMjM4IiwiY29kZSI6IkNWIn0seyJuYW1lIjoiQ2F5bWFuIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrIDM0NSIsImNvZGUiOiJLWSJ9LHsibmFtZSI6IkNlbnRyYWwgQWZyaWNhbiBSZXB1YmxpYyIsImRpYWxfY29kZSI6IisyMzYiLCJjb2RlIjoiQ0YifSx7Im5hbWUiOiJDaGFkIiwiZGlhbF9jb2RlIjoiKzIzNSIsImNvZGUiOiJURCJ9LHsibmFtZSI6IkNoaWxlIiwiZGlhbF9jb2RlIjoiKzU2IiwiY29kZSI6IkNMIn0seyJuYW1lIjoiQ2hpbmEiLCJkaWFsX2NvZGUiOiIrODYiLCJjb2RlIjoiQ04ifSx7Im5hbWUiOiJDaHJpc3RtYXMgSXNsYW5kIiwiZGlhbF9jb2RlIjoiKzYxIiwiY29kZSI6IkNYIn0seyJuYW1lIjoiQ29jb3MgKEtlZWxpbmcpIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrNjEiLCJjb2RlIjoiQ0MifSx7Im5hbWUiOiJDb2xvbWJpYSIsImRpYWxfY29kZSI6Iis1NyIsImNvZGUiOiJDTyJ9LHsibmFtZSI6IkNvbW9yb3MiLCJkaWFsX2NvZGUiOiIrMjY5IiwiY29kZSI6IktNIn0seyJuYW1lIjoiQ29uZ28iLCJkaWFsX2NvZGUiOiIrMjQyIiwiY29kZSI6IkNHIn0seyJuYW1lIjoiQ29uZ28sIFRoZSBEZW1vY3JhdGljIFJlcHVibGljIG9mIHRoZSIsImRpYWxfY29kZSI6IisyNDMiLCJjb2RlIjoiQ0QifSx7Im5hbWUiOiJDb29rIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrNjgyIiwiY29kZSI6IkNLIn0seyJuYW1lIjoiQ29zdGEgUmljYSIsImRpYWxfY29kZSI6Iis1MDYiLCJjb2RlIjoiQ1IifSx7Im5hbWUiOiJDb3RlIGQnSXZvaXJlIiwiZGlhbF9jb2RlIjoiKzIyNSIsImNvZGUiOiJDSSJ9LHsibmFtZSI6IkNyb2F0aWEiLCJkaWFsX2NvZGUiOiIrMzg1IiwiY29kZSI6IkhSIn0seyJuYW1lIjoiQ3ViYSIsImRpYWxfY29kZSI6Iis1MyIsImNvZGUiOiJDVSJ9LHsibmFtZSI6IkN5cHJ1cyIsImRpYWxfY29kZSI6IiszNTciLCJjb2RlIjoiQ1kifSx7Im5hbWUiOiJDemVjaCBSZXB1YmxpYyIsImRpYWxfY29kZSI6Iis0MjAiLCJjb2RlIjoiQ1oifSx7Im5hbWUiOiJEZW5tYXJrIiwiZGlhbF9jb2RlIjoiKzQ1IiwiY29kZSI6IkRLIn0seyJuYW1lIjoiRGppYm91dGkiLCJkaWFsX2NvZGUiOiIrMjUzIiwiY29kZSI6IkRKIn0seyJuYW1lIjoiRG9taW5pY2EiLCJkaWFsX2NvZGUiOiIrMSA3NjciLCJjb2RlIjoiRE0ifSx7Im5hbWUiOiJEb21pbmljYW4gUmVwdWJsaWMiLCJkaWFsX2NvZGUiOiIrMSA4NDkiLCJjb2RlIjoiRE8ifSx7Im5hbWUiOiJFY3VhZG9yIiwiZGlhbF9jb2RlIjoiKzU5MyIsImNvZGUiOiJFQyJ9LHsibmFtZSI6IkVneXB0IiwiZGlhbF9jb2RlIjoiKzIwIiwiY29kZSI6IkVHIn0seyJuYW1lIjoiRWwgU2FsdmFkb3IiLCJkaWFsX2NvZGUiOiIrNTAzIiwiY29kZSI6IlNWIn0seyJuYW1lIjoiRXF1YXRvcmlhbCBHdWluZWEiLCJkaWFsX2NvZGUiOiIrMjQwIiwiY29kZSI6IkdRIn0seyJuYW1lIjoiRXJpdHJlYSIsImRpYWxfY29kZSI6IisyOTEiLCJjb2RlIjoiRVIifSx7Im5hbWUiOiJFc3RvbmlhIiwiZGlhbF9jb2RlIjoiKzM3MiIsImNvZGUiOiJFRSJ9LHsibmFtZSI6IkV0aGlvcGlhIiwiZGlhbF9jb2RlIjoiKzI1MSIsImNvZGUiOiJFVCJ9LHsibmFtZSI6IkZhbGtsYW5kIElzbGFuZHMgKE1hbHZpbmFzKSIsImRpYWxfY29kZSI6Iis1MDAiLCJjb2RlIjoiRksifSx7Im5hbWUiOiJGYXJvZSBJc2xhbmRzIiwiZGlhbF9jb2RlIjoiKzI5OCIsImNvZGUiOiJGTyJ9LHsibmFtZSI6IkZpamkiLCJkaWFsX2NvZGUiOiIrNjc5IiwiY29kZSI6IkZKIn0seyJuYW1lIjoiRmlubGFuZCIsImRpYWxfY29kZSI6IiszNTgiLCJjb2RlIjoiRkkifSx7Im5hbWUiOiJGcmFuY2UiLCJkaWFsX2NvZGUiOiIrMzMiLCJjb2RlIjoiRlIifSx7Im5hbWUiOiJGcmVuY2ggR3VpYW5hIiwiZGlhbF9jb2RlIjoiKzU5NCIsImNvZGUiOiJHRiJ9LHsibmFtZSI6IkZyZW5jaCBQb2x5bmVzaWEiLCJkaWFsX2NvZGUiOiIrNjg5IiwiY29kZSI6IlBGIn0seyJuYW1lIjoiR2Fib24iLCJkaWFsX2NvZGUiOiIrMjQxIiwiY29kZSI6IkdBIn0seyJuYW1lIjoiR2FtYmlhIiwiZGlhbF9jb2RlIjoiKzIyMCIsImNvZGUiOiJHTSJ9LHsibmFtZSI6Ikdlb3JnaWEiLCJkaWFsX2NvZGUiOiIrOTk1IiwiY29kZSI6IkdFIn0seyJuYW1lIjoiR2VybWFueSIsImRpYWxfY29kZSI6Iis0OSIsImNvZGUiOiJERSJ9LHsibmFtZSI6IkdoYW5hIiwiZGlhbF9jb2RlIjoiKzIzMyIsImNvZGUiOiJHSCJ9LHsibmFtZSI6IkdpYnJhbHRhciIsImRpYWxfY29kZSI6IiszNTAiLCJjb2RlIjoiR0kifSx7Im5hbWUiOiJHcmVlY2UiLCJkaWFsX2NvZGUiOiIrMzAiLCJjb2RlIjoiR1IifSx7Im5hbWUiOiJHcmVlbmxhbmQiLCJkaWFsX2NvZGUiOiIrMjk5IiwiY29kZSI6IkdMIn0seyJuYW1lIjoiR3JlbmFkYSIsImRpYWxfY29kZSI6IisxIDQ3MyIsImNvZGUiOiJHRCJ9LHsibmFtZSI6Ikd1YWRlbG91cGUiLCJkaWFsX2NvZGUiOiIrNTkwIiwiY29kZSI6IkdQIn0seyJuYW1lIjoiR3VhbSIsImRpYWxfY29kZSI6IisxIDY3MSIsImNvZGUiOiJHVSJ9LHsibmFtZSI6Ikd1YXRlbWFsYSIsImRpYWxfY29kZSI6Iis1MDIiLCJjb2RlIjoiR1QifSx7Im5hbWUiOiJHdWVybnNleSIsImRpYWxfY29kZSI6Iis0NCIsImNvZGUiOiJHRyJ9LHsibmFtZSI6Ikd1aW5lYSIsImRpYWxfY29kZSI6IisyMjQiLCJjb2RlIjoiR04ifSx7Im5hbWUiOiJHdWluZWEtQmlzc2F1IiwiZGlhbF9jb2RlIjoiKzI0NSIsImNvZGUiOiJHVyJ9LHsibmFtZSI6Ikd1eWFuYSIsImRpYWxfY29kZSI6Iis1OTUiLCJjb2RlIjoiR1kifSx7Im5hbWUiOiJIYWl0aSIsImRpYWxfY29kZSI6Iis1MDkiLCJjb2RlIjoiSFQifSx7Im5hbWUiOiJIb2x5IFNlZSAoVmF0aWNhbiBDaXR5IFN0YXRlKSIsImRpYWxfY29kZSI6IiszNzkiLCJjb2RlIjoiVkEifSx7Im5hbWUiOiJIb25kdXJhcyIsImRpYWxfY29kZSI6Iis1MDQiLCJjb2RlIjoiSE4ifSx7Im5hbWUiOiJIb25nIEtvbmciLCJkaWFsX2NvZGUiOiIrODUyIiwiY29kZSI6IkhLIn0seyJuYW1lIjoiSHVuZ2FyeSIsImRpYWxfY29kZSI6IiszNiIsImNvZGUiOiJIVSJ9LHsibmFtZSI6IkljZWxhbmQiLCJkaWFsX2NvZGUiOiIrMzU0IiwiY29kZSI6IklTIn0seyJuYW1lIjoiSW5kaWEiLCJkaWFsX2NvZGUiOiIrOTEiLCJjb2RlIjoiSU4ifSx7Im5hbWUiOiJJbmRvbmVzaWEiLCJkaWFsX2NvZGUiOiIrNjIiLCJjb2RlIjoiSUQifSx7Im5hbWUiOiJJcmFuLCBJc2xhbWljIFJlcHVibGljIG9mIiwiZGlhbF9jb2RlIjoiKzk4IiwiY29kZSI6IklSIn0seyJuYW1lIjoiSXJhcSIsImRpYWxfY29kZSI6Iis5NjQiLCJjb2RlIjoiSVEifSx7Im5hbWUiOiJJcmVsYW5kIiwiZGlhbF9jb2RlIjoiKzM1MyIsImNvZGUiOiJJRSJ9LHsibmFtZSI6IklzbGUgb2YgTWFuIiwiZGlhbF9jb2RlIjoiKzQ0IiwiY29kZSI6IklNIn0seyJuYW1lIjoiSXNyYWVsIiwiZGlhbF9jb2RlIjoiKzk3MiIsImNvZGUiOiJJTCJ9LHsibmFtZSI6Ikl0YWx5IiwiZGlhbF9jb2RlIjoiKzM5IiwiY29kZSI6IklUIn0seyJuYW1lIjoiSmFtYWljYSIsImRpYWxfY29kZSI6IisxIDg3NiIsImNvZGUiOiJKTSJ9LHsibmFtZSI6IkphcGFuIiwiZGlhbF9jb2RlIjoiKzgxIiwiY29kZSI6IkpQIn0seyJuYW1lIjoiSmVyc2V5IiwiZGlhbF9jb2RlIjoiKzQ0IiwiY29kZSI6IkpFIn0seyJuYW1lIjoiSm9yZGFuIiwiZGlhbF9jb2RlIjoiKzk2MiIsImNvZGUiOiJKTyJ9LHsibmFtZSI6IkthemFraHN0YW4iLCJkaWFsX2NvZGUiOiIrNyA3IiwiY29kZSI6IktaIn0seyJuYW1lIjoiS2VueWEiLCJkaWFsX2NvZGUiOiIrMjU0IiwiY29kZSI6IktFIn0seyJuYW1lIjoiS2lyaWJhdGkiLCJkaWFsX2NvZGUiOiIrNjg2IiwiY29kZSI6IktJIn0seyJuYW1lIjoiS29yZWEsIERlbW9jcmF0aWMgUGVvcGxlJ3MgUmVwdWJsaWMgb2YiLCJkaWFsX2NvZGUiOiIrODUwIiwiY29kZSI6IktQIn0seyJuYW1lIjoiS29yZWEsIFJlcHVibGljIG9mIiwiZGlhbF9jb2RlIjoiKzgyIiwiY29kZSI6IktSIn0seyJuYW1lIjoiS3V3YWl0IiwiZGlhbF9jb2RlIjoiKzk2NSIsImNvZGUiOiJLVyJ9LHsibmFtZSI6Ikt5cmd5enN0YW4iLCJkaWFsX2NvZGUiOiIrOTk2IiwiY29kZSI6IktHIn0seyJuYW1lIjoiTGFvIFBlb3BsZSdzIERlbW9jcmF0aWMgUmVwdWJsaWMiLCJkaWFsX2NvZGUiOiIrODU2IiwiY29kZSI6IkxBIn0seyJuYW1lIjoiTGF0dmlhIiwiZGlhbF9jb2RlIjoiKzM3MSIsImNvZGUiOiJMViJ9LHsibmFtZSI6IkxlYmFub24iLCJkaWFsX2NvZGUiOiIrOTYxIiwiY29kZSI6IkxCIn0seyJuYW1lIjoiTGVzb3RobyIsImRpYWxfY29kZSI6IisyNjYiLCJjb2RlIjoiTFMifSx7Im5hbWUiOiJMaWJlcmlhIiwiZGlhbF9jb2RlIjoiKzIzMSIsImNvZGUiOiJMUiJ9LHsibmFtZSI6IkxpYnlhbiBBcmFiIEphbWFoaXJpeWEiLCJkaWFsX2NvZGUiOiIrMjE4IiwiY29kZSI6IkxZIn0seyJuYW1lIjoiTGllY2h0ZW5zdGVpbiIsImRpYWxfY29kZSI6Iis0MjMiLCJjb2RlIjoiTEkifSx7Im5hbWUiOiJMaXRodWFuaWEiLCJkaWFsX2NvZGUiOiIrMzcwIiwiY29kZSI6IkxUIn0seyJuYW1lIjoiTHV4ZW1ib3VyZyIsImRpYWxfY29kZSI6IiszNTIiLCJjb2RlIjoiTFUifSx7Im5hbWUiOiJNYWNhbyIsImRpYWxfY29kZSI6Iis4NTMiLCJjb2RlIjoiTU8ifSx7Im5hbWUiOiJNYWNlZG9uaWEsIFRoZSBGb3JtZXIgWXVnb3NsYXYgUmVwdWJsaWMgb2YiLCJkaWFsX2NvZGUiOiIrMzg5IiwiY29kZSI6Ik1LIn0seyJuYW1lIjoiTWFkYWdhc2NhciIsImRpYWxfY29kZSI6IisyNjEiLCJjb2RlIjoiTUcifSx7Im5hbWUiOiJNYWxhd2kiLCJkaWFsX2NvZGUiOiIrMjY1IiwiY29kZSI6Ik1XIn0seyJuYW1lIjoiTWFsYXlzaWEiLCJkaWFsX2NvZGUiOiIrNjAiLCJjb2RlIjoiTVkifSx7Im5hbWUiOiJNYWxkaXZlcyIsImRpYWxfY29kZSI6Iis5NjAiLCJjb2RlIjoiTVYifSx7Im5hbWUiOiJNYWxpIiwiZGlhbF9jb2RlIjoiKzIyMyIsImNvZGUiOiJNTCJ9LHsibmFtZSI6Ik1hbHRhIiwiZGlhbF9jb2RlIjoiKzM1NiIsImNvZGUiOiJNVCJ9LHsibmFtZSI6Ik1hcnNoYWxsIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrNjkyIiwiY29kZSI6Ik1IIn0seyJuYW1lIjoiTWFydGluaXF1ZSIsImRpYWxfY29kZSI6Iis1OTYiLCJjb2RlIjoiTVEifSx7Im5hbWUiOiJNYXVyaXRhbmlhIiwiZGlhbF9jb2RlIjoiKzIyMiIsImNvZGUiOiJNUiJ9LHsibmFtZSI6Ik1hdXJpdGl1cyIsImRpYWxfY29kZSI6IisyMzAiLCJjb2RlIjoiTVUifSx7Im5hbWUiOiJNYXlvdHRlIiwiZGlhbF9jb2RlIjoiKzI2MiIsImNvZGUiOiJZVCJ9LHsibmFtZSI6Ik1leGljbyIsImRpYWxfY29kZSI6Iis1MiIsImNvZGUiOiJNWCJ9LHsibmFtZSI6Ik1pY3JvbmVzaWEsIEZlZGVyYXRlZCBTdGF0ZXMgb2YiLCJkaWFsX2NvZGUiOiIrNjkxIiwiY29kZSI6IkZNIn0seyJuYW1lIjoiTW9sZG92YSwgUmVwdWJsaWMgb2YiLCJkaWFsX2NvZGUiOiIrMzczIiwiY29kZSI6Ik1EIn0seyJuYW1lIjoiTW9uYWNvIiwiZGlhbF9jb2RlIjoiKzM3NyIsImNvZGUiOiJNQyJ9LHsibmFtZSI6Ik1vbmdvbGlhIiwiZGlhbF9jb2RlIjoiKzk3NiIsImNvZGUiOiJNTiJ9LHsibmFtZSI6Ik1vbnRlbmVncm8iLCJkaWFsX2NvZGUiOiIrMzgyIiwiY29kZSI6Ik1FIn0seyJuYW1lIjoiTW9udHNlcnJhdCIsImRpYWxfY29kZSI6IisxNjY0IiwiY29kZSI6Ik1TIn0seyJuYW1lIjoiTW9yb2NjbyIsImRpYWxfY29kZSI6IisyMTIiLCJjb2RlIjoiTUEifSx7Im5hbWUiOiJNb3phbWJpcXVlIiwiZGlhbF9jb2RlIjoiKzI1OCIsImNvZGUiOiJNWiJ9LHsibmFtZSI6Ik15YW5tYXIiLCJkaWFsX2NvZGUiOiIrOTUiLCJjb2RlIjoiTU0ifSx7Im5hbWUiOiJOYW1pYmlhIiwiZGlhbF9jb2RlIjoiKzI2NCIsImNvZGUiOiJOQSJ9LHsibmFtZSI6Ik5hdXJ1IiwiZGlhbF9jb2RlIjoiKzY3NCIsImNvZGUiOiJOUiJ9LHsibmFtZSI6Ik5lcGFsIiwiZGlhbF9jb2RlIjoiKzk3NyIsImNvZGUiOiJOUCJ9LHsibmFtZSI6Ik5ldGhlcmxhbmRzIiwiZGlhbF9jb2RlIjoiKzMxIiwiY29kZSI6Ik5MIn0seyJuYW1lIjoiTmV0aGVybGFuZHMgQW50aWxsZXMiLCJkaWFsX2NvZGUiOiIrNTk5IiwiY29kZSI6IkFOIn0seyJuYW1lIjoiTmV3IENhbGVkb25pYSIsImRpYWxfY29kZSI6Iis2ODciLCJjb2RlIjoiTkMifSx7Im5hbWUiOiJOZXcgWmVhbGFuZCIsImRpYWxfY29kZSI6Iis2NCIsImNvZGUiOiJOWiJ9LHsibmFtZSI6Ik5pY2FyYWd1YSIsImRpYWxfY29kZSI6Iis1MDUiLCJjb2RlIjoiTkkifSx7Im5hbWUiOiJOaWdlciIsImRpYWxfY29kZSI6IisyMjciLCJjb2RlIjoiTkUifSx7Im5hbWUiOiJOaWdlcmlhIiwiZGlhbF9jb2RlIjoiKzIzNCIsImNvZGUiOiJORyJ9LHsibmFtZSI6Ik5pdWUiLCJkaWFsX2NvZGUiOiIrNjgzIiwiY29kZSI6Ik5VIn0seyJuYW1lIjoiTm9yZm9sayBJc2xhbmQiLCJkaWFsX2NvZGUiOiIrNjcyIiwiY29kZSI6Ik5GIn0seyJuYW1lIjoiTm9ydGhlcm4gTWFyaWFuYSBJc2xhbmRzIiwiZGlhbF9jb2RlIjoiKzEgNjcwIiwiY29kZSI6Ik1QIn0seyJuYW1lIjoiTm9yd2F5IiwiZGlhbF9jb2RlIjoiKzQ3IiwiY29kZSI6Ik5PIn0seyJuYW1lIjoiT21hbiIsImRpYWxfY29kZSI6Iis5NjgiLCJjb2RlIjoiT00ifSx7Im5hbWUiOiJQYWtpc3RhbiIsImRpYWxfY29kZSI6Iis5MiIsImNvZGUiOiJQSyJ9LHsibmFtZSI6IlBhbGF1IiwiZGlhbF9jb2RlIjoiKzY4MCIsImNvZGUiOiJQVyJ9LHsibmFtZSI6IlBhbGVzdGluaWFuIFRlcnJpdG9yeSwgT2NjdXBpZWQiLCJkaWFsX2NvZGUiOiIrOTcwIiwiY29kZSI6IlBTIn0seyJuYW1lIjoiUGFuYW1hIiwiZGlhbF9jb2RlIjoiKzUwNyIsImNvZGUiOiJQQSJ9LHsibmFtZSI6IlBhcHVhIE5ldyBHdWluZWEiLCJkaWFsX2NvZGUiOiIrNjc1IiwiY29kZSI6IlBHIn0seyJuYW1lIjoiUGFyYWd1YXkiLCJkaWFsX2NvZGUiOiIrNTk1IiwiY29kZSI6IlBZIn0seyJuYW1lIjoiUGVydSIsImRpYWxfY29kZSI6Iis1MSIsImNvZGUiOiJQRSJ9LHsibmFtZSI6IlBoaWxpcHBpbmVzIiwiZGlhbF9jb2RlIjoiKzYzIiwiY29kZSI6IlBIIn0seyJuYW1lIjoiUGl0Y2Fpcm4iLCJkaWFsX2NvZGUiOiIrODcyIiwiY29kZSI6IlBOIn0seyJuYW1lIjoiUG9sYW5kIiwiZGlhbF9jb2RlIjoiKzQ4IiwiY29kZSI6IlBMIn0seyJuYW1lIjoiUG9ydHVnYWwiLCJkaWFsX2NvZGUiOiIrMzUxIiwiY29kZSI6IlBUIn0seyJuYW1lIjoiUHVlcnRvIFJpY28iLCJkaWFsX2NvZGUiOiIrMSA5MzkiLCJjb2RlIjoiUFIifSx7Im5hbWUiOiJRYXRhciIsImRpYWxfY29kZSI6Iis5NzQiLCJjb2RlIjoiUUEifSx7Im5hbWUiOiJSb21hbmlhIiwiZGlhbF9jb2RlIjoiKzQwIiwiY29kZSI6IlJPIn0seyJuYW1lIjoiUnVzc2lhIiwiZGlhbF9jb2RlIjoiKzciLCJjb2RlIjoiUlUifSx7Im5hbWUiOiJSd2FuZGEiLCJkaWFsX2NvZGUiOiIrMjUwIiwiY29kZSI6IlJXIn0seyJuYW1lIjoiUsOpdW5pb24iLCJkaWFsX2NvZGUiOiIrMjYyIiwiY29kZSI6IlJFIn0seyJuYW1lIjoiU2FpbnQgQmFydGjDqWxlbXkiLCJkaWFsX2NvZGUiOiIrNTkwIiwiY29kZSI6IkJMIn0seyJuYW1lIjoiU2FpbnQgSGVsZW5hLCBBc2NlbnNpb24gYW5kIFRyaXN0YW4gRGEgQ3VuaGEiLCJkaWFsX2NvZGUiOiIrMjkwIiwiY29kZSI6IlNIIn0seyJuYW1lIjoiU2FpbnQgS2l0dHMgYW5kIE5ldmlzIiwiZGlhbF9jb2RlIjoiKzEgODY5IiwiY29kZSI6IktOIn0seyJuYW1lIjoiU2FpbnQgTHVjaWEiLCJkaWFsX2NvZGUiOiIrMSA3NTgiLCJjb2RlIjoiTEMifSx7Im5hbWUiOiJTYWludCBNYXJ0aW4iLCJkaWFsX2NvZGUiOiIrNTkwIiwiY29kZSI6Ik1GIn0seyJuYW1lIjoiU2FpbnQgUGllcnJlIGFuZCBNaXF1ZWxvbiIsImRpYWxfY29kZSI6Iis1MDgiLCJjb2RlIjoiUE0ifSx7Im5hbWUiOiJTYWludCBWaW5jZW50IGFuZCB0aGUgR3JlbmFkaW5lcyIsImRpYWxfY29kZSI6IisxIDc4NCIsImNvZGUiOiJWQyJ9LHsibmFtZSI6IlNhbW9hIiwiZGlhbF9jb2RlIjoiKzY4NSIsImNvZGUiOiJXUyJ9LHsibmFtZSI6IlNhbiBNYXJpbm8iLCJkaWFsX2NvZGUiOiIrMzc4IiwiY29kZSI6IlNNIn0seyJuYW1lIjoiU2FvIFRvbWUgYW5kIFByaW5jaXBlIiwiZGlhbF9jb2RlIjoiKzIzOSIsImNvZGUiOiJTVCJ9LHsibmFtZSI6IlNhdWRpIEFyYWJpYSIsImRpYWxfY29kZSI6Iis5NjYiLCJjb2RlIjoiU0EifSx7Im5hbWUiOiJTZW5lZ2FsIiwiZGlhbF9jb2RlIjoiKzIyMSIsImNvZGUiOiJTTiJ9LHsibmFtZSI6IlNlcmJpYSIsImRpYWxfY29kZSI6IiszODEiLCJjb2RlIjoiUlMifSx7Im5hbWUiOiJTZXljaGVsbGVzIiwiZGlhbF9jb2RlIjoiKzI0OCIsImNvZGUiOiJTQyJ9LHsibmFtZSI6IlNpZXJyYSBMZW9uZSIsImRpYWxfY29kZSI6IisyMzIiLCJjb2RlIjoiU0wifSx7Im5hbWUiOiJTaW5nYXBvcmUiLCJkaWFsX2NvZGUiOiIrNjUiLCJjb2RlIjoiU0cifSx7Im5hbWUiOiJTbG92YWtpYSIsImRpYWxfY29kZSI6Iis0MjEiLCJjb2RlIjoiU0sifSx7Im5hbWUiOiJTbG92ZW5pYSIsImRpYWxfY29kZSI6IiszODYiLCJjb2RlIjoiU0kifSx7Im5hbWUiOiJTb2xvbW9uIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrNjc3IiwiY29kZSI6IlNCIn0seyJuYW1lIjoiU29tYWxpYSIsImRpYWxfY29kZSI6IisyNTIiLCJjb2RlIjoiU08ifSx7Im5hbWUiOiJTb3V0aCBBZnJpY2EiLCJkaWFsX2NvZGUiOiIrMjciLCJjb2RlIjoiWkEifSx7Im5hbWUiOiJTb3V0aCBHZW9yZ2lhIGFuZCB0aGUgU291dGggU2FuZHdpY2ggSXNsYW5kcyIsImRpYWxfY29kZSI6Iis1MDAiLCJjb2RlIjoiR1MifSx7Im5hbWUiOiJTcGFpbiIsImRpYWxfY29kZSI6IiszNCIsImNvZGUiOiJFUyJ9LHsibmFtZSI6IlNyaSBMYW5rYSIsImRpYWxfY29kZSI6Iis5NCIsImNvZGUiOiJMSyJ9LHsibmFtZSI6IlN1ZGFuIiwiZGlhbF9jb2RlIjoiKzI0OSIsImNvZGUiOiJTRCJ9LHsibmFtZSI6IlN1cmluYW1lIiwiZGlhbF9jb2RlIjoiKzU5NyIsImNvZGUiOiJTUiJ9LHsibmFtZSI6IlN2YWxiYXJkIGFuZCBKYW4gTWF5ZW4iLCJkaWFsX2NvZGUiOiIrNDciLCJjb2RlIjoiU0oifSx7Im5hbWUiOiJTd2F6aWxhbmQiLCJkaWFsX2NvZGUiOiIrMjY4IiwiY29kZSI6IlNaIn0seyJuYW1lIjoiU3dlZGVuIiwiZGlhbF9jb2RlIjoiKzQ2IiwiY29kZSI6IlNFIn0seyJuYW1lIjoiU3dpdHplcmxhbmQiLCJkaWFsX2NvZGUiOiIrNDEiLCJjb2RlIjoiQ0gifSx7Im5hbWUiOiJTeXJpYW4gQXJhYiBSZXB1YmxpYyIsImRpYWxfY29kZSI6Iis5NjMiLCJjb2RlIjoiU1kifSx7Im5hbWUiOiJUYWl3YW4iLCJkaWFsX2NvZGUiOiIrODg2IiwiY29kZSI6IlRXIn0seyJuYW1lIjoiVGFqaWtpc3RhbiIsImRpYWxfY29kZSI6Iis5OTIiLCJjb2RlIjoiVEoifSx7Im5hbWUiOiJUYW56YW5pYSwgVW5pdGVkIFJlcHVibGljIG9mIiwiZGlhbF9jb2RlIjoiKzI1NSIsImNvZGUiOiJUWiJ9LHsibmFtZSI6IlRoYWlsYW5kIiwiZGlhbF9jb2RlIjoiKzY2IiwiY29kZSI6IlRIIn0seyJuYW1lIjoiVGltb3ItTGVzdGUiLCJkaWFsX2NvZGUiOiIrNjcwIiwiY29kZSI6IlRMIn0seyJuYW1lIjoiVG9nbyIsImRpYWxfY29kZSI6IisyMjgiLCJjb2RlIjoiVEcifSx7Im5hbWUiOiJUb2tlbGF1IiwiZGlhbF9jb2RlIjoiKzY5MCIsImNvZGUiOiJUSyJ9LHsibmFtZSI6IlRvbmdhIiwiZGlhbF9jb2RlIjoiKzY3NiIsImNvZGUiOiJUTyJ9LHsibmFtZSI6IlRyaW5pZGFkIGFuZCBUb2JhZ28iLCJkaWFsX2NvZGUiOiIrMSA4NjgiLCJjb2RlIjoiVFQifSx7Im5hbWUiOiJUdW5pc2lhIiwiZGlhbF9jb2RlIjoiKzIxNiIsImNvZGUiOiJUTiJ9LHsibmFtZSI6IlR1cmtleSIsImRpYWxfY29kZSI6Iis5MCIsImNvZGUiOiJUUiJ9LHsibmFtZSI6IlR1cmttZW5pc3RhbiIsImRpYWxfY29kZSI6Iis5OTMiLCJjb2RlIjoiVE0ifSx7Im5hbWUiOiJUdXJrcyBhbmQgQ2FpY29zIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrMSA2NDkiLCJjb2RlIjoiVEMifSx7Im5hbWUiOiJUdXZhbHUiLCJkaWFsX2NvZGUiOiIrNjg4IiwiY29kZSI6IlRWIn0seyJuYW1lIjoiVWdhbmRhIiwiZGlhbF9jb2RlIjoiKzI1NiIsImNvZGUiOiJVRyJ9LHsibmFtZSI6IlVrcmFpbmUiLCJkaWFsX2NvZGUiOiIrMzgwIiwiY29kZSI6IlVBIn0seyJuYW1lIjoiVW5pdGVkIEFyYWIgRW1pcmF0ZXMiLCJkaWFsX2NvZGUiOiIrOTcxIiwiY29kZSI6IkFFIn0seyJuYW1lIjoiVW5pdGVkIEtpbmdkb20iLCJkaWFsX2NvZGUiOiIrNDQiLCJjb2RlIjoiR0IifSx7Im5hbWUiOiJVbml0ZWQgU3RhdGVzIiwiZGlhbF9jb2RlIjoiKzEiLCJjb2RlIjoiVVMifSx7Im5hbWUiOiJVcnVndWF5IiwiZGlhbF9jb2RlIjoiKzU5OCIsImNvZGUiOiJVWSJ9LHsibmFtZSI6IlV6YmVraXN0YW4iLCJkaWFsX2NvZGUiOiIrOTk4IiwiY29kZSI6IlVaIn0seyJuYW1lIjoiVmFudWF0dSIsImRpYWxfY29kZSI6Iis2NzgiLCJjb2RlIjoiVlUifSx7Im5hbWUiOiJWZW5lenVlbGEsIEJvbGl2YXJpYW4gUmVwdWJsaWMgb2YiLCJkaWFsX2NvZGUiOiIrNTgiLCJjb2RlIjoiVkUifSx7Im5hbWUiOiJWaWV0IE5hbSIsImRpYWxfY29kZSI6Iis4NCIsImNvZGUiOiJWTiJ9LHsibmFtZSI6IlZpcmdpbiBJc2xhbmRzLCBCcml0aXNoIiwiZGlhbF9jb2RlIjoiKzEgMjg0IiwiY29kZSI6IlZHIn0seyJuYW1lIjoiVmlyZ2luIElzbGFuZHMsIFUuUy4iLCJkaWFsX2NvZGUiOiIrMSAzNDAiLCJjb2RlIjoiVkkifSx7Im5hbWUiOiJXYWxsaXMgYW5kIEZ1dHVuYSIsImRpYWxfY29kZSI6Iis2ODEiLCJjb2RlIjoiV0YifSx7Im5hbWUiOiJZZW1lbiIsImRpYWxfY29kZSI6Iis5NjciLCJjb2RlIjoiWUUifSx7Im5hbWUiOiJaYW1iaWEiLCJkaWFsX2NvZGUiOiIrMjYwIiwiY29kZSI6IlpNIn0seyJuYW1lIjoiWmltYmFid2UiLCJkaWFsX2NvZGUiOiIrMjYzIiwiY29kZSI6IlpXIn0seyJuYW1lIjoiw4VsYW5kIElzbGFuZHMiLCJkaWFsX2NvZGUiOiIrMzU4IiwiY29kZSI6IkFYIn1d";
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    private CustomEditText.DrawableClickListener clickListenerBirthday = new CustomEditText.DrawableClickListener() {
        @Override
        public void onClick(DrawablePosition target) {
            switch (target) {
                case RIGHT:
                    //Do something here
                    processingShowDialogDate();

                    break;

                default:
                    break;
            }
        }
    };

    private CustomEditText.DrawableClickListener clickListenerGenre = new CustomEditText.DrawableClickListener() {
        @Override
        public void onClick(DrawablePosition target) {
            switch (target) {
                case RIGHT:
                    //Do something here
                    processingShowDialogGender();

                    break;

                default:
                    break;
            }
        }
    };

    private void processingShowDialogGender() {
        if (dialogGender == null) {
            dialogGender = DialogUtil.setDialogCustomView(self,R.layout.dialog_choose_gender, false );
            rbMale = (RadioButton) dialogGender.findViewById(R.id.rb_male);
            rbFemale = (RadioButton) dialogGender.findViewById(R.id.rb_female);
        }


        if (mTxtGenre.getText().equals(getResources().getString(R.string.male))) {
            rbMale.setChecked(true);
        } else if (mTxtGenre.getText().equals(getResources().getString(R.string.female))) {
            rbFemale.setChecked(true);
        }
        dialogGender.show();

        rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtGenre.setText(getResources().getString(R.string.male));
                dialogGender.dismiss();

            }
        });

        rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtGenre.setText(getResources().getString(R.string.female));
                dialogGender.dismiss();
            }
        });

    }

    private void processingShowDialogDate() {
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.e("date", "y/m/d=" + year + "/" + monthOfYear + "/" + dayOfMonth );
                mTxtBrithday.setText(DateTimeUtil.convertDateToString(myCalendar.getTime(), DateTimeUtil.FORMAT_DATE));
            }

        };

        new DatePickerDialog(self,  R.style.MyDatePickerStyle, date,2000 /* myCalendar
                .get(Calendar.YEAR)*/, myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        Log.e("date1", "y/m/d=" + myCalendar
                .get(Calendar.YEAR) + "/" + myCalendar
                .get(Calendar.MONTH) + "/" + myCalendar
                .get(Calendar.DAY_OF_MONTH) );
    }

    private void showScreenStep1() {
        tabIndicatorLine.setSteps(TabIndicatorLine.Steps.STEPS1);
        llStep2.setVisibility(View.GONE);
//        imgSignUp.setImageResource(R.drawable.ic_piggybank);
//        imgSignUp.setVisibility(View.VISIBLE);
        imgAvatar.setVisibility(View.GONE);
        mLblCreateAccount.setText(R.string.register_);

        llStep1.setVisibility(View.VISIBLE);
        llStep2.setVisibility(View.GONE);
        llStep3.setVisibility(View.GONE);
        tvEg.setVisibility(View.GONE);
    }

    private void showScreenStep2() {
        tabIndicatorLine.setSteps(TabIndicatorLine.Steps.STEPS2);
//        imgSignUp.setImageResource(R.drawable.ic_call_text);
//        imgSignUp.setVisibility(View.VISIBLE);
        imgAvatar.setVisibility(View.GONE);
        mLblCreateAccount.setText(R.string.continue_);
        tvPhoneCode.setOnClickListener(this);
//        mTxtAddress.setText("");
//        mTxtZipcode.setText("");
//        mTxtGenre.setText("");
        llStep1.setVisibility(View.GONE);
        llStep2.setVisibility(View.VISIBLE);
        llStep3.setVisibility(View.GONE);
        tvEg.setVisibility(View.VISIBLE);
    }

    private void showScreenStep3() {
        tabIndicatorLine.setSteps(TabIndicatorLine.Steps.STEPS3);
        imgAvatar.setVisibility(View.VISIBLE);
//      imgSignUp.setVisibility(View.GONE);
        mLblCreateAccount.setText(R.string.start_now);
//        mTxtBrithday.setText("");
//        mTxtPhoneNumber.setText("");
        llStep1.setVisibility(View.GONE);
        llStep2.setVisibility(View.GONE);
        llStep3.setVisibility(View.VISIBLE);
        tvEg.setVisibility(View.GONE);
    }

    @Override
    void initControl() {
        imgAvatar.setOnClickListener(this);
        mTxtAddress.setOnClickListener(this);
        mLblCreateAccount.setOnClickListener(this);
        mLblAlreadyAMember.setOnClickListener(this);
        //tvPhoneCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTxtAddress) {

        } else if (v == imgAvatar) {
            chooseImage();
        } else if (v == mLblCreateAccount) {
            //Check processing in steps?
            if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS1) {
                if (isValidStep1())
                    showScreenStep2();
            } else if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS2) {
                if (isValidStep2()){
                    showScreenStep3();
                }
            } else if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS3) {
                if (isValidStep3()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(phone);
                    builder.setMessage(R.string.confirm_phone);
                    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        dialogInterface.dismiss();

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
                   // createAccount();
                }
            }

        } else if (v == mLblAlreadyAMember) {
            GlobalFunctions.startActivityWithoutAnimation(self, SplashLoginActivity.class);
            finish();
        }
        else if (v == tvPhoneCode) {
//            tvPhoneCode.setBackgroundResource(R.drawable.bg_border_grey);
            GlobalFunctions.startActivityForResult(this, PhoneCountryListActivity.class, Args.RQ_GET_PHONE_CODE);
        }
    }

    private void chooseImage() {
        AppUtil.pickImage(this, AppUtil.PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AppUtil.PICK_IMAGE_REQUEST_CODE) {
                AppUtil.setImageFromUri(imgAvatar, data.getData());
            }
            else if(requestCode==Args.RQ_SMS){
                createAccount();
            }
            else if (requestCode == Args.RQ_GET_PHONE_CODE) {
                countryCodeSelected = data.getExtras().getString(Args.KEY_PHONE_CODE);
                tvPhoneCode.setText(countryCodeSelected);
            }
            else if (requestCode == RC_ADDRESS) {
                if (resultCode == -1) {
                    Place place = PlaceAutocomplete.getPlace(self, data);
                    AppUtil.fillAddress(self, mTxtAddress, place);
                    mTxtZipcode.requestFocus();
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(self, data);
                    Log.e(TAG, status.getStatusMessage());
                    mTxtAddress.requestFocus();
                } else if (resultCode == 0) {
                    mTxtAddress.requestFocus();
                    // The user canceled the operation.
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS3) {
            showScreenStep2();
        } else if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS2) {
            showScreenStep1();
        } else if (tabIndicatorLine.getSteps() == TabIndicatorLine.Steps.STEPS1) {
            GlobalFunctions.startActivityWithoutAnimation(self, SplashLoginActivity.class);
            finish();
        }

    }

    private void getCountryCode() {
        String[] rl = getResources().getStringArray(R.array.CountryCodes);
        //int curPosition = AppUtil.getCurentPositionCountryCode(this);
        int curPosition = 0;
        String phoneCode = rl[curPosition].split(",")[0];
        //tvPhoneCode.setText(phoneCode);
    }

    private void gotoLoginPage() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Args.IS_FROM_SIGNUP, true);
        if (mIsRegistered && mChkRememberMe.isChecked()) {
            String email = mTxtEmail.getText().toString().trim();
            String password = mTxtPassword.getText().toString().trim();

            if (!email.isEmpty()) {
                bundle.putString(Args.EMAIL, email);
            }
            if (!password.isEmpty()) {
                bundle.putString(Args.PASSWORD, password);
            }

        }

        GlobalFunctions.startActivityWithoutAnimation(self, LoginActivity.class, bundle);
        finish();
    }

    private void createAccount() {
        if (NetworkUtility.getInstance(self).isNetworkAvailable()) {

            ModelManager.registerAccount(self, Volley.newRequestQueue(self), mFullName, mEmail,
                    phone, mPassword, mAddress, mZipcode, mGender, mBirthday, avatar, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            try {
                                JSONObject jsonObject = new JSONObject(object.toString());
                                ApiResponse response = new ApiResponse(jsonObject);
                                if (!response.isError()) {
                                    mIsRegistered = true;
                                    if (mChkRememberMe.isChecked()) {
                                        UserObj userObj = new UserObj();
                                        userObj.setName(mFullName);
                                        userObj.setEmail(mEmail);
                                        userObj.setPhone(phone);
                                        userObj.setPassWord(mPassword);
                                        userObj.setRememberMe(mChkRememberMe.isChecked());
                                        DataStoreManager.saveUser(userObj);
                                        AddressManager.getInstance().getArray().clear();
                                        AddressManager.getInstance().addItem(new Person(mFullName, phone, mAddress));
                                    }
                                    Toast.makeText(self, R.string.msg_register_success, Toast.LENGTH_LONG).show();
                                    gotoLoginPage();
                                } else {
                                    Toast.makeText(self, response.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
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

    private boolean isValidStep1() {
        mFullName = mTxtFullName.getText().toString().trim();
        mEmail = mTxtEmail.getText().toString().trim();
        mPassword = mTxtPassword.getText().toString().trim();
        mRetypePassword = mTxtRetypePassword.getText().toString().trim();

        if (mFullName.isEmpty()) {
            AppUtil.showToast(self, R.string.msg_name_is_required);
            mTxtFullName.requestFocus();

            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            AppUtil.showToast(self, R.string.msg_email_is_required);
            mTxtEmail.requestFocus();

            return false;
        }

        if (!StringUtil.isValidatePassword(mPassword)) {
            AppUtil.showToast(self, R.string.msg_password_is_required);
            mTxtPassword.requestFocus();

            return false;
        }
        if (!mRetypePassword.equals(mPassword)) {
            AppUtil.showToast(self, R.string.msg_password_is_not_match);
            mTxtRetypePassword.requestFocus();

            return false;
        }

        return true;
    }

    private boolean isValidStep2() {

        mPhoneNumber = mTxtPhoneNumber.getText().toString().trim();
        mAddress = mTxtAddress.getText().toString().trim();
        mZipcode = mTxtZipcode.getText().toString().trim();

        if (mPhoneNumber.isEmpty()) {
            AppUtil.showToast(self, R.string.msg_phone_is_required);
            mTxtPhoneNumber.requestFocus();

            return false;
        }
        if (mAddress.isEmpty()) {
            AppUtil.showToast(self, R.string.msg_address_is_required);
            mTxtAddress.requestFocus();

            return false;
        }
        phone = tvPhoneCode.getText()+ mTxtPhoneNumber.getText().toString().replaceAll("\\s+", "");
        if (!Patterns.PHONE.matcher(phone).matches()) {
            AppUtil.showToast(self, R.string.validation_req_phone_valid);
            return false;
        }
//        if (mZipcode.isEmpty()) {
//            AppUtil.showToast(self, R.string.msg_zipcode_is_required);
//            mTxtZipcode.requestFocus();
//
//            return false;
//        }
        return true;
    }

    private boolean isValidStep3() {
        if (imgAvatar.getDrawable() != getResources().getDrawable(R.drawable.ic_cam)) {
            avatar = new DataPart("avatar.png", AppUtil.getFileDataFromDrawable(self, imgAvatar.getDrawable()), DataPart.TYPE_IMAGE);
        }
        mGender = mTxtGenre.getText().toString().trim();
        mBirthday = mTxtBrithday.getText().toString().trim();

        if (imgAvatar == null) {
            AppUtil.showToast(self, R.string.msg_image_is_required);

            return false;
        }
        if (mGender.isEmpty()) {
            AppUtil.showToast(self, R.string.msg_gneder_is_required);
            mTxtGenre.requestFocus();

            return false;
        }
        if (mBirthday.isEmpty()) {
            AppUtil.showToast(self, R.string.msg_birthday_is_required);
            mTxtBrithday.requestFocus();

            return false;
        }
        return true;
    }

    private boolean isValid() {
        String fullName = mTxtFullName.getText().toString().trim();
        String email = mTxtEmail.getText().toString().trim();
        String phoneNumber = mTxtPhoneNumber.getText().toString().trim();
        String password = mTxtPassword.getText().toString().trim();
        String retypePassword = mTxtRetypePassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            Toast.makeText(self, R.string.msg_name_is_required, Toast.LENGTH_SHORT).show();
            mTxtFullName.requestFocus();

            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(self, R.string.msg_email_is_required, Toast.LENGTH_SHORT).show();
            mTxtEmail.requestFocus();

            return false;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(self, R.string.msg_phone_is_required, Toast.LENGTH_SHORT).show();
            mTxtPhoneNumber.requestFocus();

            return false;
        }
        if (!StringUtil.isValidatePassword(password)) {
            Toast.makeText(self, R.string.msg_password_is_required, Toast.LENGTH_LONG).show();
            mTxtPassword.requestFocus();

            return false;
        }
        if (!retypePassword.equals(password)) {
            Toast.makeText(self, R.string.msg_password_is_not_match, Toast.LENGTH_SHORT).show();
            mTxtRetypePassword.requestFocus();

            return false;
        }

        return true;
    }

    private void getListCode() {

        Map<String, String> languagesMap = new TreeMap<>();

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale obj : locales) {

            if ((obj.getDisplayCountry() != null) && (!"".equals(obj.getDisplayCountry()))) {
                languagesMap.put(obj.getCountry(), obj.getLanguage());
                Log.e(TAG, "country: " + obj.getCountry());
            }

        }
    }
}
