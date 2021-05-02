package com.wfour.onlinestoreapp.view.Dialogs;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.network.modelmanager.singletonmanager.CartManager;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.PropetiesObj;
import com.wfour.onlinestoreapp.quickblox.conversation.utils.DialogUtil;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.activities.DealDetailActivity;
import com.wfour.onlinestoreapp.view.activities.LoginActivity;
import com.wfour.onlinestoreapp.view.adapters.ColorProductAdapter;
import com.wfour.onlinestoreapp.view.adapters.SizeProductAdapter;
import com.wfour.onlinestoreapp.view.fragments.CartListFragment;

import java.util.ArrayList;

public class Dialog_Cart extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String COLOR = "color";
    public static final String SIZE = "size";
    private TextView btnClose, tvName, btnGoCart, tvPrice,tvOldPrice;
    private ImageView img;
    private Activity mdealDetailActivity;
    private Bundle bundle;
    public static final String KEY1 = "key1";
    public static final String KEY2 = "key2";
    public static final String KEY3 = "key3";
    private ProductObj item;
    private CartListFragment FrgCartLits;
    private CartListFragment mCartListFragment;
    private RecyclerView rcvSize, rcvColor, rcvData;
    //private PropetiesListAdapter colorAdapter;
    private ArrayList<String> sizeList, colorLits;
    private Activity activity;
    private ColorProductAdapter colorAdapter;
    private SizeProductAdapter sizeAdapter;
    private String color, size;
    private LinearLayout lnlColor, lnlSize, lnl1, lnl2;
    private ArrayList<ProductObj> productObjList;
    //private ProductObj cart;
    private int count;
    private CartObj cart;
    private ArrayList<CartObj> cartObjList;

    //     public static Dialog_Cart newInstance(){
