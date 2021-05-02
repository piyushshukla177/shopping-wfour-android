package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.MyOnClick;
import com.wfour.onlinestoreapp.objects.Person;
import com.wfour.onlinestoreapp.view.activities.CartActivity;
import com.wfour.onlinestoreapp.view.fragments.UpdateAddressFragment;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private ArrayList<Person> personList;
    private static MyOnClick onClick;
    private Context context;
    private CartActivity mCartActivity;
    private Intent intent;
    private Bundle bundle;
    private UpdateAddressFragment updateAddressFragment;
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String ADDRESS = "ADDRESS";
    private int mSelectedITEM= -1;


    public  void setMyOnClick(MyOnClick onClick){
        this.onClick = onClick;
    }



    public AddressAdapter(CartActivity activity, ArrayList<Person> personList, MyOnClick onClick){
        this.personList = personList;
        this.onClick = onClick;
        this.mCartActivity = activity;
    }

    public void addList( ArrayList<Person> personList){
        this.personList = personList;
        this.notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_address, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Person person = personList.get(position);
        holder.tvName.setText(person.getName());
        holder.tvPhone.setText(String.valueOf(person.getPhone()));
        holder.tvAllAddress.setText(person.getAddress());
//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClickSize(View view) {
//                if(onClickSize != null){
//                    onClickSize.DeleteItem(position);
//                }
//            }
//        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClick != null){
                    onClick.EditItem(person, position);
                }


            }
        });

        //holder.radioButton.setChecked(position == mSelectedITEM);

    }

    @Override
    public int getItemCount() {
        return (personList ==null)? 0: personList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPhone, tvAllAddress, btnDelete, btnEdit;
        private RadioButton radioButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAllAddress = itemView.findViewById(R.id.tvAllAddress);
//            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
           // radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
