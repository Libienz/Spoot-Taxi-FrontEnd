package com.example.spoot_taxi_front.activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.spoot_taxi_front.network.dto.responses.EmailVerificationResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityFindPasswordBinding;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindPasswordActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private final long TTIMER_DURATION = 5 * 60 * 1000; //5분 밀리초로 변환
    private ActivityFindPasswordBinding binding;
    InputChecker ic;
    AuthApi authApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);

        //Api client 생성
        authApi = ApiManager.getInstance().getAuthApi();

        //인증 창 안보이도록 설정: 인증 창은 이메일 입력되고 가입된 이메일인지 확인 후에 보이게 설정
        binding.verifyLayout.setVisibility(View.GONE);

        //인증 메일 발송 클릭
        binding.sendVerificationBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                binding.verifyTv.setText("");
                binding.verifyTimerTv.setText("");
                //이메일을 입력 안했는지, 포맷이 올바른지 확인
                ic = new InputChecker();
                boolean isEmailFilled = ic.checkEmailFilled(binding.emailEdt, binding.emailTv);
                if (!isEmailFilled) {
                    return;
                }
                boolean isCorrectEmailFormat = ic.checkEmailFormat(binding.emailEdt, binding.emailTv);
                if (!isCorrectEmailFormat) {
                    return;
                }



                String email = binding.emailEdt.getText().toString();
                Call<EmailVerificationResponse> verificationCall = authApi.sendVerificationEmailForUpdate(email);

                //이메일 인증 요청
                verificationCall.enqueue(new Callback<EmailVerificationResponse>() {
                    @Override
                    public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                        Boolean sended = response.body().getSuccess();
                        Integer verificationCode = response.body().getVerificationCode();
                        if (sended != null && sended) {
                            // 가입된 이메일임 -> 인증 코드로 진행
//                            Toast.makeText(getApplicationContext(), "인증메일 발송 완료!", Toast.LENGTH_SHORT).show();
                            processEmailverification(email, verificationCode);

                        } else {
                            // 가입되어 있지 않은 이메일임을 알리고 종료
                            binding.emailTv.setText("가입되어 있지 않은 이메일입니다");
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                        // API 호출 실패 처리
                        Toast.makeText(getApplicationContext(), "서버로 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                    }


                });


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

    public void processEmailverification(String email, Integer code) {

        binding.verifyLayout.setVisibility(View.VISIBLE);

        // 이전 타이머가 존재하는 경우 취소
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(TTIMER_DURATION, 1000) {
            //tick (현재 1초) 지날때마다
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                binding.verifyTimerTv.setText(timeString);
            }

            //타이머 끝나면
            @Override
            public void onFinish() {

                binding.emailTv.setText("인증 시간 초과! 다시 이메일 인증을 진행하세요");
                binding.verifyLayout.setVisibility(View.GONE);

            }
        };

        countDownTimer.start();
        //인증버튼 클릭
        binding.verificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.verifyCodeEdt.getText().toString().equals("")) {
                    binding.verifyTv.setText("잘못된 인증번호 입니다");
                    return;
                }
                Integer inputCode = Integer.parseInt(binding.verifyCodeEdt.getText().toString());
                //인증 코드 올바르게 입력: 비밀번호 reset하는 액티비티로 이동
                if (code.equals(inputCode)) {
                    binding.verifyTv.setText("");
                    Toast.makeText(getApplicationContext(), "이메일 인증 성공!", Toast.LENGTH_SHORT).show();
                    //email넘기면서 intent전환
                    Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                    intent.putExtra("email", email); // Add the email as an extra
                    startActivity(intent);

                }
                //인증 코드 부정확
                else {
                    binding.verifyTv.setText("잘못된 인증번호 입니다");
                    return;
                }
            }
        });


    }
}