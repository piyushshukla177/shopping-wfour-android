package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.DealReviewObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.utils.ViewUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Suusoft on 11/24/2016.
 */

public class DealReviewAdapter extends RecyclerView.Adapter<DealReviewAdapter.ViewHolder> {

    private List<DealReviewObj> mDatas;
    private Context context;

    public DealReviewAdapter(Context context, List<DealReviewObj> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal_review_reskin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DealReviewObj item = mDatas.get(position);
        if (item != null) {
            holder.bind(item);
//            if (position % 2 == 0) {
//                holder.itemView.setBackgroundResource(R.color.bg_deal_review);
//            } else {
//                holder.itemView.setBackgroundResource(R.color.white);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvContent;
        RatingBar ratingBar;
        CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.lbl_deal_name);
            tvName.setSelected(true);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            imageView = (CircleImageView) itemView.findViewById(R.id.img_user);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);

            ViewUtil.setRatingbarColor(ratingBar);
        }

        public void bind(DealReviewObj item) {
            tvName.setText(item.getUsername());
            tvContent.setText(item.getContent());
            ratingBar.setRating(item.getRating());
            ImageUtil.setImage(context, imageView, item.getUserImage());
        }

    }
}
