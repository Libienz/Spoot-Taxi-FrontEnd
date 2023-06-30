package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityPasswordResetBinding;

public class PasswordResetActivity extends AppCompatActivity {

    private InputChecker ic;
    private ActivityPasswordResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset);


        //비밀번호 변경 클릭
        binding.pwResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ic = new InputChecker();
                boolean isPasswordFilled = ic.checkPasswordFilled(binding.inputPassword, binding.pwTv);
                if (!isPasswordFilled) {
                    return;
                }

                boolean isPasswordCheckFilled = ic.checkPasswordConfirmFilled(binding.inputPwck, binding.pwckTv);
                if (!isPasswordCheckFilled) {
                    return;
                }

                boolean isSamePassword = ic.checkSamePassword(binding.inputPassword, binding.inputPwck, binding.pwckTv);
                if (!isSamePassword) {
                    return;
                }

                //유저정보 재설정 요청 api 호출

                //비밀번호 수정

            }
        });

        //이전 클릭
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}