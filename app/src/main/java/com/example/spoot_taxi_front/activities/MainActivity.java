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
import android.view.MenuItem;
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
import com.example.spoot_taxi_front.utils.NewMessageEvent;
import com.example.spoot_taxi_front.utils.NonReadCountUpdateEvent;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.util.maps.helper.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_RALLY = "RallyFragment";
    private static final String TAG_CHAT = "ChatFragment";
    private static final String TAG_SETTINGS = "SettingsFragment";
    private static final String TAG_MATCHING = "MatchingFragment";
    private RallyFragment rallyFragment;
    private ChatFragment chatFragment;
    private SettingsFragment settingsFragment;
    private MatchingFragment matchingFragment;
    private LocalChatRoomManager localChatRoomManager;
    private ActivityMainBinding binding;
    BadgeDrawable badgeDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        localChatRoomManager = LocalChatRoomManager.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        localChatRoomManager.loadChatRoomsFromServer();

        // 채팅 아이템의 Badge 설정
        MenuItem chatMenuItem = binding.navigationView.getMenu().findItem(R.id.chatFragment);
        badgeDrawable = binding.navigationView.getOrCreateBadge(chatMenuItem.getItemId());
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(10); // 안읽은 메시지 개수

        int totalNonReadCount = localChatRoomManager.getTotalNonReadCount();
        if (totalNonReadCount > 0) {
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(totalNonReadCount);
        } else {
            badgeDrawable.setVisible(false);
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus 해제
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessageArrived(NonReadCountUpdateEvent event) {
        Log.d("BadgeUpdate", "onNewMessageArrived: ");
        int totalNonReadCount = localChatRoomManager.getTotalNonReadCount();
        if (totalNonReadCount > 0) {
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(totalNonReadCount);
        } else {
            badgeDrawable.setVisible(false);
        }


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