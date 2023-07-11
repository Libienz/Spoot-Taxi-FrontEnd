package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.dto.UserDto;

public class VerificationActivity extends AppCompatActivity {

    private Button vButton;
    private Button prevButton;


    public void callVerificationApi(UserDto userDto) {

    }
    public void callJoinApi(UserDto userDto) {

        // Dto 넣어서 api 호출

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Retrieve the UserDto object from the Intent
        User user = (User) getIntent().getSerializableExtra("user");

        TextView tv = findViewById(R.id.emailTv);
        tv.setText(user.getEmail());

        vButton = findViewById(R.id.verificationBtn);
        prevButton = findViewById(R.id.prevBtn);

        //인증 클릭
        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                callVerificationApi(user);
                //api 반응에 따라 분기

                //인증 성공
//                callJoinApi(user);
                //인증 실패 (예외처리)

            }
        });

        //이전 클릭
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}