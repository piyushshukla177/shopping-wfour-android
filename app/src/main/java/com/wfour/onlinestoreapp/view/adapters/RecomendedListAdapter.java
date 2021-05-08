package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IMyOnClick;
import com.wfour.onlinestoreapp.objects.ProductObj;
import com.wfour.onlinestoreapp.objects.RecomendedObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;

import java.util.ArrayList;

public class RecomendedListAdapter extends RecyclerView.Adapter<RecomendedListAdapter.RecomendedListViewHolder> {

    ArrayList<RecomendedObj> productObjList;
    private Activity context;
    private int w;

    public void initParam(Activity activity) {
        w = AppUtil.getScreenWidth(activity);

    }

    public RecomendedListAdapter(Activity context, ArrayList<RecomendedObj> productObjList) {
        this.context = context;
        this.productObjList = productObjList;
        initParam(context);
    }

    @Override
    public RecomendedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recomended_list_layout, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        params.width = (int) (w * 0.45);
        cardView.setLayoutParams(params);
        return new RecomendedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecomendedListViewHolder holder, final int position) {
        final RecomendedObj productObj = productObjList.get(position);
        holder.imgAvatar.setImageResource(R.drawable.demo_product);
        holder.tvTitle.setText(productObj.getProduct_Name());
        holder.tvDescription.setText(productObj.getDescription());
        holder.tvPrice.setText("$ " + productObj.getActual_rate());
        if(productObj.getDiscount_rate().equals("0.00")){
            holder.tvOldPrice.setVisibility(View.GONE);
        }
        holder.tvOldPrice.setText(productObj.getDiscount_rate());
    }

    @Override
    public int getItemCount() {
        return (productObjList.size() >= 10) ? 10 : productObjList.size();
    }

    public class RecomendedListViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription, tvPrice, tvOldPrice;
        private CardView cardView;
        private ImageView imgAvatar;

        public RecomendedListViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cardView = itemView.findViewById(R.id.cardView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvPrice = itemView.findViewById(R.id.lbl_price);
            tvOldPrice = itemView.findViewById(R.id.lbl_price_old);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
