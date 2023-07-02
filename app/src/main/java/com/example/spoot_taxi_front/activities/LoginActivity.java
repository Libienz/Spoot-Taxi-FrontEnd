package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.dto.Gender;
import com.example.spoot_taxi_front.dto.User;
import com.example.spoot_taxi_front.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText enterID;
    private EditText enterPW;
    private Button loginButton;
    private Button registerButton;
    private Button lostPWButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enterID = findViewById(R.id.enter_ID);
        enterPW = findViewById(R.id.enter_PW);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        lostPWButton = findViewById(R.id.lost_pw);


        //로그인 클릭
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = enterID.getText().toString();
                String password = enterPW.getText().toString();
                // 로그인 기능 구현
                // api 요청 부분
                Toast.makeText(LoginActivity.this, "로그인 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();

                //로그인 성공 currentUser를 앱 전역 범위의 싱글톤 객체에 저장하고 메인 화면 전환

                //테스트 유저 생성
                User testuser = new User("202010891@sangmyung.kr", "1234", null, Gender.MALE);
                SessionManager.getInstance().setCurrentUser(testuser); //api에서 반환받은 유저정보 넣어야 하는데 일단 테스트 유저 정보를 넣자
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //계정 만들기 클릭
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);

            }
        });

        //비밀번호 찾기 클릭
        lostPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
//                Toast.makeText(LoginActivity.this, "비밀번호를 잊으셨나요? 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}