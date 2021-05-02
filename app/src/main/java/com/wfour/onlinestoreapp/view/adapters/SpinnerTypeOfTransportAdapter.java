package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wfour.onlinestoreapp.objects.TransportObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 12/12/2016.
 */

public class SpinnerTypeOfTransportAdapter extends ArrayAdapter {
    private ArrayList<TransportObj> listTransports;

    public SpinnerTypeOfTransportAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        listTransports = (ArrayList<TransportObj>) objects;

    }


    @Override
    public int getCount() {
        return listTransports.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return listTransports.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNameTransport.setText(listTransports.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNameTransport.setText(listTransports.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        private TextView tvNameTransport;

        public ViewHolder(View view) {
            tvNameTransport = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
