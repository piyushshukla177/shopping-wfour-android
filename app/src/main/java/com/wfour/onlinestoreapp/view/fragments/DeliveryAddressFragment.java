package com.wfour.onlinestoreapp.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.base.BaseFragment;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.interfaces.MyOnClick;
import com.wfour.onlinestoreapp.interfaces.MyOnClickDelivery;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.AddressManager;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.DeliveryObj;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.MainActivity;
import com.wfour.onlinestoreapp.view.adapters.AddressAdapter;
import com.wfour.onlinestoreapp.view.adapters.DeliveryAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveryAddressFragment extends BaseFragment implements View.OnClickListener {

    public static DeliveryAddressFragment newInstance() {
        Bundle bundle = new Bundle();
        DeliveryAddressFragment fragment = new DeliveryAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Toolbar toolbar;
    private TextView btn_Next, btnAddAddress, tvTitle;
    private EditText edtName, edtPhone, edtAddress, edtCity, edtTown;
    private LinearLayout lnl_add;
    private ArrayList<Person> personList;
    private RecyclerView contentRCL;
    private AddressAdapter mAdapter;

    private String name;
    private String phone;
    private String address;
    private String action;
    private int id;
    private String mAddress;

    private IOnItemClickListener onClick;
    private CartActivity mCartActivity;
    private InformationOrderFragment FrgOrder;
    private Person person;
    private Intent intent;

    public static final int ADD_ITEM_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;
    public String ActionType = "";
    public static final String PERSON = "PERSON";
    private InformationOrderFragment informationOrderFragment;
    private SoloveFragment soloveFragment;
    private CartListFragment cartListFragment;

    private DeliveryAddressFragment deliveryAddressFragment;
    private ArrayList<DeliveryObj> deliveryObjList;

    private String city;
    private String town;
    private int position;
    private int count;
    private ArrayList<ProductObj> cartList;
    private DeliveryObj deliveryObj = new DeliveryObj();
    private RecyclerView rcv_data;
    private DeliveryAdapter mAAdapter;
    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_delivery_address;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        mCartActivity = (CartActivity) getActivity();
        setHasOptionsMenu(true);

        toolbar = view.findViewById(R.id.toolbar);
        mCartActivity.setSupportActionBar(toolbar);
        mCartActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCartActivity.setTitle(R.string.delivery_address);

        btn_Next = view.findViewById(R.id.btn_Next);
        btn_Next.setOnClickListener(this);

        lnl_add = view.findViewById(R.id.lnlAdd);
        lnl_add.setOnClickListener(this);

        contentRCL = view.findViewById(R.id.rclView);

        mAdapter = new AddressAdapter(mCartActivity, personList, new MyOnClick() {
            @Override
            public void DeleteItem(int position) {
                AddressManager.getInstance().removeItem(position);
                personList = AddressManager.getInstance().getArray();
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void EditItem(Person person, int position) {
                showDialogInformation();
                edtName.setText(person.getName());
                edtAddress.setText(person.getAddress());
                edtPhone.setText(person.getPhone());
//                edtTown.setText(person.getTown());
//                edtCity.setText(person.getCity());

            }
        });

        mAAdapter = new DeliveryAdapter(self, deliveryObjList, new MyOnClickDelivery() {

            @Override
            public void onClick(int position) {
                deliveryObj = new DeliveryObj();
                deliveryObj = deliveryObjList.get(position);
            }
        });

        initDeliveryList();
        mAdapter.notifyDataSetChanged();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCartActivity);
        contentRCL.setLayoutManager(layoutManager);
        contentRCL.setAdapter(mAdapter);

        if (AddressManager.getInstance().getArray().size() != 0) {
            lnl_add.setVisibility(View.GONE);
        } else {
            lnl_add.setVisibility(View.VISIBLE);
        }
        rcv_data = view.findViewById(R.id.rcvData);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(self);
        rcv_data.setLayoutManager(layoutManager1);
        rcv_data.setAdapter(mAAdapter);
        rcv_data.setNestedScrollingEnabled(false);
         mAAdapter.notifyDataSetChanged();

    }



    private void initDeliveryList(){
        if(NetworkUtility.getInstance(self).isNetworkAvailable()){
            ModelManager.deliveryList(self, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject obj = (JSONObject) object;
                    ApiResponse response = new ApiResponse(obj);
                    deliveryObjList = new ArrayList<>();
                    if(!response.isError()){
                        deliveryObjList.addAll(response.getDataList(DeliveryObj.class));


//                        deliveryObj = deliveryObjList.get(0);
//                        deliveryObj = deliveryObjList.get(1);
//                        deliveryObj = deliveryObjList.get(2);
//                        deliveryObj = deliveryObjList.get(3);

                        mAAdapter.addList(deliveryObjList);
                    }
                    Log.e("TAG", "onError: " + "Success");
                }

                @Override
                public void onError() {
                    Log.e("TAG", "onError: " + "Error");
                }
            });
        }else {
            Toast.makeText(self, getString(R.string.msg_no_network), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_go_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_go_home) {
            GlobalFunctions.startActivityWithoutAnimation(self, MainActivity.class);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void getData() {
        if (DataStoreManager.getUser() != null && AddressManager.getInstance().getArray().size() == 0) {
            name = DataStoreManager.getUser().getName();
            phone = DataStoreManager.getUser().getPhone();
            address = DataStoreManager.getUser().getAddress();
            if(name != null && phone != null && address != null) {
                AddressManager.getInstance().addItem(new Person(name, phone, address));
            }
            personList = AddressManager.getInstance().getArray();
            if(name != null && phone != null && address != null) {
                mAdapter.addList(personList);
            }
        } else {
            personList = AddressManager.getInstance().getArray();
            mAdapter.addList(personList);
        }
        if (AddressManager.getInstance().getArray().size() != 0 ) {
            lnl_add.setVisibility(View.GONE);
        } else {
            lnl_add.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = mCartActivity.getSupportFragmentManager().beginTransaction();
        if (view == btn_Next) {
            if (!deliveryObjList.get(0).isSelected()
                    && !deliveryObjList.get(1).isSelected()
                    && !deliveryObjList.get(2).isSelected()
                    && !deliveryObjList.get(3).isSelected()) {
                Toast.makeText(self, "Choose shipping method", Toast.LENGTH_LONG).show();
                return;
            }

            if (AddressManager.getInstance().getArray().size() != 0) {
                if (soloveFragment == null) {
                    soloveFragment = SoloveFragment.newInstance();
                }
                fragmentTransaction.replace(R.id.frgCart, soloveFragment).addToBackStack("soloveFragment").commit();
            } else {
                Toast.makeText(mCartActivity, "You need to enter the address", Toast.LENGTH_LONG).show();
            }
        } else if (view == lnl_add) {
            if (AddressManager.getInstance().getArray().size() == 0 ) {
                showDialogInformation();
            }
        }
    }

    private void showDialogInformation() {
        final Dialog dialogOrder = DialogUtil.setDialogCustomView(self, R.layout.fragment_information_order, false);
        //dialogOrder.setTitle("hehe");
        TextView btnClose = dialogOrder.findViewById(R.id.btnClose);
        tvTitle = dialogOrder.findViewById(R.id.tvTitle);
        edtName = dialogOrder.findViewById(R.id.edtName);
        edtPhone = dialogOrder.findViewById(R.id.edtPhone);
        edtAddress = dialogOrder.findViewById(R.id.edtAddress);
//        edtTown = dialogOrder.findViewById(R.id.edtTown);
//        edtCity = dialogOrder.findViewById(R.id.edtCity);
        btnAddAddress = dialogOrder.findViewById(R.id.btn_AddAddress);

        personList = AddressManager.getInstance().getArray();
        if (personList.size() != 0) {
            tvTitle.setText(R.string.edit_information);
            btnAddAddress.setText(R.string.update);
            btnAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upData();
                    dialogOrder.dismiss();

                }
            });

        } else {
            tvTitle.setText(R.string.add_infomation);
            btnAddAddress.setText(R.string.add);
            btnAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setData();
                    lnl_add.setVisibility(View.GONE);
                    dialogOrder.dismiss();

                }
            });
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOrder.dismiss();
            }
        });
        dialogOrder.show();
    }

    public void setData() {
        name = edtName.getText().toString();
        phone = String.valueOf(edtPhone.getText().toString());
        address = edtAddress.getText().toString();
//        city = edtCity.getText().toString();
//        town = edtTown.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(self, "You need to enter all the entries", Toast.LENGTH_LONG).show();
        } else {
            AddressManager.getInstance().addItem(new Person(name, phone, address));
            FragmentTransaction fragmentTransaction = mCartActivity.getSupportFragmentManager().beginTransaction();
            if (deliveryAddressFragment == null) {
                deliveryAddressFragment = DeliveryAddressFragment.newInstance();

            }
            //deliveryAddressFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.frgCart, deliveryAddressFragment).commit();
            dimisKeyBoard();
        }


    }

    public void upData() {
        personList = AddressManager.getInstance().getArray();
        personList.set(position, new Person(edtName.getText().toString(), edtPhone.getText().toString(), edtAddress.getText().toString()));

        FragmentTransaction fragmentTransaction = mCartActivity.getSupportFragmentManager().beginTransaction();
        if (deliveryAddressFragment == null) {
            deliveryAddressFragment = DeliveryAddressFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.frgCart, deliveryAddressFragment).commit();
        dimisKeyBoard();

    }

    private void dimisKeyBoard() {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
