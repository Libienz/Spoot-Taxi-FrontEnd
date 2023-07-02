package com.example.spoot_taxi_front.activities;



import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;

import com.example.spoot_taxi_front.dto.User;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityJoinBinding;
import com.example.spoot_taxi_front.dto.Gender;
import com.example.spoot_taxi_front.dto.UserDto;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * authController에 noreply 재학생 인증 api 추가 필요
 * authController에 이미 존재하는 아이디인지 확인하는 api 추가 필요
 * entity 속성 정립 해야 함 -> 근데 하면서 하게 될 듯
 */
public class JoinActivity extends AppCompatActivity {


    private ActivityJoinBinding binding;
    private static final int ALBUM_PERMISSION_REQUEST_CODE = 1; // 앨범 접근 권한 요청 코드
    private static final int ALBUM_REQUEST_CODE = 2; // 앨범 액티비티 호출 요청 코드


    private String email;
    private String password;
    private String nickname;
    private String imageUri;
    private Gender gen;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);

        // ActivityResultLauncher 초기화
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            // 선택한 이미지 URI 처리
                            imageUri = result.toString();
                            // 선택한 이미지 처리
                            // ...
                        }
                    }
                });
        //프로필 이미지 클릭
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 앨범 접근 권한 확인
                if (ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(JoinActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ALBUM_PERMISSION_REQUEST_CODE);
                } else {
                    // 이미 권한이 있는 경우 앨범 액티비티 호출
                    galleryLauncher.launch("image/*");
                }
            }
        });



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
                //User 초기화
                email = binding.inputEmail.getText().toString();
                password = binding.inputPassword.getText().toString();
                nickname = binding.inputNickname.getText().toString();

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


                //재학생 인증 activity에 user넘기면서 인텐트 전환
                User user = new User(email, password, nickname, gen);
                Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
                intent.putExtra("user", user);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            // 앨범 액티비티로부터 이미지 선택 결과를 받아온 경우
            if (data != null) {
                Uri selectedImageUri = data.getData();
                // 선택한 이미지를 처리하는 로직
                imageUri = selectedImageUri.toString();
                binding.profileImageView.setImageURI(selectedImageUri);
            }
        }
    }
}