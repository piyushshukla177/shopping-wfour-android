package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.graphics.Paint;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.interfaces.MyInterface;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.view.activities.CartActivity;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    //private List<ProductObj> cartObjList;
    private List<CartObj> cartObjList;
    private Activity context;
    private MyInterface mMyInterface;
    private CartActivity cartActivity;
    private int h;


    public CartAdapter(Activity context, List<CartObj> cartObjList, MyInterface mMyInterface) {
        this.cartObjList = cartObjList;
        this.context = context;
        this.mMyInterface = mMyInterface;
        initParam(context);
    }

    public void addList(ArrayList<CartObj> cartObjList) {
        this.cartObjList = cartObjList;
        this.notifyDataSetChanged();
    }
    private void initParam(Activity context) {
        h = AppUtil.getScreenHeight(context);
    }

    public void setOnClickListener(MyInterface mMyInterface) {
        this.mMyInterface = mMyInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_row, parent, false);
        CardView cardView = itemView.findViewById(R.id.cardView);
        //ViewGroup.LayoutParams params = cardView.getLayoutParams();
        //params.height= (int) (h * 0.25);
        //cardView.setLayoutParams(params);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CartObj cartObj = cartObjList.get(position);
        holder.tvName.setText(cartObj.getName());
        if(cartObj.getIs_point()==1){
            holder.tvPrice.setText( " P."+StringUtil.convertNumberToString(cartObj.getPrice(),0) );
        }
        else {
            holder.tvPrice.setText( " $"+StringUtil.convertNumberToString(cartObj.getPrice(),2) );
        }

        if(cartObj.getOldPrice()==0){
            holder.tvOldPrice.setVisibility(View.GONE);
        }
        if(cartObj.getIs_point()==1){
            holder.tvOldPrice.setText(" P."+ StringUtil.convertNumberToString(cartObj.getOldPrice(),0));
        }
        else {
            holder.tvOldPrice.setText(" $"+ StringUtil.convertNumberToString(cartObj.getOldPrice(),2));
        }


        if(cartObj.getSize() == null){
            holder.lnlSize.setVisibility(View.GONE);
        }else {
            holder.lnlSize.setVisibility(View.VISIBLE);
        }
        if(cartObj.getColor() == null){
            holder.lnlColor.setVisibility(View.GONE);
        }else {
            holder.lnlColor.setVisibility(View.VISIBLE);
        }
        holder.tvSize.setText(cartObj.getSize());
        holder.tvColor.setText(cartObj.getColor());
        ImageUtil.setImage(context, holder.img, cartObj.getImage());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyInterface != null) {
                    mMyInterface.deleteItem(position);
                }
            }
        });

//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Args.KEY_PRODUCT_OBJECT, cartObj);
//                bundle.putString("cart", "cart");
//                GlobalFunctions.startActivityWithoutAnimation(context, DealDetailActivity.class, bundle);
//            }
//        });

        holder.tvNum.setText(cartObj.getNumber() + "");
        holder.btnInCrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartObj cart = cartObjList.get(position);
                int number = cart.getNumber();
                number += 1;
                cart.setNumber(number);
                holder.tvNum.setText(number + "");
                DataStoreManager.saveCountCart(number);
                double money = cart.getPrice() * number;
                if (mMyInterface != null) {
                    mMyInterface.onInCrease(money, position);
                }
            }
        });
        holder.btnDeCrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartObj cart = cartObjList.get(position);
                int number = cart.getNumber();
                if (number <= 1) return;
                number -= 1;
                cart.setNumber(number);
                holder.tvNum.setText(number + "");
                DataStoreManager.saveCountCart(number);
                double money = cart.getPrice() * number;
                if (mMyInterface != null) {
                    mMyInterface.onDeCrease(money, position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (cartObjList == null) ? 0 : cartObjList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice, tvNum, btnDeCrease, btnInCrease, tvSize, tvColor,tvOldPrice;
        private ImageView img, btnDelete;
        private LinearLayout lnlColor, lnlSize;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.lbl_price_old);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            img = itemView.findViewById(R.id.img);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvNum = itemView.findViewById(R.id.tvNum);
            btnInCrease = itemView.findViewById(R.id.btnInCrease);
            btnDeCrease = itemView.findViewById(R.id.btnDeCrease);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            lnlColor = itemView.findViewById(R.id.lnlColor);
            lnlSize = itemView.findViewById(R.id.lnlSize);
        }
    }
}
