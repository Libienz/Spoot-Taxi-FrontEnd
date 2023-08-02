package com.example.spoot_taxi_front.network.fcm;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.activities.MainActivity;
import com.example.spoot_taxi_front.activities.UpdateActivity;
import com.example.spoot_taxi_front.utils.MatchingSuccessEvent;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg, title;

    //token을 서버로 전송
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("SessionManagerdeviceToken", "onNewToken: " + token);
        SessionManager.getInstance().setDeviceToken(token);
        super.onNewToken(token);

    }

    //수신한 메시지를 처리
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("libienz", "onMessageReceived");
        title = remoteMessage.getNotification().getTitle();
        Log.d("libienz", "rcv title: " + title);
        msg = remoteMessage.getNotification().getBody();
        if (title.equals("matchSuccess")) {
            EventBus.getDefault().post(new MatchingSuccessEvent());
        }

        //알림 채널 생성
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // 알림 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel_id";
            String channelName = "My Channel";
            String channelDescription = "This is my notification channel.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
