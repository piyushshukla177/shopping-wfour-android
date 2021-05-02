package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IChat;
import com.wfour.onlinestoreapp.objects.RecentChatObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Suusoft on 14/12/2016.
 */

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<RecentChatObj> listContacts;
    private IChat iChat;

    public RecentChatAdapter(Activity context, ArrayList<RecentChatObj> listContacts, IChat iChat) {
        this.context = context;
        this.listContacts = listContacts;
        this.iChat = iChat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecentChatObj obj = listContacts.get(position);

        ImageUtil.setImage(context, holder.imgContact, ""/*avatar*/, 300, 300);
        holder.tvName.setText(obj.getQbUser().getFullName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iChat.onUserClicked(obj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgContact;
        private TextViewBold tvName;
        private TextViewRegular tvLastMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            imgContact = (CircleImageView) itemView.findViewById(R.id.img_contact);
            tvName = (TextViewBold) itemView.findViewById(R.id.lbl_deal_name);
            tvLastMsg = (TextViewRegular) itemView.findViewById(R.id.tv_description);
        }
    }
}
