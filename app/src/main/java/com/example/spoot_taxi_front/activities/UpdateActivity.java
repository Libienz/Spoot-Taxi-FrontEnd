package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityUpdateBinding;
import com.example.spoot_taxi_front.models.Gender;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.utils.SessionManager;

public class UpdateActivity extends AppCompatActivity {

    private ActivityUpdateBinding binding;
    private static final int ALBUM_PERMISSION_REQUEST_CODE = 1; // 앨범 접근 권한 요청 코드
    private static final int ALBUM_REQUEST_CODE = 2; // 앨범 액티비티 호출 요청 코드


    private String email;
    private String password;
    private String nickname;
    private String imgUrl;
    private Gender gen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update);

        //현재 유저정보로 Edit Text 채워놓기
        User currentUser = SessionManager.getInstance().getCurrentUser();

        binding.inputEmail.setText(currentUser.getEmail());
        //임시 주석: img 처리 완료하고 수정할 것
//        binding.profileImageView.setImageURI(Uri.parse(currentUser.getImgUrl()));
        binding.inputPassword.setText(currentUser.getPassword());
        binding.inputPwck.setText(currentUser.getPassword());
        binding.inputNickname.setText(currentUser.getNickname());
        RadioGroup inputGender = binding.inputGender;
        Gender gender = currentUser.getGender();
        if (gender == Gender.FEMALE) {
            inputGender.check(R.id.gender_female);
        } else if (gender == Gender.MALE) {
            inputGender.check(R.id.gender_male);
        } else if (gender == Gender.ETC) {
            inputGender.check(R.id.gender_etc);
        }

        //프로필 이미지 클릭
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 앨범 접근 권한 확인
                if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ALBUM_PERMISSION_REQUEST_CODE);
                } else {
                    // 이미 권한이 있는 경우 앨범 액티비티 호출
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                }
            }
        });


        // 수정하기 버튼 클릭
        binding.updateButton.setOnClickListener(new View.OnClickListener() {

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
                //User 초기화 및 업데이트 요청
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


                //유저 초기화
                User user = new User(email, password, nickname, imgUrl, gen);
                //update user api


            }
        });

        //취소 버튼 클릭
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                imgUrl = selectedImageUri.toString();
                binding.profileImageView.setImageURI(selectedImageUri);
            }
        }
    }

}