//
//     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_cart, container, false);

        mdealDetailActivity = (Activity) getActivity();

        getActivity().getWindow().setBackgroundDrawableResource(R.color.transparent);

        btnClose = v.findViewById(R.id.btnClose);
        btnGoCart = v.findViewById(R.id.btnGoCart);
        tvName = v.findViewById(R.id.tvName);
        tvPrice = v.findViewById(R.id.tvPrice);
        tvOldPrice = v.findViewById(R.id.lbl_price_old);
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        img = v.findViewById(R.id.img);
        lnlColor = v.findViewById(R.id.lnlColor);
        lnlSize = v.findViewById(R.id.lnlSize);
        lnl1 = v.findViewById(R.id.lnl1);
        lnl2 = v.findViewById(R.id.lnl2);

        bundle = getArguments();

        if (bundle != null) {
            item = bundle.getParcelable(Args.KEY_PRODUCT_OBJECT);
            tvName.setText(item.getTitle());
            if(item.getIs_prize()==1){
                tvPrice.setText(" P."+StringUtil.convertNumberToString(item.getPrice(),0));
                btnGoCart.setText(R.string.troka_poin);
            }
            else {
                tvPrice.setText(" $."+StringUtil.convertNumberToString(item.getPrice(),2));
            }

            if(item.getOld_price()==0){
                tvOldPrice.setVisibility(View.GONE);
            }
            if (item.getIs_prize()==1){
                tvOldPrice.setText(" P."+ StringUtil.convertNumberToString(item.getOld_price(),0));
            }
            else {
                tvOldPrice.setText(" $"+ StringUtil.convertNumberToString(item.getOld_price(),2));
            }

            ImageUtil.setImage(mdealDetailActivity, img, item.getImage());
        }

        ArrayList<PropetiesObj> list = new ArrayList<>();
        list.add(new PropetiesObj("Color", item.getColors()));
        btnClose.setOnClickListener(this);
        btnGoCart.setOnClickListener(this);

        checkLayout();
        initColorProduct(v);
        initSizeProduct(v);

        btnGoCart.setBackgroundColor(getContext().getResources().getColor(R.color.grey));

        if (color == null && size == null && item.getSizes().size()==0 && item.getColors().size()== 0) {
            btnGoCart.setBackgroundColor(getContext().getResources().getColor(R.color.red));
        }

        return v;
    }

    private void checkLayout() {
        if (item.getSizes().size() > 0) {
            lnlSize.setVisibility(View.VISIBLE);
            lnl1.setVisibility(View.VISIBLE);
            lnl2.setVisibility(View.VISIBLE);
        } else {
            lnlSize.setVisibility(View.GONE);
            lnl1.setVisibility(View.GONE);
            lnl2.setVisibility(View.GONE);
        }
        if (item.getColors().size() > 0) {
            lnlColor.setVisibility(View.VISIBLE);
            lnl1.setVisibility(View.VISIBLE);
            lnl2.setVisibility(View.VISIBLE);
        } else {
            lnlColor.setVisibility(View.GONE);
            lnl1.setVisibility(View.GONE);
            lnl2.setVisibility(View.GONE);
        }
    }

    private void setOrder() {
        cartObjList = CartManager.getInstance().getArray();
        if (cartObjList.size() != 0) {
            boolean isExist = false;
            String id = item.getId();

            for (int i = 0; i < cartObjList.size(); i++) {
                cart = cartObjList.get(i);
                if (color != null && size != null) {
                    if (cart.getId().equals(id) && cart.getColor().equals(color) && cart.getSize().equals(size)) {
                        isExist = true;
                        break;
                    }
                } else if (color != null && size == null) {
                    if (cart.getId().equals(id) && cart.getColor().equals(color)) {
                        isExist = true;
                        break;
                    }
                } else if (color == null && size != null) {
                    if (cart.getId().equals(id) && cart.getSize().equals(size)) {
                        isExist = true;
                        break;
                    }
                } else {
                    if (cart.getId().equals(id)) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist) {
                int number = cart.getNumber();
                number += 1;
                cart.setNumber(number);

                double money = cart.getTotalMoney();
                money = number * cart.getPrice();
                cart.setTotalMoney(money);
            } else {
                CartManager.getInstance().addItem(new CartObj(item.getId(), item.getTitle(), item.getPrice(),item.getImage(),1, item.getPrice(),color, size,item.getOld_price(),item.getIs_prize()));

            }
        } else {
            CartManager.getInstance().addItem(new CartObj(item.getId(), item.getTitle(), item.getPrice(),item.getImage(),1, item.getPrice(),color, size,item.getOld_price(),item.getIs_prize()));


        }

        doIncrease();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void doIncrease() {
        count = DataStoreManager.getCountCart();
        count++;
        //getContext().invalidateOptionsMenu();
        DataStoreManager.saveCountCart(count);
    }

    private void initColorProduct(View view) {
        rcvColor = view.findViewById(R.id.rcvColor);
        colorAdapter = new ColorProductAdapter(getContext(), item.getColors(), new IOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                color = item.getColors().get(position).getColor();
                if (color != null && size != null) {
                    btnGoCart.setBackgroundColor(getContext().getResources().getColor(R.color.red));
                }else if(color != null && item.getColors().size() != 0 && item.getSizes().size() == 0){
                    btnGoCart.setBackgroundColor(getContext().getResources().getColor(R.color.red));
                }
            }
        });
        rcvColor.setAdapter(colorAdapter);
        rcvColor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        colorAdapter.addLiist(item.getColors());

    }

    private void initSizeProduct(View view) {
        rcvSize = view.findViewById(R.id.rcvSize);
        sizeAdapter = new SizeProductAdapter(getContext(), item.getSizes(), new IOnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                size = item.getSizes().get(position).getSize();
                if (color != null && size != null) {
                    btnGoCart.setBackgroundColor(getContext().getResources().getColor(R.color.red));
                }
            }
        });
        rcvSize.setAdapter(sizeAdapter);
        rcvSize.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        sizeAdapter.addLiist(item.getSizes());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnClose) {
            dismiss();
        } else if (id == R.id.btnGoCart) {
            if (DataStoreManager.getUser() != null) {
                if(item.getIs_prize()==1){
                    if (DataStoreManager.getUser() != null) {
                        if(DataStoreManager.getUser().getPoint()>=item.getPrice()){
                            if (color != null && size != null) {
                                setOrder();
                                GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                                dismiss();
                            } else if (item.getColors().size() == 0 && item.getSizes().size() == 0) {
                                setOrder();
                                GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                                dismiss();
                            }else if(color != null && size == null&& item.getColors().size() != 0 && item.getSizes().size() == 0){
                                setOrder();
                                GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                                dismiss();
                            }else if(color == null && size != null && item.getColors().size() == 0 && item.getSizes().size() != 0){
                                setOrder();
                                GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                                dismiss();
                            }
                        }else {
                            Toast.makeText(mdealDetailActivity, "Point not enough", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(mdealDetailActivity, R.string.you_are_not_login, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (color != null && size != null) {
                        setOrder();
                        GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                        dismiss();
                    } else if (item.getColors().size() == 0 && item.getSizes().size() == 0) {
                        setOrder();
                        GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                        dismiss();
                    }else if(color != null && size == null&& item.getColors().size() != 0 && item.getSizes().size() == 0){
                        setOrder();
                        GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                        dismiss();
                    }else if(color == null && size != null && item.getColors().size() == 0 && item.getSizes().size() != 0){
                        setOrder();
                        GlobalFunctions.startActivityWithoutAnimation(mdealDetailActivity, CartActivity.class);
                        dismiss();
                    }
                }
            }else {
                DialogUtil.showAlertDialog(mdealDetailActivity, R.string.notification, R.string.you_need_login, new DialogUtil.IDialogConfirm() {
                    @Override
                    public void onClickOk() {
                        AppUtil.startActivity(mdealDetailActivity, LoginActivity.class);
                        mdealDetailActivity.finish();
                    }
                });
            }

        }
    }

}
