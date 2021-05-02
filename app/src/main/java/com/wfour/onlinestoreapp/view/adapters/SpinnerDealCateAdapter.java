package com.wfour.onlinestoreapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wfour.onlinestoreapp.objects.DealCateObj;

import java.util.ArrayList;

/**
 * Created by Suusoft on 12/12/2016.
 */

public class SpinnerDealCateAdapter extends BaseAdapter {
    private ArrayList<DealCateObj> listDealCateObjs;

    public SpinnerDealCateAdapter(ArrayList<DealCateObj> listDealCateObjs) {
        this.listDealCateObjs = listDealCateObjs;
    }

    @Override
    public int getCount() {
        return listDealCateObjs.size();
    }

    @Override
    public Object getItem(int i) {
        return listDealCateObjs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_spinner_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvNameDealCate.setText(listDealCateObjs.get(i).getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView== null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,parent, false);
            viewHolder = new ViewHolder(convertView);
           convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNameDealCate.setText(listDealCateObjs.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        public TextView tvNameDealCate;

        public ViewHolder(View view) {
            tvNameDealCate = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
