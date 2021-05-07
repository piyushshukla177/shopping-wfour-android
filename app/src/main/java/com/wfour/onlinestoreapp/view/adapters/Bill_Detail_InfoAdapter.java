package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.OrderDetailObj;

import java.util.ArrayList;

public class Bill_Detail_InfoAdapter extends RecyclerView.Adapter<Bill_Detail_InfoAdapter.MyViewHolder> {

    private ArrayList<OrderDetailObj> orderDetailObjs;
    private Context context;

    public Bill_Detail_InfoAdapter(Context context, ArrayList<OrderDetailObj> orderDetailObjs) {
        this.context = context;
        this.orderDetailObjs = orderDetailObjs;
    }

    public void addList(ArrayList<OrderDetailObj> orderDetailObjs) {
        this.orderDetailObjs = orderDetailObjs;
        this.notifyDataSetChanged();
    }

    @Override
    public Bill_Detail_InfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_detail_info_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderDetailObj orderDetailObj = orderDetailObjs.get(position);
       holder.product_name_tv.setText(orderDetailObj.getProductName()+"");
      holder.qty_tv.setText(orderDetailObj.getQuantity()+"");
       holder.price_tv.setText(String.valueOf(orderDetailObj.getPrice()+""));
       holder.sub_total_tv.setText(String.valueOf(orderDetailObj.getSubTotal()+""));
    }

    @Override
    public int getItemCount() {
        return (orderDetailObjs == null) ? 0 : orderDetailObjs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView product_name_tv, qty_tv, price_tv, sub_total_tv;


        public MyViewHolder(View itemView) {
            super(itemView);
            product_name_tv = itemView.findViewById(R.id.product_name_tv);
            qty_tv = itemView.findViewById(R.id.qty_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
            sub_total_tv = itemView.findViewById(R.id.sub_total_tv);
        }
    }
}