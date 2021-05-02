package com.wfour.onlinestoreapp.quickblox.conversation.utils;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.users.model.QBUser;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.quickblox.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Suusoft on 13.05.16.
 */
public class PushNotificationSender {

    public static void sendPushMessage(final Context context, List<Integer> recipients) {
        QBUser qbUser = SharedPreferencesUtil.getQbUser();

        String outMessage = String.format(String.valueOf(R.string.text_push_notification_message), qbUser.getFullName());

        // Send Push: create QuickBlox Push Notification Event
        QBEvent qbEvent = new QBEvent();
        StringifyArrayList<Integer> userIds = new StringifyArrayList<>(recipients);
        qbEvent.setUserIds(userIds);
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        qbEvent.setPushType(QBPushType.GCM);
        // Generic push - will be delivered to all platforms (Android, iOS, WP, Blackberry..)
        HashMap<String, Object> data = new HashMap<>();
        data.put(Args.MESSAGE, outMessage);
        data.put(Args.NOTIFICATION_TYPE, ChatConfigs.QUICKBLOX_CALLING);
        data.put(Args.QB_USER, new Gson().toJson(qbUser));
        qbEvent.setMessage(data);

        QBPushNotifications.createEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Toast.makeText(context, "Pushed successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }
}
