package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IOnItemClickListener;
import com.wfour.onlinestoreapp.objects.SizeProduct;

import java.util.ArrayList;

public class SizeProductAdapter extends RecyclerView.Adapter<SizeProductAdapter.MyViewHolder>{

    private ArrayList<SizeProduct> sizeList;
    private Context context;
    private IOnItemClickListener onClickPropety;
    private int selectedPosition = -1;

    public SizeProductAdapter(Context context, ArrayList<SizeProduct> sizeList, IOnItemClickListener onClickPropety){
        this.context = context;
        this.sizeList = sizeList;
        this.onClickPropety= onClickPropety;
    }

    public void addLiist(ArrayList<SizeProduct> sizeList){
        this.sizeList = sizeList;
        this.notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SizeProduct sizeProduct = sizeList.get(position);
        holder.tvName.setText(sizeProduct.getSize());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickPropety != null){
                   onClickPropety.onItemClick(position);
                   selectedPosition = position;
                   notifyDataSetChanged();
                }
            }
        });
        if(selectedPosition == position){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.tvName.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return (sizeList == null) ? 0 : sizeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private CardView cardView;
        private LinearLayout lnl;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
