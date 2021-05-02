package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.ColorProduct;
import com.wfour.onlinestoreapp.objects.ProductObj;

import java.util.ArrayList;

public class PropetiesListAdapter extends RecyclerView.Adapter<PropetiesListAdapter.MyViewHolder> {

    private ArrayList<ColorProduct> propeties;
    private Activity activity;
    private ArrayList<ProductObj> productList;
    private ProductObj productObj;

    public PropetiesListAdapter(Activity activity, ArrayList<ColorProduct> propeties){
        this.activity = activity;
        this.propeties = propeties;
    }

    public void addList(ArrayList<ColorProduct> propeties){
        this.propeties = propeties;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_propeties_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ColorProduct colorProduct = propeties.get(position);
//        holder.rcvData.setAdapter(new ColorProductAdapter(activity, productObj.getColors(), new MyOnClickPropety() {
//
//            @Override
//            public void onClickSize(SizeProduct sizeProduct) {
//
//            }
//
//            @Override
//            public void onClickColor(ColorProduct colorProduct) {
//
//            }
//        }));
        holder.rcvData.setLayoutManager(new GridLayoutManager(activity, 4));
    }

    @Override
    public int getItemCount() {
        return (propeties == null) ? 0: propeties.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private RecyclerView rcvData;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            rcvData = itemView.findViewById(R.id.rcvData);
        }
    }
}
