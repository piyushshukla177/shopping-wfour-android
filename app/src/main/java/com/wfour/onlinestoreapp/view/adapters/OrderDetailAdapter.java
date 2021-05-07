package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.OrderDetailObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    private ArrayList<OrderDetailObj> orderDetailObjs;
    private Context context;

    public OrderDetailAdapter(Context context, ArrayList<OrderDetailObj> orderDetailObjs) {
        this.context = context;
        this.orderDetailObjs = orderDetailObjs;
    }

    public void addList(ArrayList<OrderDetailObj> orderDetailObjs) {
        this.orderDetailObjs = orderDetailObjs;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_order, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderDetailObj orderDetailObj = orderDetailObjs.get(position);
        holder.tvName.setText(orderDetailObj.getProductName());
        if (orderDetailObj.isIs_prize()) {
            holder.tvPrice.setText(" P." + StringUtil.convertNumberToString(orderDetailObj.getPrice(), 2));
        } else {
            holder.tvPrice.setText(" $" + StringUtil.convertNumberToString(orderDetailObj.getPrice(), 2));
        }

        if (orderDetailObj.getOld_price() == 0) {
            holder.tvOldPrice.setVisibility(View.GONE);
        }
        if (orderDetailObj.isIs_prize()) {
            holder.tvOldPrice.setText(" $" + StringUtil.convertNumberToString(orderDetailObj.getOld_price(), 2));
        } else {
            holder.tvOldPrice.setText(" $" + StringUtil.convertNumberToString(orderDetailObj.getOld_price(), 2));
        }
        holder.tvNumber.setText(String.valueOf(orderDetailObj.getQuantity()));
        holder.tvStatus.setText("Prosesu");
        ImageUtil.setImage(context, holder.img, orderDetailObj.getImage());
        if (orderDetailObj.getSize() == null) {
            holder.lnlSize.setVisibility(View.GONE);
        } else {
            holder.lnlSize.setVisibility(View.VISIBLE);
        }
        if (orderDetailObj.getColor() == null) {
            holder.lnlColor.setVisibility(View.GONE);
        } else {
            holder.lnlColor.setVisibility(View.VISIBLE);
        }
        holder.tvSize.setText(orderDetailObj.getSize());
        holder.tvColor.setText(orderDetailObj.getColor());
    }

    @Override
    public int getItemCount() {
        return (orderDetailObjs == null) ? 0 : orderDetailObjs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice, btnDelete, tvNumber, tvColor, tvSize, tvOldPrice, tvStatus;
        private ImageView img;
        private LinearLayout lnlColor, lnlSize;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            img = itemView.findViewById(R.id.img);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            lnlColor = itemView.findViewById(R.id.lnlColor);
            lnlSize = itemView.findViewById(R.id.lnlSize);
            tvOldPrice = itemView.findViewById(R.id.lbl_price_old);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}
