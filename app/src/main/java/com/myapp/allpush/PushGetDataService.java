package com.myapp.allpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import java.util.Arrays;

import static com.huawei.hms.push.constant.RemoteMessageConst.Notification.CHANNEL_ID;

public class PushGetDataService extends HmsMessageService {
    private static final String TAG = "rainPush";

    // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
    // extends HmsMessageService super class
    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "received refresh token:" + token);
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private int num = 0;
    private final static String CODELABS_ACTION = "com.huawei.codelabpush.action";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }
        num++;
        setBadgeNum(num);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onMessageReceived");
        intent.putExtra("msg", "onMessageReceived called, message id:" + message.getMessageId() + ", payload data:"
                + message.getData());

        // sendBroadcast(intent);
        showNotification(intent);

        Log.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());

        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            Log.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
        Boolean judgeWhetherIn10s = false;
        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process message within 10s
            processWithin10s(message);
        }
    }

    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }

    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }


    /**
     * set badge number
     */
    public void setBadgeNum(int num) {
        try {

            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.myapp.allpush");
            bunlde.putString("class", "com.myapp.allpush.MainActivity");
            bunlde.putInt("badgenumber", num);
            this.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {

        }
    }


    private void showNotification(Intent intent) {

        Notification notification;
        NotificationManager manager;
        int NOTIFY_ID = 100;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent hangIntent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 1001,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = getString(R.string.channel_name);//应用频道Id唯一值， 长度若太长可能会被截断，
        String CHANNEL_NAME = getString(R.string.channel_name);//最长40个字符，太长会被截断
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("这是一个猫头")
                .setContentText("点我返回应用")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(hangPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.cat))
                .setAutoCancel(true)
                .build();

        //Android 8.0 以上需包添加渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }

        manager.notify(NOTIFY_ID, notification);
    }

}
