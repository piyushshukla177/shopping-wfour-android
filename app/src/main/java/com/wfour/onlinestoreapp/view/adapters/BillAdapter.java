package com.wfour.onlinestoreapp.view.adapters;

import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.MyOnClickOrderHistory;
import com.wfour.onlinestoreapp.objects.OrderObj;
import com.wfour.onlinestoreapp.view.activities.MainActivity;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    private ArrayList<OrderObj> orderObjList;
    private MainActivity activity;
    private MyOnClickOrderHistory onItemClick;

    public BillAdapter(MainActivity activity, ArrayList<OrderObj> orderObjList, MyOnClickOrderHistory onItemClick) {
        this.activity = activity;
        this.orderObjList = orderObjList;
        this.onItemClick = onItemClick;
    }

    public void addList(ArrayList<OrderObj> orderObjList) {
        this.orderObjList = orderObjList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_bill_detail, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderObj orderObj = orderObjList.get(position);

        holder.tvIdBill.setText(String.valueOf(orderObj.getId()));
        holder.tvDateTime.setText(orderObj.getCreateDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onClick(orderObj, position);
                }
            }
        });

        if (orderObj.getStatus().equals("0")) {
            holder.tvStatus.setText(R.string.moving);
        } else if (orderObj.getStatus().equals("1")) {
            holder.tvStatus.setText(R.string.appoved);
        } else if (orderObj.getStatus().equals("2")) {
            holder.tvStatus.setText(AppController.getInstance().getString(R.string.canceled));
        } else if (orderObj.getStatus().equals("3")) {
            holder.tvStatus.setText(R.string.haruka_ona);
            if (orderObj.getPaymentMethod().equals("point")) {
                holder.tvStatus.setText(R.string.troka_ona);
            }
        } else if (orderObj.getStatus().equals("4")) {
            holder.tvStatus.setText(AppController.getInstance().getString(R.string.not_paid));
        } else if (orderObj.getStatus().equals("5")) {
            holder.tvStatus.setText(R.string.delivery);
        }
        else {
            holder.tvStatus.setText(AppController.getInstance().getString(R.string.rejected));
            if (orderObj.getPaymentMethod().equals("point")) {
                holder.tvStatus.setText(R.string.troka_ona);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (orderObjList == null) ? 0 : orderObjList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvIdBill, tvDateTime, tvStatus;
        private ImageView imgClose, imgSyn;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvIdBill = itemView.findViewById(R.id.tvIdBill);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cardView = itemView.findViewById(R.id.cardView);
            imgClose = itemView.findViewById(R.id.imgClose);
            imgSyn = itemView.findViewById(R.id.imgSyn);
        }
    }
}