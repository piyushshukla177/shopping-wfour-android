package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.objects.DealObj;
import com.wfour.onlinestoreapp.utils.AppUtil;

import java.util.ArrayList;

public class NewDealAdapter extends RecyclerView.Adapter<NewDealAdapter.MyViewHolder> {

    ArrayList<DealObj> dealObjList;
    private Activity context;
    private IMyOnClick onClick;
    private int w;

    public void initParam(Activity activity){
        w = AppUtil.getScreenWidth(activity);
    }

    public NewDealAdapter(Activity context, ArrayList<DealObj> dealObjList, IMyOnClick onClick ){
        this.context = context;
        this.dealObjList = dealObjList;
        this.onClick = onClick;
        //initParam(context);
    }

    public void addList(ArrayList<DealObj> dealObjList) {
        this.dealObjList = dealObjList;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_deal, parent, false);
        MyViewHolder vh = new MyViewHolder(view);

//        CardView cardView = view.findViewById(R.id.cardView);
//        ViewGroup.LayoutParams params = cardView.getLayoutParams();
//        params.width= (int) (w * 0.4);
//        cardView.setLayoutParams(params);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DealObj dealObj = dealObjList.get(position);
        holder.tvTitle.setText(dealObj.getName());
        holder.tvPrice.setText(String.valueOf(dealObj.getPrice())+" $");
        Glide.with(context).load(dealObj.getImage()).into(holder.imgAvatar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClick != null){
                   // onClickSize.MyOnClick(position, dealObj);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dealObjList.size() > 10) ? 10 : dealObjList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

       private TextView tvTitle, tvDescription, tvPrice;
       private CardView cardView;
       private ImageView imgAvatar;

       public MyViewHolder(View itemView) {
           super(itemView);
           tvDescription = itemView.findViewById(R.id.tvDescription);
           tvTitle = itemView.findViewById(R.id.tvTitle);
           cardView = itemView.findViewById(R.id.cardView);
           imgAvatar = itemView.findViewById(R.id.img_avatar);
           tvPrice = itemView.findViewById(R.id.lbl_price);
       }
   }
}
