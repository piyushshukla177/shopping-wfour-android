package com.wfour.onlinestoreapp.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.helper.CollectionsUtil;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.quickblox.PaginationHistoryListener;
import com.wfour.onlinestoreapp.quickblox.QbUsersHolder;
import com.wfour.onlinestoreapp.quickblox.chat.ChatHelper;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.ResourceUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.Collection;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ChatAdapter extends BaseListAdapter<QBChatMessage> implements StickyListHeadersAdapter {

    private static final String TAG = ChatAdapter.class.getSimpleName();

    private final QBChatDialog chatDialog;
    private OnItemInfoExpandedListener onItemInfoExpandedListener;
    private PaginationHistoryListener paginationListener;
    private int previousGetCount = 0;
    private Activity activity;


    public ChatAdapter(Activity context, QBChatDialog chatDialog, List<QBChatMessage> chatMessages) {
        super(context, chatMessages);
        activity = context;
        this.chatDialog = chatDialog;
    }

    public void setOnItemInfoExpandedListener(OnItemInfoExpandedListener onItemInfoExpandedListener) {
        this.onItemInfoExpandedListener = onItemInfoExpandedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_chat_message, parent, false);

            holder.messageBodyTextView = (TextView) convertView.findViewById(R.id.text_image_message);
            holder.messageBodyTextView.setMaxWidth(AppUtil.getScreenWidth(activity) - AppUtil.getScreenWidth(activity)/4);
            holder.messageAuthorTextView = (TextView) convertView.findViewById(R.id.text_message_author);
            holder.messageContainerLayout = (LinearLayout) convertView.findViewById(R.id.layout_chat_message_container);
            holder.messageBodyContainerLayout = (RelativeLayout) convertView.findViewById(R.id.layout_message_content_container);
            holder.messageInfoTextView = (TextView) convertView.findViewById(R.id.text_message_info);
//            holder.attachmentImageView = (MaskedImageView) convertView.findViewById(R.id.image_message_attachment);
//            holder.attachmentProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_message_attachment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final QBChatMessage chatMessage = getItem(position);

        setIncomingOrOutgoingMessageAttributes(holder, chatMessage);
        setMessageBody(holder, chatMessage);
        setMessageInfo(chatMessage, holder);
        setMessageAuthor(holder, chatMessage);

        holder.messageContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleItemInfo(holder, position);
            }
        });
        holder.messageContainerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (hasAttachments(chatMessage)) {
                    toggleItemInfo(holder, position);
                    return true;
                }

                return false;
            }
        });
        holder.messageInfoTextView.setVisibility(View.GONE);

        if (isIncoming(chatMessage) && !isRead(chatMessage)) {
            readMessage(chatMessage);
        }

        downloadMore(position);

        return convertView;
    }

    private void downloadMore(int position) {
        if (position == 0) {
            if (getCount() != previousGetCount) {
                paginationListener.downloadMore();
                previousGetCount = getCount();
            }
        }
    }

    public void setPaginationHistoryListener(PaginationHistoryListener paginationListener) {
        this.paginationListener = paginationListener;
    }

    private void toggleItemInfo(ViewHolder holder, int position) {
        boolean isMessageInfoVisible = holder.messageInfoTextView.getVisibility() == View.VISIBLE;
        holder.messageInfoTextView.setVisibility(isMessageInfoVisible ? View.GONE : View.VISIBLE);

        if (onItemInfoExpandedListener != null) {
            /** Uncomment to allow expand time chat
            onItemInfoExpandedListener.onItemInfoExpanded(position);
             **/
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.view_chat_message_header, parent, false);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.header_date_textview);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        QBChatMessage chatMessage = getItem(position);
        holder.dateTextView.setText(DateTimeUtil.convertTimeStampToDate(chatMessage.getDateSent(), "MMMM dd"));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.dateTextView.getLayoutParams();
        if (position == 0) {
            lp.topMargin = ResourceUtils.getDimen(R.dimen.dimen_2x);
        } else {
            lp.topMargin = 0;
        }
        holder.dateTextView.setLayoutParams(lp);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        QBChatMessage chatMessage = getItem(position);
        return Long.parseLong(DateTimeUtil.convertTimeStampToDate(chatMessage.getDateSent(), "ddMMyyyy"));
    }

    private void setMessageBody(final ViewHolder holder, QBChatMessage chatMessage) {
        if (hasAttachments(chatMessage)) {
            Collection<QBAttachment> attachments = chatMessage.getAttachments();
            QBAttachment attachment = attachments.iterator().next();

            holder.messageBodyTextView.setVisibility(View.GONE);
//            holder.attachmentImageView.setVisibility(View.VISIBLE);
//            holder.attachmentProgressBar.setVisibility(View.VISIBLE);
//            Glide.with(context)
//                    .load(attachment.getUrl())
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model,
//                                                   Target<GlideDrawable> target, boolean isFirstResource) {
//                            e.printStackTrace();
//                            holder.attachmentImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                            holder.attachmentProgressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model,
//                                                       Target<GlideDrawable> target,
//                                                       boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.attachmentImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                            holder.attachmentProgressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
//                    .override(Consts.PREFERRED_IMAGE_SIZE_PREVIEW, Consts.PREFERRED_IMAGE_SIZE_PREVIEW)
//                    .dontTransform()
//                    .error(R.drawable.ic_error)
//                    .into(holder.attachmentImageView);
        } else {
            String body = chatMessage.getBody();
            if (body.contains(Constants.STR_TAG_OPEN) && body.contains(Constants.STR_TAG_CLOSE)){
                if (isIncoming(chatMessage)){
                    body = StringUtil.getStringBeforeCharacter(chatMessage.getBody()) +
                            StringUtil.convertToTextHtmlBold(context  ,
                                    StringUtil.getStringBetweenPairCharacters(chatMessage.getBody()),
                                    context.getResources().getColor(R.color.colorTextDeal)  );
                }else {
                    body = StringUtil.getStringBeforeCharacter(chatMessage.getBody()) +
                            StringUtil.convertToTextHtmlBold(context  ,
                                    StringUtil.getStringBetweenPairCharacters(chatMessage.getBody()),
                                    context.getResources().getColor(R.color.colorTextDeal)  );
                }
                holder.messageBodyTextView.setText(Html.fromHtml(body));
            }else {
                holder.messageBodyTextView.setText(body);
            }


            holder.messageBodyTextView.setVisibility(View.VISIBLE);
//            holder.attachmentImageView.setVisibility(View.GONE);
//            holder.attachmentProgressBar.setVisibility(View.GONE);
        }
    }

    private void setMessageAuthor(ViewHolder holder, QBChatMessage chatMessage) {
        if (isIncoming(chatMessage)) {
            QBUser sender = QbUsersHolder.getInstance().getUserById(chatMessage.getSenderId());
            /** Uncomment this to show opponent name
            holder.messageAuthorTextView.setText(sender.getFullName());
            holder.messageAuthorTextView.setVisibility(View.VISIBLE);
             **/

            if (hasAttachments(chatMessage)) {
                holder.messageAuthorTextView.setBackgroundResource(R.drawable.shape_rectangle_semi_transparent);
                holder.messageAuthorTextView.setTextColor(ResourceUtils.getColor(R.color.white));
            } else {
                holder.messageAuthorTextView.setBackgroundResource(0);
                holder.messageAuthorTextView.setTextColor(ResourceUtils.getColor(R.color.dark_grey));
            }
        } else {
            holder.messageAuthorTextView.setVisibility(View.GONE);
        }
    }

    private void setMessageInfo(QBChatMessage chatMessage, ViewHolder holder) {
        holder.messageInfoTextView.setText(DateTimeUtil.convertTimeStampToDate(chatMessage.getDateSent(), "HH:mm"));
    }

    @SuppressLint("RtlHardcoded")
    private void setIncomingOrOutgoingMessageAttributes(ViewHolder holder, QBChatMessage chatMessage) {
        boolean isIncoming = isIncoming(chatMessage);
        int gravity = isIncoming ? Gravity.LEFT : Gravity.RIGHT;
        holder.messageContainerLayout.setGravity(gravity);
        holder.messageInfoTextView.setGravity(gravity);

//        int messageBodyContainerBgResource = isIncoming ? R.drawable.bg_message_white : R.drawable.bg_message_red;
//        int textColorResource = isIncoming ? R.color.textColorPrimary : R.color.white;

        int textColorResource = 0;
        int messageBodyContainerBgResource = 0;
        String body = chatMessage.getBody();
        if (isIncoming){
            messageBodyContainerBgResource =  isContainAction(body) ? R.drawable.bg_message_income_accent: R.drawable.bg_message_white;
            textColorResource              =  isContainAction(body) ? R.color.white: R.color.textColorPrimary;
        }else {
            messageBodyContainerBgResource =  isContainAction(body) ? R.drawable.bg_message_red: R.drawable.bg_message_grey;
            textColorResource               =  isContainAction(body)? R.color.white: R.color.textColorPrimary;
        }


        if (hasAttachments(chatMessage)) {
            holder.messageBodyContainerLayout.setBackgroundResource(0);
            holder.messageBodyContainerLayout.setPadding(0, 0, 0, 0);
//            holder.attachmentImageView.setMaskResourceId(messageBodyContainerBgResource);
        } else {
            holder.messageBodyContainerLayout.setBackgroundResource(messageBodyContainerBgResource);
        }

//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.messageAuthorTextView.getLayoutParams();
//        if (isIncoming && hasAttachments(chatMessage)) {
//            lp.leftMargin = ResourceUtils.getDimen(R.dimen.chat_message_attachment_username_margin);
//            lp.topMargin = ResourceUtils.getDimen(R.dimen.chat_message_attachment_username_margin);
//        } else if (isIncoming) {
//            lp.leftMargin = ResourceUtils.getDimen(R.dimen.chat_message_username_margin);
//            lp.topMargin = 0;
//        }
//        holder.messageAuthorTextView.setLayoutParams(lp);


        holder.messageBodyTextView.setTextColor(ResourceUtils.getColor(textColorResource));
    }

    private int getBackgroundBody(boolean isIncoming, QBChatMessage chatMessage) {
        int background = 0;
        String body = chatMessage.getBody();
        if (isIncoming){
            background =  isContainAction(body) ? R.drawable.bg_message_income_accent: R.drawable.bg_message_white;
        }else {
            background =  isContainAction(body) ? R.drawable.bg_message_red: R.drawable.bg_message_grey;
        }

        return 0;
    }

    private boolean isContainAction(String body){
        if ( body.contains(String.format(context.getString(R.string.msg_notify_opponent_disagreed_deal), "").trim())
              || body.contains(String.format(context.getString(R.string.msg_notify_opponent_agreed_deal), "").trim())
                || body.contains(String.format(context.getString(R.string.msg_notify_opponent_agreed_deal_with_id), "","", "").trim())
                ||  body.contains(String.format(context.getString(R.string.user_canceled_deal), "").trim()))
            return true;
        return false;
    }

    private boolean hasAttachments(QBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }

    private boolean isIncoming(QBChatMessage chatMessage) {
        QBUser currentUser = ChatHelper.getCurrentUser();
        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }

    private boolean isRead(QBChatMessage chatMessage) {
        Integer currentUserId = ChatHelper.getCurrentUser().getId();
        return !CollectionsUtil.isEmpty(chatMessage.getReadIds()) && chatMessage.getReadIds().contains(currentUserId);
    }

    private void readMessage(QBChatMessage chatMessage) {
        try {
            chatDialog.readMessage(chatMessage);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private static class HeaderViewHolder {
        public TextView dateTextView;
    }

    private static class ViewHolder {
        private TextView messageBodyTextView, messageAuthorTextView, messageInfoTextView;
        private LinearLayout messageContainerLayout;
        private RelativeLayout messageBodyContainerLayout;
//        private MaskedImageView attachmentImageView;
//        private ProgressBar attachmentProgressBar;
    }

    public interface OnItemInfoExpandedListener {
        void onItemInfoExpanded(int position);
    }
}
