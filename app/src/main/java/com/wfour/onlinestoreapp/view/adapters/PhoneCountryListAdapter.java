package com.wfour.onlinestoreapp.view.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.CountryPhoneObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suusoft on 12/26/2016.
 */

public class PhoneCountryListAdapter extends RecyclerView.Adapter<PhoneCountryListAdapter.ViewHolder> implements Filterable {

    List<CountryPhoneObj> mDatas, mFilterValues;
    IOnItemClickListener listener;

    public interface IOnItemClickListener{
        void onItemSelected(String phoneCode);
    }

    public PhoneCountryListAdapter(List<CountryPhoneObj> data, IOnItemClickListener listener) {
        this.mDatas = data;
        this.listener = listener;
        this.mFilterValues =  data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bind(mFilterValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(mFilterValues.get(position).getPhoneCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.lbl_deal_name);
            tvCode = (TextView) itemView.findViewById(R.id.tv_code);
        }

        public void bind(CountryPhoneObj item){
            if (item != null){
                tvName.setText(item.getName());
                tvCode.setText(item.getPhoneCode());
            }
        }
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mFilterValues = (ArrayList<CountryPhoneObj>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CountryPhoneObj> FilteredArrList = new ArrayList<>();

                if (mDatas == null) {
                    mDatas = new ArrayList<>(mFilterValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mDatas.size();
                    results.values = mDatas;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mDatas.size(); i++) {
                        String data = mDatas.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new CountryPhoneObj(mDatas.get(i).getName(),mDatas.get(i).getPhoneCode()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;

    }




}
