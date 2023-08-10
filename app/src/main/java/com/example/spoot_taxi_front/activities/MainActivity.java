package com.example.spoot_taxi_front.activities;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.spoot_taxi_front.databinding.ActivityMainBinding;
import com.example.spoot_taxi_front.fragments.ChatFragment;
import com.example.spoot_taxi_front.fragments.MatchingFragment;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.fragments.RallyFragment;
import com.example.spoot_taxi_front.fragments.SettingsFragment;


import com.example.spoot_taxi_front.utils.LocalChatRoomManager;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.util.maps.helper.Utility;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_RALLY = "RallyFragment";
    private static final String TAG_CHAT = "ChatFragment";
    private static final String TAG_SETTINGS = "SettingsFragment";
    private static final String TAG_MATCHING = "MatchingFragment";
    private RallyFragment rallyFragment;
    private ChatFragment chatFragment;
    private SettingsFragment settingsFragment;
    private MatchingFragment matchingFragment;

    private ActivityMainBinding binding;
    Button matchingButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        rallyFragment = new RallyFragment();
        chatFragment = new ChatFragment();
        settingsFragment = new SettingsFragment();
        matchingFragment = new MatchingFragment();

        String keyHash = Utility.getKeyHash(this);
        Log.d("카카오키해시", keyHash);

        Intent intent = getIntent();
        if(intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if(notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        SessionManager.getInstance().setDeviceToken(token);
                        // Log and toast
                        Log.d("FCM", token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        setFragment(TAG_RALLY, rallyFragment);

        binding.navigationView.setSelectedItemId(R.id.homeFragment);

        binding.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Log.d("libienz", "libienz");
            if (itemId == R.id.homeFragment) {
                setFragment(TAG_RALLY, rallyFragment);
            } else if (itemId == R.id.chatFragment) {
                setFragment(TAG_CHAT, chatFragment);
            } else if (itemId == R.id.settingsFragment) {
                setFragment(TAG_SETTINGS, settingsFragment);
            } else if (itemId == R.id.matchingFragment) {
                setFragment(TAG_MATCHING, matchingFragment);
        }
            return true;
        });

    }

    protected void setFragment(String tag, Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            Log.d("프래그먼트 추가로그",fragment.toString());
            fragmentTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment home = fragmentManager.findFragmentByTag(TAG_RALLY);
        Fragment chat = fragmentManager.findFragmentByTag(TAG_CHAT);
        Fragment settings = fragmentManager.findFragmentByTag(TAG_SETTINGS);
        Fragment matching = fragmentManager.findFragmentByTag(TAG_MATCHING);


        if (home != null){
            fragmentTransaction.hide(home);
        }

        if (chat != null){
            fragmentTransaction.hide(chat);
        }

        if (settings != null){
            fragmentTransaction.hide(settings);
        }
        if (matching != null) {
            fragmentTransaction.hide(matching);
        }

        if (tag.equals(TAG_RALLY)) {
            if (home != null){
                fragmentTransaction.show(home);
            }
        } else if (tag.equals(TAG_CHAT)) {
            if (chat != null) {
                fragmentTransaction.show(chat);
            }
        } else if (tag.equals(TAG_SETTINGS)) {
            if (settings != null) {
                fragmentTransaction.show(settings);
            }
        } else {
            if (matching != null) {
                fragmentTransaction.show(matching);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}