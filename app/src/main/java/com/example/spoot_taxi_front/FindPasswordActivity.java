package com.example.spoot_taxi_front;



import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.spoot_taxi_front.databinding.ActivityFindPasswordBinding;


public class FindPasswordActivity extends AppCompatActivity {

    private ActivityFindPasswordBinding binding;

    InputChecker ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);

        //이메일 인증 클릭
        binding.emailVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ic = new InputChecker();
                boolean isEmailFilled = ic.checkEmailFilled(binding.emailEdt, binding.emailTv);
                if (!isEmailFilled) {
                    return;
                }
                boolean isCorrectEmailFormat = ic.checkEmailFormat(binding.emailEdt, binding.emailTv);
                if (!isCorrectEmailFormat) {
                    return;
                }

                //이메일 입력했고 format이 맞다.

                //가입된 이메일인지 api 요청

                //가입된 이메일이 맞다면 verification 요청

                //이메일 인증 완료했다면 새로운 비밀번호 설정하는 액티비티로
            }
        });

        //이전 클릭
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}