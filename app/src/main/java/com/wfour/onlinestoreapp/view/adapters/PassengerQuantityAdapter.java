package com.wfour.onlinestoreapp.view.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IPassenger;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

/**
 * Created by Suusoft on 10/15/2015.
 */
public class PassengerQuantityAdapter extends RecyclerView.Adapter<PassengerQuantityAdapter.ViewHolder> {

    private static final int SIZE = 9;

    private IPassenger iPassenger;

    public PassengerQuantityAdapter(IPassenger iPassenger) {
        this.iPassenger = iPassenger;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_quantity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final String quantity = String.valueOf(position + 1);
            holder.lblPassengerQuantity.setText(quantity);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPassenger.onQuantitySelected(quantity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        try {
            return SIZE;
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblPassengerQuantity;

        public ViewHolder(View view) {
            super(view);

            lblPassengerQuantity = (TextViewRegular) view.findViewById(R.id.lbl_quantity);
        }
    }
}
