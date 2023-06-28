package com.example.spoot_taxi_front;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.spoot_taxi_front.databinding.ActivityJoinBinding;
import com.example.spoot_taxi_front.dto.Gender;
import com.example.spoot_taxi_front.dto.UserDto;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * authController에 noreply 재학생 인증 api 추가 필요
 * authController에 이미 존재하는 아이디인지 확인하는 api 추가 필요
 * entity 속성 정립 해야 함 -> 근데 하면서 하게 될 듯
 */
public class JoinActivity extends AppCompatActivity {



    private ActivityJoinBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);


        // 회원가입 버튼 클릭
        binding.joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                InputChecker ic = new InputChecker();

                //빈칸없이 모두 채웠는지 확인
                boolean isEmailFilled = ic.checkEmailFilled(binding.inputEmail, binding.emailTv);
                boolean isPasswordFilled = ic.checkPasswordFilled(binding.inputPassword, binding.pwTv);
                boolean isPasswordConfirmFilled = ic.checkPasswordConfirmFilled(binding.inputPwck, binding.pwckTv);
                boolean isNicknameFilled = ic.checkNicknameFilled(binding.inputNickname, binding.nicknameTv);
                boolean isGenderFilled = ic.checkGenderFilled(binding.inputGender, binding.genderTv);


                if (!(isEmailFilled && isPasswordFilled && isPasswordConfirmFilled && isNicknameFilled && isGenderFilled)) {
                    return;
                }

                //모두 빈칸 없이 채웠다면 다음을 추가로 체크
                boolean isSamePassword = ic.checkSamePassword(binding.inputPassword, binding.inputPwck, binding.pwckTv);
                boolean isValidPassword = ic.checkPasswordValidity(binding.inputPassword, binding.inputEmail, binding.pwTv);
                boolean isCorrectEmailFormat = ic.checkEmailFormat(binding.inputEmail, binding.emailTv);
                if (!(isSamePassword && isValidPassword && isCorrectEmailFormat)) {
                    return;
                }

                //모든 체크 사항 통과
                //UserDto 초기화
                String email = binding.inputEmail.getText().toString();
                String password = binding.inputPassword.getText().toString();
                String nickname = binding.inputNickname.getText().toString();
                Gender gen;

                RadioGroup radioGroup = binding.inputGender;
                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(selectedId);
                String gender = radioButton.getText().toString();

                if (gender.equals("여성")) {
                    gen = Gender.FEMALE;
                } else if (gender.equals("남성")) {
                    gen = Gender.MALE;
                } else {
                    gen = Gender.ETC;
                }


                //재학생 인증 activity에 userDto넘기면서 인텐트 전환
                UserDto userDto = new UserDto(email, password, nickname, gen);
                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra("userDto", userDto);
                startActivity(intent);


            }
        });

        //취소 버튼 클릭
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //로그인 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}