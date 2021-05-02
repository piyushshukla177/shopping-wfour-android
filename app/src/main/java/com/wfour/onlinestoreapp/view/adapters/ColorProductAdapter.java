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
import com.wfour.onlinestoreapp.objects.ColorProduct;

import java.util.ArrayList;

public class ColorProductAdapter extends RecyclerView.Adapter<ColorProductAdapter.MyViewHolder> {
    private ArrayList<ColorProduct> colorList;
    private Context context;
    private IOnItemClickListener onClickPropety;
    private int selectedPosition = -1;
    private boolean isCheck = false;

    public ColorProductAdapter(Context context, ArrayList<ColorProduct> colorList, IOnItemClickListener onClickPropety){
        this.context = context;
        this.colorList = colorList;
        this.onClickPropety = onClickPropety;
    }

    public void addLiist(ArrayList<ColorProduct> colorList){
        this.colorList = colorList;
        this.notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ColorProduct colorProduct = colorList.get(position);
        holder.tvName.setText(colorProduct.getColor());
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
        return (colorList == null) ? 0 : colorList.size();
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
