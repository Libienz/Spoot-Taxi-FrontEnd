package com.example.spoot_taxi_front.activities;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.example.spoot_taxi_front.databinding.ActivityMainBinding;
import com.example.spoot_taxi_front.fragments.ChatFragment;
import com.example.spoot_taxi_front.fragments.MatchingFragment;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.fragments.RallyFragment;
import com.example.spoot_taxi_front.fragments.SettingsFragment;


import com.example.spoot_taxi_front.network.socket.WebSocketViewModel;
import com.example.spoot_taxi_front.utils.LocalChatRoomManager;
import com.example.spoot_taxi_front.utils.ChatRoomDataChange;
import com.google.android.material.badge.BadgeDrawable;
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
    private WebSocketViewModel webSocketViewModel;
    BadgeDrawable badgeDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webSocketViewModel = WebSocketViewModel.getInstance();
        //알림 권한 설정
        requestNotificationPermission();

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

    @Override
    protected void onResume() {
        super.onResume();

        if(webSocketViewModel.isConnected()){
            Log.d("resumeConnect","연결되어있어요");
        }
        if(!webSocketViewModel.isConnected()){
            Log.d("resumeDisConnect","비연결되어있어요");
            webSocketViewModel.reconnect();
            LocalChatRoomManager.getInstance().loadChatRoomsFromServer();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessageArrived(ChatRoomDataChange event) {
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

    //알림 권한 설정
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //해당 권한이 이전에 거부되었거나, 사용자에게 허용되지 않은 상태에서 다시 권한을 요청할 때 사용
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    //안드로이드 13이상부터는 런타임 퍼미션이 있음
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            } else {
                // 안드로이드 12 이하는 알림에 런타임 퍼미션 없으니, 설정 가서 켜보라고 권해볼 수 있겠다.
                showNotificationSettingAlert();
            }
        }
    }
    // 알림 설정 화면으로 이동 안내 메시지 띄우기
    private void showNotificationSettingAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림 권한 설정");
        builder.setMessage("매칭이 성사되는 경우 알림을 받기 위해 알림 권한을 허용해야 합니다. 설정 화면으로 이동하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openNotificationSettings();
            }
        });
        builder.setNegativeButton("아니오", null);
        builder.show();
    }
    // 알림 설정 화면으로 이동하는 메서드(안드로이드 12이하는 원래도 설정되어있지만 혹시 끈사람들에게 설정으로 이동시켜주는 메서드)
    private void openNotificationSettings() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(android.net.Uri.parse("package:" + getPackageName()));
        }
        startActivity(intent);
    }
    //안드 13용 런타임 알림 퍼미션
    private ActivityResultLauncher<String> requestNotificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean ok) {
                            if (ok) {
                                // 알림 권한 허용됨
                            } else {
                                // 알림 권한 거부. 필요한 처리 수행
                            }
                        }
                    });

}