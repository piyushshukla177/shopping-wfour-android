package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.interfaces.IChat;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.ContactObj;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.view.activities.RecentChatsActivity;
import com.wfour.onlinestoreapp.view.adapters.ContactAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 14/12/2016.
 */

public class ContactFragment extends com.wfour.onlinestoreapp.base.BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ContactFragment.class.getSimpleName();
    private SwipeRefreshLayout mSw;
    private RecyclerView rcvData;
    private ContactAdapter adapter;
    private ArrayList<ContactObj> listContacts;

    private LinearLayout mLlNoData, mLlNoConnection;

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void init() {
        listContacts = new ArrayList<>();
        adapter = new ContactAdapter(getActivity(), listContacts, new IChat() {
            @Override
            public void onUserClicked(Object obj) {
                QBUser qbUser = ((ContactObj) obj).getQbUser();

                RecentChatObj recentChatObj = new RecentChatObj(null, null, qbUser);
                RecentChatsActivity.start(getActivity(), recentChatObj);
            }

            @Override
            public void onActionClicked(View view, Object obj) {
                showPopupMenu((ContactObj) obj, view);
            }
        });
    }

    @Override
    protected void initView(View view) {
        mSw = (SwipeRefreshLayout) view.findViewById(R.id.sw_contacts);
        mSw.setColorSchemeResources(R.color.primary, R.color.colorPrimaryDark, R.color.colorAccent);
        mSw.setOnRefreshListener(this);
        rcvData = (RecyclerView) view.findViewById(R.id.rcv_data);
        setUpRecyclerView();

        mLlNoData = (LinearLayout) view.findViewById(R.id.ll_no_data);
        mLlNoConnection = (LinearLayout) view.findViewById(R.id.ll_no_connection);
    }

    @Override
    protected void getData() {
        getContacts(true);
    }

    @Override
    public void onRefresh() {
        getContacts(false);
    }

    private void setUpRecyclerView() {
        rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcvData.setAdapter(adapter);
    }

    private void getContacts(boolean isProgress) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getContacts(self, isProgress, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;

                    Log.e(TAG, "contacts " + new Gson().toJson(object));
                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        ArrayList<ContactObj> contactObjs = JSONParser.parseContacts(jsonObject);
                        if (contactObjs.size() > 0) {
                            if (listContacts.size() > 0) {
                                listContacts.clear();
                            }

                            listContacts.addAll(contactObjs);

                            // Refresh adapter
                            adapter.notifyDataSetChanged();

                            // Save contacts into preference
                            DataStoreManager.saveContactsList(listContacts);
                        }
                    }

                    // Show/hide 'no data' layout
                    showHideNoDataLayout();

                    mSw.setRefreshing(false);
                }

                @Override
                public void onError() {
                    mSw.setRefreshing(false);
                }
            });
        } else {
            mSw.setRefreshing(false);

            Toast.makeText(self, R.string.msg_no_network, Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupMenu(final ContactObj contactObj, View view) {
        PopupMenu popupMenu = new PopupMenu(self, view);
        popupMenu.inflate(R.menu.menu_contact);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_remove_from_contacts:
                        removeFromContacts(contactObj);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void removeFromContacts(final ContactObj contactObj) {
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.manualContacts(self, Constants.ACTION_REMOVE, contactObj.getFriendId(),
                    true, new ModelManagerListener() {
                        @Override
                        public void onSuccess(Object object) {
                            JSONObject jsonObject = (JSONObject) object;
                            if (JSONParser.responseIsSuccess(jsonObject)) {
                                listContacts.remove(contactObj);
                                adapter.notifyDataSetChanged();

                                showHideNoDataLayout();

                                // Remove contact from preference which just removed from server
                                ArrayList<ContactObj> contactObjs = DataStoreManager.getContactsList();
                                for (int i = 0; i < contactObjs.size(); i++) {
                                    if(contactObj.getFriendId().equals(contactObjs.get(i).getFriendId())){
                                        contactObjs.remove(i);
                                        break;
                                    }
                                }
                                DataStoreManager.saveContactsList(contactObjs);
                            } else {
                                Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void showHideNoDataLayout() {
        if (listContacts.size() > 0) {
            mLlNoData.setVisibility(View.GONE);
        } else {
            mLlNoData.setVisibility(View.VISIBLE);
        }
    }
}
