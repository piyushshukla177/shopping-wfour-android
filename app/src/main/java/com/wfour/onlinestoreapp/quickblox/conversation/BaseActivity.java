package com.wfour.onlinestoreapp.quickblox.conversation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.StringRes;
import android.view.KeyEvent;
import android.view.View;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.quickblox.content.SharedPrefsHelper;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.Consts;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.GooglePlayServicesHelper;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.QBResRequestExecutor;
import com.wfour.onlinestoreapp.utils.ErrorUtils;

/**
 * QuickBlox team
 */
public abstract class BaseActivity extends CoreBaseActivity {

    SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progressDialog;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    protected QBResRequestExecutor requestExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestExecutor = AppController.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();
    }

    public void initDefaultActionBar() {
        String currentUserFullName = "";
        String currentRoomName = sharedPrefsHelper.get(Consts.PREF_CURREN_ROOM_NAME, "");

        if (sharedPrefsHelper.getQbUser() != null) {

        }

        setActionBarTitle(currentRoomName);
        setActionbarSubTitle(String.format(getString(R.string.subtitle_text_logged_in_as), currentUserFullName));
    }


    public void setActionbarSubTitle(String subTitle) {
//        if (actionBar != null)
//            actionBar.setSubtitle(subTitle);
    }

    public void removeActionbarSubTitle() {
//        if (actionBar != null)
//            actionBar.setSubtitle(null);
    }

    void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }

        progressDialog.setMessage(getString(messageId));

        progressDialog.show();

    }

    void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showErrorSnackbar(@StringRes int resId, Exception e,
                                     View.OnClickListener clickListener) {
        if (getSnackbarAnchorView() != null) {
            ErrorUtils.showSnackbar(getSnackbarAnchorView(), resId, e,
                    R.string.retry, clickListener);
        }
    }

    protected abstract View getSnackbarAnchorView();
}




