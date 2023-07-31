package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.requests.LoginRequest;
import com.example.spoot_taxi_front.network.dto.responses.LoginResponse;
import com.example.spoot_taxi_front.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        //레이아웃 요소 바인딩
        enterID = findViewById(R.id.enter_ID);
        enterPW = findViewById(R.id.enter_PW);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        lostPWButton = findViewById(R.id.lost_pw);

        //Api Client 생성
        AuthApi authApi = ApiManager.getInstance().createAuthApi(SessionManager.getInstance().getJwtToken());

        //로그인 클릭
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = enterID.getText().toString();
                String password = enterPW.getText().toString();
                LoginRequest loginRequest = LoginRequest.create(email, password);

                Call<LoginResponse> call = authApi.login(loginRequest);

                //enqueue: 로그인 api 호출을 큐에 넣고 비동기 처리한다.
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        handleLoginResponse(response.code(), response.body());
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "로그인 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                    }
                });


            }
        });

        //계정 만들기 클릭
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 올바른 흐름
                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                startActivity(intent);
                // 테스트용 흐름
//                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
//                startActivity(intent);

            }
        });

        //비밀번호 찾기 클릭
        lostPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void handleLoginResponse(int statusCode, LoginResponse responseBody) {
        switch (statusCode) {
            case 200:
                processLoginSuccess(responseBody);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            default:
                processLoginFail(responseBody);
                break;

        }
    }

    private void processLoginSuccess(LoginResponse jsonResponse) {

        UserDto userDto = jsonResponse.getUserDto();
        String token = jsonResponse.getToken();
        Log.e("Login Success", userDto.toString());
        Log.e("Login Success", token);

        // SessionManager 현재 세션 관리
        User user = new User(userDto.getEmail(), userDto.getPassword(), userDto.getName(), userDto.getImgUrl(), userDto.getGender());
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.setJwtToken(token);
        sessionManager.setCurrentUser(user);
    }


    private void processLoginFail(LoginResponse jsonResponse) {
        Toast.makeText(getApplicationContext(), "로그인 실패. 아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();

    }






}