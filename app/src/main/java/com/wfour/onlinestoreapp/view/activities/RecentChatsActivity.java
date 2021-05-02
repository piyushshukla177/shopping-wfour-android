package com.wfour.onlinestoreapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.view.adapters.RecentChatAdapter;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.interfaces.IChat;
import com.wfour.onlinestoreapp.network1.MyProgressDialog;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.quickblox.QbDialogHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecentChatsActivity extends BaseActivity {

    private static final String TAG = RecentChatsActivity.class.getSimpleName();

    private static final int RC_UPDATE_CONTACT = 999;

    private MyProgressDialog myProgressDialog;

    private RecyclerView mRclContact;
    private RecentChatAdapter mAdapter;
    private ArrayList<RecentChatObj> mContacts;
    private RecentChatObj mRecentChatObj;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraValues(getIntent());

        // Init quickblox
        if (DataStoreManager.getUser() != null && (DataStoreManager.getUser().getToken() != null
                && !DataStoreManager.getUser().getToken().equals(""))) {
            initSession(savedInstanceState);
            initDialogsListener();
            initPushManager();
        }
    }

    @Override
    void inflateLayout() {
        setContentView(R.layout.activity_recent_chats);
    }

    @Override
    void initUI() {
        // Show as up button
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        try {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        mRclContact = (RecyclerView) findViewById(R.id.rcv_data);
        mRclContact.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));

        // Init adapter first
        if (mContacts == null) {
            mContacts = new ArrayList<>();
        } else {
            mContacts.clear();
        }
        mContacts.addAll(DataStoreManager.getRecentChat());
        // Refresh adapter
        Collections.sort(mContacts, new Comparator<RecentChatObj>() {
            @Override
            public int compare(RecentChatObj obj, RecentChatObj t1) {
                return String.valueOf(t1.getTime()).compareTo(String.valueOf(obj.getTime()));
            }
        });

        mAdapter = new RecentChatAdapter(self, mContacts, new IChat() {
            @Override
            public void onUserClicked(Object obj) {
                openChat((RecentChatObj) obj);
                Log.e(TAG, "onUserClicked mRecentChatObj " + new Gson().toJson(obj));
            }

            @Override
            public void onActionClicked(View view, Object obj) {
                // Do nothing
            }
        });
        mRclContact.setAdapter(mAdapter);
    }

    @Override
    void initControl() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_UPDATE_CONTACT) {
            if (resultCode == RESULT_OK) {
                RecentChatObj obj = data.getParcelableExtra(Args.RECENT_CHAT_OBJ);
                if (obj != null) {
                    QBUser qbUser = obj.getQbUser();

                    for (int i = 0; i < mContacts.size(); i++) {
                        if (mContacts.get(i).getQbUser().getId().equals(qbUser.getId())) {
                            // Update this item
                            mContacts.get(i).setTransportDealObj(obj.getTransportDealObj());
                            mContacts.get(i).setDealObj(obj.getDealObj());
                            Log.e(TAG, "onActivityResult RC_UPDATE_CONTACT mRecentChatObj " + new Gson().toJson(obj));
                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onSessionCreated() {
        super.onSessionCreated();

        if (mRecentChatObj != null) {
            openChat(mRecentChatObj);
            Log.e(TAG, "onSessionCreated mRecentChatObj " + new Gson().toJson(mRecentChatObj));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getExtraValues(intent);
    }

    @Override
    public void onBackPressed() {
        // Reset recent chat list
        DataStoreManager.clearRecentChat();

        super.onBackPressed();
    }

    private void getExtraValues(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Args.RECENT_CHAT_OBJ)) {
                mRecentChatObj = bundle.getParcelable(Args.RECENT_CHAT_OBJ);

                // Add to recent chat list and refresh view
                addToRecentChat(mRecentChatObj, true);
            }
        }
    }

    private void openChat(RecentChatObj obj) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.add(obj.getQbUser());
//        /*if (obj.justForChatting()) {
//            selectedUsers.add(obj.getQbUser());
//        } else if (obj.getTransportDealObj() != null) {
//            QBUser qbUser = new QBUser();
//            if (!obj.getTransportDealObj().getDriverId().equals(DataStoreManager.getUser().getId())) {
//                qbUser.setId(obj.getTransportDealObj().getDriverQBId());
//                qbUser.setFullName(obj.getTransportDealObj().getDriverName());
//                qbUser.setPhone(obj.getTransportDealObj().getDriverPhone());
//            } else {
//                qbUser = obj.getQbUser();
//            }
//            selectedUsers.add(qbUser);
//        } else if (obj.getDealObj() != null) {
//            QBUser qbUser = new QBUser();
//            if (!obj.getDealObj().getSeller_id().equals(DataStoreManager.getUser().getId())) {
//                qbUser.setId(obj.getDealObj().getSellerQbId());
//                qbUser.setFullName(obj.getDealObj().getProData().getname());
//                qbUser.setPhone(obj.getDealObj().getProData().getBusiness_phone());
//            } else {
//                qbUser = obj.getQbUser();
//            }
//            selectedUsers.add(qbUser);
//        }*/

        if (isPrivateDialogExist(selectedUsers)) {
            // Dismiss progress dialog
            if (myProgressDialog.isShowing()) {
                myProgressDialog.dismiss();
            }

            QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
            ChatActivityReskin2.startForResult(self, existingPrivateDialog.getDialogId(), obj, RC_UPDATE_CONTACT);

            Log.e(TAG, "openChat mRecentChatObj " + new Gson().toJson(obj));

            // Reset last message to avoid showing dialog again
            if (obj.getLastMessage() != null) {
                obj.setLastMessage("");
                addToRecentChat(obj, false);
            }
        } else {
            createDialog(selectedUsers, obj, myProgressDialog);
        }

        // Keep current chat
        DataStoreManager.saveCurrentChat(obj);
    }

    private void addToRecentChat(RecentChatObj obj, boolean showProgress) {
        if (myProgressDialog == null && showProgress) {
            myProgressDialog = new MyProgressDialog(self);
        }

        if (myProgressDialog != null && !myProgressDialog.isShowing() && showProgress) {
            myProgressDialog.show();
            myProgressDialog.setMessage(getString(R.string.creating_chat_dialog));
        }

        if (obj != null) {
            QBUser qbUser = obj.getQbUser();

            if (obj.getTransportDealObj() != null) {
                if (!obj.getTransportDealObj().getDriverId().equals(DataStoreManager.getUser().getId())) {
                    qbUser.setId(obj.getTransportDealObj().getDriverQBId());
//                    qbUser.setId(80813);
                    qbUser.setFullName(obj.getTransportDealObj().getDriverName());
                    qbUser.setPhone(obj.getTransportDealObj().getDriverPhone());
                }
            } else if (obj.getDealObj() != null) {
                if (!obj.getDealObj().getSeller_id().equals(DataStoreManager.getUser().getId())) {
                    qbUser.setId(obj.getDealObj().getSellerQbId());
//                    qbUser.setId(80813);
                    qbUser.setFullName(obj.getDealObj().getProData().getname());
                    qbUser.setPhone(obj.getDealObj().getProData().getBusiness_phone());
                }
            }

            for (int i = 0; i < mContacts.size(); i++) {
                if (mContacts.get(i).getQbUser().getId().equals(qbUser.getId())) {
                    // Remove old obj
                    mContacts.remove(i);
                    break;
                }
            }

            // Add new obj
            DataStoreManager.addToRecentChat(obj);
            mContacts.clear();
            mContacts.addAll(DataStoreManager.getRecentChat());

            // Refresh adapter
            Collections.sort(mContacts, new Comparator<RecentChatObj>() {
                @Override
                public int compare(RecentChatObj obj, RecentChatObj t1) {
                    return String.valueOf(t1.getTime()).compareTo(String.valueOf(obj.getTime()));
                }
            });
            mAdapter.notifyDataSetChanged();
        }
    }

    public static void start(Activity activity, RecentChatObj obj) {
        Intent intent = new Intent(activity, RecentChatsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Args.RECENT_CHAT_OBJ, obj);
        activity.startActivity(intent);
    }
}
