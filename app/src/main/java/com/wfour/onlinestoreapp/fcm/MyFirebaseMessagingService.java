package com.wfour.onlinestoreapp.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.wfour.onlinestoreapp.AppController;
import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.configs.ChatConfigs;
import com.wfour.onlinestoreapp.datastore.DataStoreManager;
import com.wfour.onlinestoreapp.globals.Args;
import com.wfour.onlinestoreapp.globals.Constants;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.objects.PaymentMethodObj;
import com.wfour.onlinestoreapp.objects.UserObj;
import com.wfour.onlinestoreapp.retrofit.ApiUtils;
import com.wfour.onlinestoreapp.retrofit.respone.BaseRespone;
import com.wfour.onlinestoreapp.view.activities.SplashLoginActivity;
import com.wfour.onlinestoreapp.view.chat.chatutils.PreferenceUtils;
import com.wfour.onlinestoreapp.view.chat.groupchat.GroupChannelActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static int REQUEST_CODE = 0;
    private static int NOTIFICATION_ID = 0;
    private static String notifi_chanel = "notify_001";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        DataStoreManager.saveTokenFCM(s);
        sendGcm(s);
    }

    private void sendGcm(String s) {
        Log.e(TAG, "sendGcm: " + s);
        ApiUtils.getAPIService().sendGcmID(s).enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                Log.e(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        Log.e(TAG, "From: " + new Gson().toJson(remoteMessage.getData()));


        String channelUrl = null;
        try {
            if (remoteMessage.getData().containsKey("sendbird")) {
                JSONObject sendbird = new JSONObject(remoteMessage.getData().get("sendbird"));
                JSONObject channel = (JSONObject) sendbird.get("channel");
                channelUrl = (String) channel.get("channel_url");

                // If you want to customize a notification with the received FCM message,
                // write your method like the sendNotification() below.
                senNotifiCationSendbird(this, remoteMessage.getData().get("message"), channelUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sendNotification(remoteMessage.getData().get("message"));
        // Check if message contains a data payload.
        String message = "", type = "";
        if (remoteMessage.getData().size() > 0) {
            message = remoteMessage.getData().get(Args.MESSAGE);
            type = remoteMessage.getData().get(Args.NOTIFICATION_TYPE);
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        if (type != null) {
            if (type.equals(ChatConfigs.QUICKBLOX_MESSAGE) || type.equalsIgnoreCase(ChatConfigs.QUICKBLOX_PAY_FOR_DEAL)) {
                String json = remoteMessage.getData().get(Args.RECENT_CHAT_OBJ);
                // Replace json if it's not correct format
                if (json.contains("=>")) {
                    json = json.replace("=>", ":");
                }
                Log.e(TAG, json);
                //RecentChatObj obj = new Gson().fromJson(json, RecentChatObj.class);

                if (type.equals(ChatConfigs.QUICKBLOX_MESSAGE)) {
//                    if (SharedPreferencesUtil.hasQbUser() && (!GlobalFunctions.isForeground(obj) || !DataStoreManager.conversationIsLive())) {
//                        // Remove sender's name(sender's name is just for iOS)
//                        if (message.contains(":")) {
//                            message = message.substring((message.indexOf(":") + 1)).trim();
//                        }
//
//                        obj.setLastMessage(message);
//                        //sendQBNotification(message, type, obj);
//
//                        // Must also set negotiation to false here
//                        if (message.endsWith(String.format(getString(R.string.user_canceled_deal), "").trim())) {
//                            DataStoreManager.saveDealNegotiation(obj, false);
//                        }
//                    }
                } else if (type.equalsIgnoreCase(ChatConfigs.QUICKBLOX_PAY_FOR_DEAL)) {
                    // Remove sender's name(sender's name is just for iOS)
                    if (message.contains(":")) {
                        message = message.substring((message.indexOf(":") + 1)).trim();
                    }

                    // Must also set negotiation to false here
//                    DataStoreManager.saveDealNegotiation(obj, false);
//                    if (GlobalFunctions.isForeground(obj)) {
//                        // Update option menu
//                        sendMessage(false);
//                    }

                    // Update balance if client paid via credits
                    String paymentMethod = remoteMessage.getData().get(Args.PAYMENT_METHOD);
                    if (paymentMethod.equalsIgnoreCase(PaymentMethodObj.CREDITS)) {
                        String fare = remoteMessage.getData().get(Args.FARE);
                        if (fare == null || fare.equals("")) {
                            fare = "0";
                        } else if (fare.contains(",")) {
                            fare = fare.replace(",", "");
                        }
                        UserObj user = DataStoreManager.getUser();
                        user.setBalance(user.getBalance() + Float.parseFloat(fare));
                        DataStoreManager.saveUser(user);
                        if (AppController.getInstance().getiRedeem() != null) {
                            AppController.getInstance().getiRedeem().updateBalace(user.getBalance());
                        }
                    }

                    sendNotification(message);
                }
            } else if (type.equals(ChatConfigs.TYPE_DEAL)) {
                String id = remoteMessage.getData().get(Args.BALANCE);
                sendNewDeal(message, ChatConfigs.TYPE_DEAL, id);
            } else if (type.equals(Args.TYPE_BALANCE)) {
//                UserObj user = DataStoreManager.getUser();
//                String balance = remoteMessage.getData().get(Args.BALANCE);
//                user.setBalance(Float.parseFloat(balance));
//                DataStoreManager.saveUser(user);
//                if (AppController.getInstance().getiRedeem() != null) {
//                    AppController.getInstance().getiRedeem().updateBalace(user.getBalance());
//                }
                sendNotification(message);
                EventBus.getDefault().post(new GlobalFunctions.updateBalance());
            } else {
                sendNotification(message);
                EventBus.getDefault().post(new GlobalFunctions.updateBalance());
            }
        }

        /*if (type.equals(Args.TYPE_BALANCE)) {
            UserObj user = DataStoreManager.getUser();
            String balance = remoteMessage.getData().get(Args.BALANCE);
            Log.e("EE", "BAL: " + balance);
            user.setBalance(Float.parseFloat(balance));
            DataStoreManager.saveUser(user);
            if (AppController.getInstance().getiRedeem() != null) {
                AppController.getInstance().getiRedeem().updateBalace(user.getBalance());
            }
        }*/
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private void sendNotification(String messageBody) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent intent = new Intent(this, SplashLoginActivity.class);
        REQUEST_CODE++;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notifi_chanel)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NOTIFICATION_ID++;
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

//    private void sendQBNotification(String messageBody, String notificationType, RecentChatObj obj) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel();
//        }
//
//        Intent intent = new Intent(this, SplashLoginActivity.class);
//        intent.putExtra(Args.RECENT_CHAT_OBJ, obj);
//        intent.putExtra(Args.NOTIFICATION_TYPE, notificationType);
//
//        REQUEST_CODE++;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
////        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notifi_chanel)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(obj.getQbUser().getFullName())
//                .setContentText(messageBody)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(obj.getQbUser().getId(), notificationBuilder.build());
//    }

    private void sendNewDeal(String messageBody, String notificationType, String idDeal) {
        Log.e(TAG, "sendNewDeal: " + "tag login");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        Intent intent = new Intent(this, SplashLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(Args.KEY_ID_DEAL, idDeal);
        bundle.putString(Args.NOTIFICATION_TYPE, notificationType);
        intent.putExtras(bundle);

        REQUEST_CODE++;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notifi_chanel)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Integer.parseInt(idDeal), notificationBuilder.build());
    }

    private void sendMessage(boolean negotiated) {
        // The string "my-integer" will be used to filer the intent
        Intent intent = new Intent(Constants.INTENT_ACTION_UPDATE_MENU);
        // Adding some data
        intent.putExtra(Args.NEGOTIATED, negotiated);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void senNotifiCationSendbird(Context context, String messageBody, String channelUrl) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.message);
        final String CHANNEL_ID = "CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// Build.VERSION_CODES.O
            mChannel = new NotificationChannel(CHANNEL_ID, "CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.parseColor("#FF0000"));
            mChannel.enableLights(true);
            mChannel.setDescription("Notification chat");
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mChannel.setSound(soundUri, audioAttributes);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(context, GroupChannelActivity.class);
        intent.putExtra("groupChannelUrl", channelUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setColor(Color.parseColor("#FF0000"))  // small icon background color
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setSound(soundUri);
        }
        if (PreferenceUtils.getNotificationsShowPreviews(context)) {
            notificationBuilder.setContentText(messageBody);
        } else {
            notificationBuilder.setContentText("Somebody sent you a message.");
        }
        int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }

        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager
                mNotificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = notifi_chanel;
        // The user-visible name of the channel.
        CharSequence name = "Notification";
        // The user-visible description of the channel.
        String description = "Notification Application";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mNotificationManager.createNotificationChannel(mChannel);
    }
}
