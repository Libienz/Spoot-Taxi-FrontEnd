package com.example.spoot_taxi_front.network.fcm;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spoot_taxi_front.activities.UpdateActivity;
import com.example.spoot_taxi_front.utils.MatchingSuccessEvent;
import com.example.spoot_taxi_front.utils.SessionManager;
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
        EventBus.getDefault().post(new MatchingSuccessEvent());

//        Intent intent = new Intent(this, UpdateActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setVibrate(new long[]{1,1000});
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0,mBuilder.build());
//
//        mBuilder.setContentIntent(contentIntent);
    }

}
