package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.MyOnClickDelivery;
import com.wfour.onlinestoreapp.objects.DeliveryObj;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {

    private ArrayList<DeliveryObj> deliveryList;
    private MyOnClickDelivery onClick;
    private Context context;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private int selectedPosition = 1;

    public DeliveryAdapter(Context context, ArrayList<DeliveryObj> deliveryList, MyOnClickDelivery onClick) {
        this.context = context;
        this.deliveryList = deliveryList;
        this.onClick = onClick;
    }

    public void addList(ArrayList<DeliveryObj> deliveryList) {
        this.deliveryList = deliveryList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_method, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DeliveryObj deliveryObj = deliveryList.get(position);
        holder.tvName.setText(deliveryObj.getName());
        holder.tvDescription.setText(deliveryObj.getDescription());
        holder.tvPrice.setText(String.valueOf("$"+deliveryObj.getPrice()));
        holder.tvPrice.setVisibility(View.GONE);
        if (deliveryList.get(position).getPrice() >= 1) {
            holder.tvTranspot.setText("Wfour Delivery");
        }else {
            holder.tvTranspot.setText("Foti iha Loja");
        }


        holder.radioButton.setChecked(deliveryObj.isSelected());
        holder.radioButton.setTag(new Integer(position));

        if (position == 1 && deliveryList.get(1).isSelected() && holder.radioButton.isChecked()) {
            lastChecked = holder.radioButton;
            lastCheckedPos = 1;
        }else if (position == 2 && deliveryList.get(2).isSelected() && holder.radioButton.isChecked()) {
            lastChecked = holder.radioButton;
            lastCheckedPos = 2;
        }else if (position == 3 && deliveryList.get(3).isSelected() && holder.radioButton.isChecked()) {
            lastChecked = holder.radioButton;
            lastCheckedPos = 3;
        }else if (position == 4 && deliveryList.get(4).isSelected() && holder.radioButton.isChecked()) {
            lastChecked = holder.radioButton;
            lastCheckedPos = 4;
        }else if (position == 0 && deliveryList.get(0).isSelected() && holder.radioButton.isChecked()) {
            lastChecked = holder.radioButton;
            lastCheckedPos = 0;
        }


        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton cb = (RadioButton) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();

                if (cb.isChecked()) {

                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        deliveryList.get(lastCheckedPos).setSelected(false);
                    }
                        lastChecked = cb;
                        lastCheckedPos = clickedPos;

                }
                else
                    lastChecked = null;

                deliveryList.get(clickedPos).setSelected(cb.isChecked());

            }
        });

        //comment
//        holder.radioButton.setOnCheckedChangeListener(null);
//
//        holder.radioButton.setChecked(selectedPosition == position);
//        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(onClick !=null) {
//                    onClick.onClick(position);
//                    selectedPosition = position;
//                    notifyDataSetChanged();
//                }
//            }
//        });
//
//        if(selectedPosition == position){
//            holder.radioButton.setChecked(true);
//        }
//        else{
//            holder.radioButton.setChecked(false);
//        }


    }

    @Override
    public int getItemCount() {
        return (deliveryList == null) ? 0 : deliveryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice, tvDescription, tvTranspot;
        private RadioButton radioButton;
        private LinearLayout lnl_container;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            radioButton = itemView.findViewById(R.id.radioButton);
            lnl_container = itemView.findViewById(R.id.lnl_container);
            tvTranspot    = itemView.findViewById(R.id.tranpost);

        }
    }
}
