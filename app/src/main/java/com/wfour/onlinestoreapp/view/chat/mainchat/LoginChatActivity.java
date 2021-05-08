package com.wfour.onlinestoreapp.view.chat.mainchat;

import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.BaseActivity;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.view.chat.chatutils.PreferenceUtils;
import com.wfour.onlinestoreapp.view.chat.chatutils.PushUtils;
import com.wfour.onlinestoreapp.view.chat.groupchat.GroupChannelActivity;

public class LoginChatActivity extends BaseActivity {

    final static String TAG = LoginChatActivity.class.getSimpleName();
    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUserIdConnectEditText, mUserNicknameEditText;
    private Button mConnectButton;
    private ContentLoadingProgressBar mProgressBar;
    private ProductObj item;

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_chat_login;
    }

    @Override
    protected void getExtraData(Intent intent) {
        if (DataStoreManager.getUser().getRole() == 30) {
            item = new ProductObj();
            item = null;
        }else if (DataStoreManager.getUser().getRole() != 30){
            if (intent.getExtras().getParcelable(Args.KEY_DEAL_OBJECT) == null) {
                item = new ProductObj();
                item = null;
            }else {
                item = intent.getExtras().getParcelable(Args.KEY_DEAL_OBJECT);
                Log.e(TAG, "getExtraData: "+ item.getTitle());
            }

        }
    }

    @Override
    protected void initilize() {

    }

    @Override
    protected void initView() {

        mLoginLayout = findViewById(R.id.layout_login);

        mUserIdConnectEditText = findViewById(R.id.edittext_login_user_id);
        mUserNicknameEditText  = findViewById(R.id.edittext_login_user_nickname);
//        mUserIdConnectEditText.setText(DataStoreManager.getUser().getId());

        if (DataStoreManager.getUser().getRole() == 30) {
            mUserIdConnectEditText.setText("1");
        } else if (DataStoreManager.getUser().getRole() == 30 && DataStoreManager.getUser().getId() == String.valueOf(1)) {
            mUserIdConnectEditText.setText("1");
        }else if (DataStoreManager.getUser().getId() == String.valueOf(1) && DataStoreManager.getUser().getRole() != 30) {
            int id = 1 + 9998;
            mUserIdConnectEditText.setText(String.valueOf(id));
        }else {
            mUserIdConnectEditText.setText(DataStoreManager.getUser().getId());
        }

        if (DataStoreManager.getUser().getName().isEmpty()) {
            AppUtil.showToast(self, R.string.enter_you_name);
            return;
        }else if (DataStoreManager.getUser().getName() != "") {
            mUserNicknameEditText.setText(DataStoreManager.getUser().getName());
        }
//        mUserNicknameEditText.setText(DataStoreManager.getUser().getEmail());

//        mUserIdConnectEditText.setText(PreferenceUtils.getUserId(this));
//        mUserNicknameEditText.setText(PreferenceUtils.getNickname(this));

        mConnectButton = findViewById(R.id.button_login_connect);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserIdConnectEditText.getText().toString();
                // Remove all spaces from userID
                userId = userId.replaceAll("\\s", "");
                String userNickname = mUserNicknameEditText.getText().toString();
                PreferenceUtils.setUserId(self, userId);
                PreferenceUtils.setNickname(self, userNickname);

                connectToSendBird(userId, userNickname);

            }
        });

        mUserIdConnectEditText.setSelectAllOnFocus(true);
        mUserNicknameEditText.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar =  findViewById(R.id.progress_bar_login);

        // Display current SendBird and app versions in a TextView
        String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
                AppController.VERSION, SendBird.getSDKVersion());
        ((TextView) findViewById(R.id.text_login_versions)).setText(sdkVersion);

    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PreferenceUtils.getConnected(self)) {
            connectToSendBird(PreferenceUtils.getUserId(self), PreferenceUtils.getNickname(this));
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */

    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        showProgressBar(true);
        mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                showProgressBar(false);

                if (e != null) {
                    // Error!
                    AppUtil.showToast(self, "" + e.getCode() + ": " + e.getMessage());

                    // Show login failure snackbar
                    showSnackbar("Login to SendBird failed");
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(self, false);
                    return;
                }

                PreferenceUtils.setNickname(self, user.getNickname());
                PreferenceUtils.setProfileUrl(self, user.getProfileUrl());
                PreferenceUtils.setConnected(self, true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                // Proceed to MainActivity
                Log.e(TAG, "onConnected: " + userId + "getUserID" + user.getUserId());

//                Intent intent = new Intent(self, GroupChannelActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Args.KEY_DEAL_OBJECT, LoginChatActivity.this.item);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();

                if ("1".equals(user.getUserId()) && DataStoreManager.getUser().getRole() == 30) {
                    Intent intent = new Intent(self, GroupChannelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", user.getUserId());
                    bundle.putParcelable(Args.KEY_DEAL_OBJECT, LoginChatActivity.this.item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else if (DataStoreManager.getUser().getRole() != 30){
                    Intent intent = new Intent(self, GroupChannelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", user.getUserId());
                    bundle.putParcelable(Args.KEY_DEAL_OBJECT, LoginChatActivity.this.item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(self, null);
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginChatActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    showSnackbar("Update user nickname failed");

                    return;
                }

                PreferenceUtils.setNickname(LoginChatActivity.this, userNickname);
            }
        });
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }
}
