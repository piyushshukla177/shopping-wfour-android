package com.wfour.onlinestoreapp.view.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network1.MyProgressDialog;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.objects.TransportObj;
import com.wfour.onlinestoreapp.quickblox.QbAuthUtils;
import com.wfour.onlinestoreapp.quickblox.QbChatDialogMessageListenerImp;
import com.wfour.onlinestoreapp.quickblox.QbDialogHolder;
import com.wfour.onlinestoreapp.quickblox.QbSessionStateCallback;
import com.wfour.onlinestoreapp.quickblox.SharedPreferencesUtil;
import com.wfour.onlinestoreapp.quickblox.chat.ChatHelper;
import com.wfour.onlinestoreapp.quickblox.manager.DialogsManager;
import com.wfour.onlinestoreapp.utils.ErrorUtils;
import com.wfour.onlinestoreapp.utils.map.MapsUtil;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity implements QbSessionStateCallback,
        DialogsManager.ManagingDialogsCallbacks {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected BaseActivity self;

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    protected boolean isAppSessionActive;
    private QBSystemMessagesManager systemMessagesManager;
    private SystemMessagesListener systemMessagesListener;
    private DialogsManager dialogsManager;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private QBIncomingMessagesManager incomingMessagesManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateLayout();
        self = this;
        getExtraValues();
        initUI();
       initControl();

//        // Init quickblox
//        if (DataStoreManager.getUser() != null && (DataStoreManager.getUser().getToken() != null
//                && !DataStoreManager.getUser().getToken().equals(""))) {
//            initSession(savedInstanceState);
//            initDialogsListener();
//            initPushManager();
//        }
    }

    abstract void inflateLayout();

    abstract void initUI();

    abstract void initControl();

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
//        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
//        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
//        updateDialogsAdapter();
    }

    @Override
    public void onSessionCreated(boolean success) {
        if (success) {
            registerQbChatListeners();
            onSessionCreated();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isAppSessionActive) {
            unregisterQbChatListeners();
        }
    }

    protected void onSessionCreated() {
    }

    protected void getExtraValues() {
    }

    protected void showPermissionsReminder(final int reqCode, final boolean flag) {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_remind_user_grants_permissions),
                getString(R.string.allow), getString(R.string.no_thank), false, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        GlobalFunctions.isGranted(self, new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION}, reqCode, null);
                    }

                    @Override
                    public void onNegative() {
                        if (flag) {
                            finish();
                        }
                    }
                });
    }

    public void turnOnLocationReminder(final int reqCode, final boolean flag) {
        GlobalFunctions.showConfirmationDialog(self, getString(R.string.msg_remind_user_turn_on_location),
                getString(R.string.turn_on), getString(R.string.no_thank), false, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        MapsUtil.displayLocationSettingsRequest(self, reqCode);
                    }

                    @Override
                    public void onNegative() {
                        if (flag) {
                            finish();
                        }
                    }
                });
    }

    protected int getTransportIcon(TransportDealObj transportDealObj) {
        int icon = R.drawable.ic_taxi_type;
        if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.TAXI)) {
            if (transportDealObj.driverIsDelivery()) {
                icon = R.drawable.ic_taxi_delivery_type;
            } else {
                icon = R.drawable.ic_taxi_type;
            }
        } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.VIP)) {
            if (transportDealObj.driverIsDelivery()) {
                icon = R.drawable.ic_vip_delivery_type;
            } else {
                icon = R.drawable.ic_vip_type;
            }
        } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.LIFTS)) {
            if (transportDealObj.driverIsDelivery()) {
                icon = R.drawable.ic_lifts_delivery_type;
            } else {
                icon = R.drawable.ic_lifts_type;
            }
        } else if (transportDealObj.getTransportType().equalsIgnoreCase(TransportObj.MOTORBIKE)) {
            if (transportDealObj.driverIsDelivery()) {
                icon = R.drawable.ic_moto_delivery_type;
            } else {
                icon = R.drawable.ic_moto_type;
            }
        }

        return icon;
    }

    protected Snackbar showErrorSnackbar(@StringRes int resId, View view, Exception e,
                                         View.OnClickListener clickListener) {
        return ErrorUtils.showSnackbar(view, resId, e, R.string.retry, clickListener);
    }

    protected void initSession(Bundle savedInstanceState) {
        boolean wasAppRestored = savedInstanceState != null;
        boolean isQbSessionActive = QbAuthUtils.isSessionActive();
        final boolean needToRestoreSession = wasAppRestored || !isQbSessionActive;
        Log.e(TAG, "wasAppRestored = " + wasAppRestored);
        Log.e(TAG, "isQbSessionActive = " + isQbSessionActive);

        // Triggering callback via Handler#post() method
        // to let child's code in onCreate() to execute first
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (needToRestoreSession) {
                    recreateChatSession();
                    isAppSessionActive = false;
                } else {
                    onSessionCreated(true);
                    isAppSessionActive = true;
                }
            }
        });
    }

    private void recreateChatSession() {
        QBUser user = SharedPreferencesUtil.getQbUser();
        if (user == null) {
            throw new RuntimeException("User is null, can't restore session");
        }

        reLoginToChat(user);
    }

    private void reLoginToChat(final QBUser user) {
        ChatHelper.getInstance().login(getApplicationContext(), user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()");
                isAppSessionActive = true;
                onSessionCreated(true);
            }

            @Override
            public void onError(QBResponseException e) {
                isAppSessionActive = false;
                Log.w(TAG, "Chat login onError(): " + e);
                // Auto relogin to chat after 1 second
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reLoginToChat(user);
                    }
                }, 1000);

                onSessionCreated(false);
            }
        });
    }

    protected void initDialogsListener() {
        if (isAppSessionActive) {
            allDialogsMessagesListener = new AllDialogsMessageListener();
            systemMessagesListener = new SystemMessagesListener();
        }

        dialogsManager = new DialogsManager();
    }

    public void createDialog(final ArrayList<QBUser> selectedUsers, final RecentChatObj obj, final MyProgressDialog progressDialog) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        ChatActivityReskin2.start(self, dialog.getDialogId(), obj);

                        // Reset last message to avoid showing dialog again
                        if (obj.getLastMessage() != null) {
                            obj.setLastMessage("");
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Toast.makeText(self, R.string.dialogs_creation_error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
//        ArrayList<QBUser> selectedUsers = new ArrayList<>();
//        selectedUsers.addAll(allSelectedUsers);
//        selectedUsers.remove(ChatHelper.getCurrentUser());
        return allSelectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(allSelectedUsers.get(0));
    }

    protected void registerQbChatListeners() {
        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    protected void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    protected void initPushManager() {
        Log.e(TAG, "initPushManager");
        QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
            @Override
            public void onSubscriptionCreated() {
                Log.e(TAG, "SubscriptionCreated");
            }

            @Override
            public void onSubscriptionError(Exception e, int resultCode) {
                Log.e(TAG, "SubscriptionError" + e.getLocalizedMessage());
                if (resultCode >= 0) {
                    String error = GoogleApiAvailability.getInstance().getErrorString(resultCode);
                    Log.e(TAG, "SubscriptionError playServicesAbility: " + error);
                }
            }
        });
    }

    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String from = intent.getStringExtra("from");
            Log.e(TAG, "Receiving message: " + message + ", from " + from);
        }
    };

    protected void registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(pushBroadcastReceiver,
                new IntentFilter("new-push-event"));
    }

    protected void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushBroadcastReceiver);
    }
}
