package com.example.spoot_taxi_front;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = enterID.getText().toString();
                String password = enterPW.getText().toString();
                // 로그인 기능 구현
                // api 요청 부분
                Toast.makeText(LoginActivity.this, "로그인 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원 가입 화면으로 이동 코드 부분
                Toast.makeText(LoginActivity.this, "계정 만들기 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        lostPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기 화면으로 이동 코드 부분
                Toast.makeText(LoginActivity.this, "비밀번호를 잊으셨나요? 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}