package com.wfour.onlinestoreapp.view.chat.groupchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.fragments.ContactFragment;
import com.wfour.onlinestoreapp.view.fragments.DealManagerFragment;
import com.wfour.onlinestoreapp.view.fragments.DealsFragment;
import com.wfour.onlinestoreapp.view.fragments.FragmentFavorite;
import com.wfour.onlinestoreapp.view.fragments.FragmentWhisList;
import com.wfour.onlinestoreapp.view.fragments.HomeFragment;
import com.wfour.onlinestoreapp.view.fragments.IwanaPayFragment;
import com.wfour.onlinestoreapp.view.fragments.MyAccountMyInfoFragment;
import com.wfour.onlinestoreapp.view.fragments.MyDealFragment;
import com.wfour.onlinestoreapp.view.fragments.NewsFragment;
import com.wfour.onlinestoreapp.view.fragments.ProducerManagerFragment;
import com.wfour.onlinestoreapp.view.fragments.SellerFragment;
import com.wfour.onlinestoreapp.view.fragments.SettingsFragment;
import com.wfour.onlinestoreapp.view.fragments.WebViewFragment;


public class GroupChannelActivity extends AppCompatActivity {
    private Toolbar toolbar;
    final static String TAG = GroupChannelActivity.class.getSimpleName();
    protected static ProductObj item;
    private String id;
    protected static int idUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

        if (savedInstanceState == null) {
            // Load list of Group Channels
            Fragment fragment = GroupChannelListFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .commit();
        }

        if (savedInstanceState != null) {
            MainActivity.mHomeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "allDeals");
        }

        String channelUrl = getIntent().getStringExtra("groupChannelUrl");
        if (channelUrl != null) {
            // If started from notification

            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        Intent intent = getIntent();
        item = intent.getExtras().getParcelable(Args.KEY_DEAL_OBJECT);
//        id = intent.getStringExtra("userId");
//        idUser = Integer.parseInt(id);
//        if (idUser == 1 && DataStoreManager.getUser().getRole() == 30) {
//            AppUtil.showToast(getApplicationContext(), "admin");
//        }else if(DataStoreManager.getUser().getRole() != 30 && idUser != 1){
//            item = intent.getExtras().getParcelable(Args.KEY_DEAL_OBJECT);
//        }


    }


    interface onBackPressedListener {
        boolean onBack();
    }

    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            MainActivity.nav_main.getMenu().findItem(R.id.home_menu).setChecked(true);
            finish();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
