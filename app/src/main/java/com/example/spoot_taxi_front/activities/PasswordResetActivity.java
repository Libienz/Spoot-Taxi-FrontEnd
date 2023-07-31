package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.responses.UserSaveResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityPasswordResetBinding;
import com.example.spoot_taxi_front.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetActivity extends AppCompatActivity {

    private InputChecker ic;
    private ActivityPasswordResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset);
        String email = getIntent().getStringExtra("email");
        binding.emailTv.setText(email);

        //Api Client 생성
        AuthApi authApi = ApiManager.getInstance().createAuthApi(SessionManager.getInstance().getJwtToken());

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


                String newPassword = binding.inputPassword.getText().toString();
                //유저정보 재설정 요청 api 호출
                UserDto updateDto = new UserDto(email, newPassword, null, null, null);
                Call<UserSaveResponse> updatePasswordCall = authApi.updateUserPassword(email, updateDto);
                updatePasswordCall.enqueue(new Callback<UserSaveResponse>() {
                    @Override
                    public void onResponse(Call<UserSaveResponse> call, Response<UserSaveResponse> response) {
                        //수정 성공
                        if (response.body().getSuccess() == Boolean.TRUE) {
                            //비밀번호 재설정이 완료되었습니다 Toastmsg -> 로그인 액티비티로 이동
                            Toast.makeText(getApplicationContext(), "비밀번호 재설정이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                        }
                        //수정 실패
                        else {
                            //유저 정보 수정에 실패하였습니다.
                            Toast.makeText(getApplicationContext(), "비밀번호 재설정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSaveResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "서버로 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                    }
                });
            };
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