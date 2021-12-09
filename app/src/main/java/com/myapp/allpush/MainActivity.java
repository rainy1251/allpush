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
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import static com.huawei.hms.push.constant.RemoteMessageConst.Notification.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvSkip = findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(v -> {
//            setBadgeNum(0);
           Intent intent = new Intent(MainActivity.this, PushGetTokenActivity.class);
//            startActivity(intent);
            showNotification(intent);
        });
//        createNotificationChannel();
        getIntentData(getIntent());

        requestPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestPermission() {
        //打开系统消息通知设置页面
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = mNotificationManager.getNotificationChannel(getString(R.string.channel_name));
        if (channel!=null&&channel.getImportance() == NotificationManager.IMPORTANCE_HIGH) {
            return;
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0引导，引导到CHANNEL_ID对应的渠道设置下
            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.channel_name));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //android 5.0-7.0,引导到所有渠道设置下（单个渠道没有具体的设置）
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            //其他
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        startActivity(intent);


    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            // Directly add parameters to the Intent. Obtain data as follows:
            String type = intent.getStringExtra("Type");
            String value = intent.getStringExtra("Value");
            String msg = intent.getStringExtra("msg");
            Log.d("rainPush", "收到通知" + msg);
            if (type != null && type.equals("Trend")) {
                Intent skip = new Intent(MainActivity.this, PushGetTokenActivity.class);
                skip.putExtra("trend", value);
                startActivity(skip);
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);//TODO  会重置
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
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            long[] pattern = {100, 400, 100, 400};
//            channel.setVibrationPattern(pattern);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

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