package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.objects.NotificationObject;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private ArrayList<NotificationObject> notification_list;
    private Context context;

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView no_of_msg_tv, msg_from_tv, msg_tv;
        public RelativeLayout relatiive;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            no_of_msg_tv = itemView.findViewById(R.id.no_of_msg_tv);
            msg_from_tv = itemView.findViewById(R.id.msg_from_tv);
            msg_tv = itemView.findViewById(R.id.msg_tv);
            relatiive = itemView.findViewById(R.id.relatiive);
        }
    }

    public NotificationAdapter(Context context, ArrayList<NotificationObject> list) {
        notification_list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        NotificationViewHolder evh = new NotificationViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationViewHolder holder, int i) {
        final NotificationObject currentItem = notification_list.get(i);

        holder.msg_from_tv.setText(currentItem.getNotification_from());
        holder.msg_tv.setText(currentItem.getNotification_messege());
        if (currentItem.getNo_of_msg().equals(String.valueOf(0))) {
            holder.relatiive.setVisibility(View.GONE);
        } else {
            holder.relatiive.setVisibility(View.VISIBLE);
        }
        holder.no_of_msg_tv.setText(currentItem.getNo_of_msg());
    }

    @Override
    public int getItemCount() {
        return notification_list.size();
    }
}
