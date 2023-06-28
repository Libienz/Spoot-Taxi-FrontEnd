package com.example.spoot_taxi_front;


import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class LocationActivity extends AppCompatActivity{
    Map mapFragment = new Map();
    Chatting chattingFragment = new Chatting();
    Setting settingFragment = new Setting();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        int homeFragment = R.id.homeFragment;
        // 처음에는 home 화면
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,mapFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 클릭 이벤트 처리
                int itemId = item.getItemId();
                if (itemId == R.id.homeFragment) {// 홈 버튼 클릭 시 처리
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, mapFragment).commit();
                    return true;
                } else if (itemId == R.id.chattingFragment) {// 채팅 버튼 클릭 시 처리
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, chattingFragment).commit();
                    return true;
                } else if (itemId == R.id.settingFragment) {// 설정 버튼 클릭 시 처리
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, settingFragment).commit();
                    return true;
                }
                return false;
            }
        });


    }

}
