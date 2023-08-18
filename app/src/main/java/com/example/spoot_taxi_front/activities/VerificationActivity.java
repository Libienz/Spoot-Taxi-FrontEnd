package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityVerificationBinding;
import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.dto.responses.EmailVerificationResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding binding;
    private InputChecker ic;
    private AuthApi authApi;
    private CountDownTimer countDownTimer;
    private final long TTIMER_DURATION = 5 * 60 * 1000; //5분 밀리초로 변환



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        authApi = ApiManager.getInstance().createAuthApi(SessionManager.getInstance().getJwtToken());
        ic = new InputChecker();

        //인증 레이아웃 숨겨놓기
        binding.verifyLayout.setVisibility(View.GONE);

        //인증 메일 발송 클릭
        binding.sendVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.verifyTv.setText("");
                binding.verifyTimerTv.setText("");
                Toast.makeText(getApplicationContext(), "가입여부 체크 및 전송 중..", Toast.LENGTH_SHORT).show();
                boolean isEmailFilled = ic.checkEmailFilled(binding.emailEdt, binding.emailTv);
                boolean isCorrectEmailForm = ic.checkEmailFormat(binding.emailEdt, binding.emailTv);

                //filled, malform 체크
                if (!isEmailFilled || !isCorrectEmailForm) {
                    return;
                }

                String email = binding.emailEdt.getText().toString();

                Call<EmailVerificationResponse> sendVerificationEmailCall = authApi.sendVerificationEmailForJoin(email, false);
                sendVerificationEmailCall.enqueue(new Callback<EmailVerificationResponse>() {
                    @Override
                    public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                        Toast.makeText(getApplicationContext(), "인증메일 발송 완료!", Toast.LENGTH_SHORT).show();
                        handleSendVerificationEmailResponse(response, email);

                    }

                    @Override
                    public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                        // API 호출 실패 처리
                        Toast.makeText(getApplicationContext(), "서버로 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                    }
                });


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

    private void handleSendVerificationEmailResponse(Response<EmailVerificationResponse> response, String email) {
        int statusCode = response.code();
        switch (statusCode) {
            case 200:
                // 가입되어 있지 않은 이메일이어서 인증메일이 발송됨 -> 이메일 인증 후 회원 가입 프로세스 진행
                binding.emailTv.setText("");
                Integer verificationCode = response.body().getVerificationCode();
                processEmailverification(email, verificationCode);
                break;

            case 409:
                // 가입된 이메일이라 전송되지 않음 -> 회원가입 할 필요 X
                binding.emailTv.setText("이미 가입되어 있는 이메일입니다");
                break;
            default:
                break;

        }

    }

    public void processEmailverification(String email, Integer code) {


        binding.verifyLayout.setVisibility(View.VISIBLE);

        // 타이머 세팅
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
                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
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