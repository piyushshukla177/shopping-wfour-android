package com.wfour.onlinestoreapp.view.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.IChat;
import com.wfour.onlinestoreapp.objects.ContactObj;
import com.wfour.onlinestoreapp.utils.ImageUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Suusoft on 14/12/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<ContactObj> listContacts;
    private IChat iChat;

    public ContactAdapter(Activity context, ArrayList<ContactObj> listContacts, IChat iChat) {
        this.context = context;
        this.listContacts = listContacts;
        this.iChat = iChat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ContactObj contactObj = listContacts.get(position);

        ImageUtil.setImage(context, holder.imgContact, contactObj.getAvatar(), 300, 300);
        holder.tvName.setText(contactObj.getQbUser().getFullName());

        holder.imgAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iChat.onActionClicked(view, contactObj);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iChat.onUserClicked(contactObj);
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
        private ImageView imgAction;

        public ViewHolder(View itemView) {
            super(itemView);
            imgContact = (CircleImageView) itemView.findViewById(R.id.img_contact);
            tvName = (TextViewBold) itemView.findViewById(R.id.lbl_deal_name);
            tvLastMsg = (TextViewRegular) itemView.findViewById(R.id.tv_description);
            imgAction = (ImageView) itemView.findViewById(R.id.img_action);
        }
    }
}
