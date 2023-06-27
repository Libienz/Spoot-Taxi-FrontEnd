package com.example.spoot_taxi_front;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2초간 Splash 화면을 표시하기 위한 딜레이 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 일정 시간이 지난 후 메인 액티비티로 전환하기 위해 핸들러를 사용합니다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 메인 액티비티로 전환하기 위한 인텐트를 생성합니다.
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // 현재 액티비티를 종료합니다.
                finish();
            }
        }, SPLASH_DELAY);
    }
}