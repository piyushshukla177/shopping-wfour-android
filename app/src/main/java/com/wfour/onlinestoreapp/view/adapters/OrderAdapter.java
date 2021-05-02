package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.CartObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
//    private List<ProductObj> cartObjList;
private List<CartObj> cartObjList;
    private Activity context;


    public  OrderAdapter (Activity context,List<CartObj> cartObjList){
        this.cartObjList = cartObjList;
        this.context = context;
    }
    public void addList( ArrayList<CartObj> cartObjList){
        this.cartObjList = cartObjList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_order, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CartObj cartObj = cartObjList.get(position);
        holder.tvName.setText(cartObj.getName());
        if(cartObj.getIs_point()==1){
            holder.tvPrice.setText(" P."+ StringUtil.convertNumberToString(cartObj.getPrice(),2));
        }
        else {
            holder.tvPrice.setText(" $"+ StringUtil.convertNumberToString(cartObj.getPrice(),2));
        }

        if(cartObj.getOldPrice()==0){
            holder.tvOldPrice.setVisibility(View.GONE);
        }
        if(cartObj.getIs_point()==1){
            holder.tvOldPrice.setText(" P."+ StringUtil.convertNumberToString(cartObj.getOldPrice(),2));
        }
        else {
            holder.tvOldPrice.setText(" $"+ StringUtil.convertNumberToString(cartObj.getOldPrice(),2));
        }

        ImageUtil.setImage(context, holder.img, cartObj.getImage());
        holder.tvNumber.setText(String.valueOf(cartObj.getNumber()));
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
    }

    @Override
    public int getItemCount() {
        return (cartObjList == null)? 0 :cartObjList.size();
    }

    public  static  class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvPrice, btnDelete, tvNumber, tvSize, tvColor,tvOldPrice;
        private ImageView img;
        private LinearLayout lnlColor, lnlSize;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.lbl_price_old);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            img = itemView.findViewById(R.id.img);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            lnlColor = itemView.findViewById(R.id.lnlColor);
            lnlSize = itemView.findViewById(R.id.lnlSize);
        }
    }
}
