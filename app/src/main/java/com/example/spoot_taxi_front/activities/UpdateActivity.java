package com.example.spoot_taxi_front.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityUpdateBinding;
import com.example.spoot_taxi_front.models.Gender;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.responses.JoinResponse;
import com.example.spoot_taxi_front.network.dto.responses.UploadImageResponse;
import com.example.spoot_taxi_front.network.dto.responses.UserSaveResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private ActivityUpdateBinding binding;
    private static final int ALBUM_PERMISSION_REQUEST_CODE = 1; // 앨범 접근 권한 요청 코드
    private static final int ALBUM_REQUEST_CODE = 2; // 앨범 액티비티 호출 요청 코드


    private String email;
    private String password;
    private String nickname;
    private String imgUrl;

    //멀티파트 form으로 전송할 image file
    File imageFile;

    private Gender gen;
    private ActivityResultLauncher<String> galleryLauncher;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update);

        authApi = ApiManager.getInstance().createAuthApi(SessionManager.getInstance().getJwtToken());
        //현재 유저정보로 Edit Text 채워놓기
        User currentUser = SessionManager.getInstance().getCurrentUser();

        binding.inputEmail.setText(currentUser.getEmail());
//        binding.profileImageView.setImageURI(Uri.parse(currentUser.getImgUrl()));
//        binding.profileImageView.setImageURI(Uri.parse("http://192.168.219.110:8080/api/images/profile-image/file_20230731_195748_7305.jpg"));
        Glide.with(this).load("http://192.168.219.110:8080/api/images/profile-image/file_20230731_195748_7305.jpg").into(binding.profileImageView);
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

        // ActivityResultLauncher 초기화
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            // 선택한 이미지 레이아웃에 배치
                            binding.profileImageView.setImageURI(result);
                            // 선택한 이미지를 File 객체로 변환
                            imageFile = new File(getFilePathFromUri(result));

                        }
                    }
                });

        //프로필 이미지 클릭
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sdk 33이상
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 없는 경우 권한 요청
                        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, ALBUM_PERMISSION_REQUEST_CODE);

                    } else {
                        // 이미 권한이 있는 경우 앨범 액티비티 호출
                        galleryLauncher.launch("image/*");
                    }
                }
                //sdk 32 이하
                else {
                    if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 없는 경우 권한 요청
                        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ALBUM_PERMISSION_REQUEST_CODE);

                    } else {
                        // 이미 권한이 있는 경우 앨범 액티비티 호출
                        galleryLauncher.launch("image/*");
                    }
                }
            }
        });


        // 수정하기 버튼 클릭
        binding.updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!chcekUpdateInputs()) return;

//                //모든 체크 사항 통과
//                //User 초기화 및 업데이트 요청
//                email = binding.inputEmail.getText().toString();
//                password = binding.inputPassword.getText().toString();
//                nickname = binding.inputNickname.getText().toString();
//                RadioGroup radioGroup = binding.inputGender;
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//                RadioButton radioButton = findViewById(selectedId);
//                String gender = radioButton.getText().toString();
//
//                if (gender.equals("여성")) {
//                    gen = Gender.FEMALE;
//                } else if (gender.equals("남성")) {
//                    gen = Gender.MALE;
//                } else {
//                    gen = Gender.ETC;
//                }

                updateProcess();
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



    private boolean chcekUpdateInputs() {
        InputChecker ic = new InputChecker();

        //빈칸없이 모두 채웠는지 확인
        boolean isEmailFilled = ic.checkEmailFilled(binding.inputEmail, binding.emailTv);
        boolean isPasswordFilled = ic.checkPasswordFilled(binding.inputPassword, binding.pwTv);
        boolean isPasswordConfirmFilled = ic.checkPasswordConfirmFilled(binding.inputPwck, binding.pwckTv);
        boolean isNicknameFilled = ic.checkNicknameFilled(binding.inputNickname, binding.nicknameTv);
        boolean isGenderFilled = ic.checkGenderFilled(binding.inputGender, binding.genderTv);


        if (!(isEmailFilled && isPasswordFilled && isPasswordConfirmFilled && isNicknameFilled && isGenderFilled)) {
            return false;
        }

        //모두 빈칸 없이 채웠다면 다음을 추가로 체크
        boolean isSamePassword = ic.checkSamePassword(binding.inputPassword, binding.inputPwck, binding.pwckTv);
        boolean isValidPassword = ic.checkPasswordValidity(binding.inputPassword, binding.inputEmail, binding.pwTv);
        boolean isCorrectEmailFormat = ic.checkEmailFormat(binding.inputEmail, binding.emailTv);
        if (!(isSamePassword && isValidPassword && isCorrectEmailFormat)) {
            return false;
        }
        return true;
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


    //uri -> filePath
    //api 레벨 29이상에서는 이렇게 경로를 받아와야 함
    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    private void updateProcess() {

        //프로필 이미지를 선택했다면 프로필 이미지를 서버에 업로드
        //imgUrl을 받아온 후 imgurl 정보를 포함시켜 회원가입을 요청한다.
        if (imageFile != null) {

            //가입에 요청할 유저 정보를 EditText에서 받아옴
            UserDto userDto = createUserDtoFromEditText();

            //프로필 이미지를 선택했기에 서버에 업로드하고 url을 받아논다.
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imagePart", imageFile.getName(), requestBody);
            Call<UploadImageResponse> uploadImageCall = authApi.uploadImage(imagePart);
            uploadImageCall.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                    //imgUrl을 가입할 회원정보에 넣고 회원가입 요청한다.
                    imgUrl = response.body().getImageUrl();
                    userDto.setImgUrl(imgUrl);
                    //받아온 imgUrl정보까지 유저에 말아서 회원가입 api 호출!
                    callUpdateApi(userDto);
                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "프로필 이미지 업로드에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                }
            });

        }

        //프로필 이미지를 선택하지 않았다면 default Profile Image의 url로 유저의 프로필 이미지 url을 설정한다.
        else {
            UserDto userDto = createUserDtoFromEditText();
            callUpdateApi(userDto);
        }
    }

    private void callUpdateApi(UserDto userDto) {
        //회원가입 요청
        Call<UserSaveResponse> updateCall = authApi.updateUser(userDto.getEmail(), userDto);
        updateCall.enqueue(new Callback<UserSaveResponse>() {
            @Override
            public void onResponse(Call<UserSaveResponse> call, Response<UserSaveResponse> response) {
                handleUpdateResponse(response.code(), response.body());
            }

            @Override
            public void onFailure(Call<UserSaveResponse> call, Throwable t) {

            }
        });

    }
    private void handleUpdateResponse(int statusCode, UserSaveResponse responseBody) {
        switch (statusCode) {
            case 200:
                UserDto userDto = responseBody.getUserDto();
                //UserDto -> User
                User user = userDtoToUser(userDto);
                //setCurrent user
                SessionManager.getInstance().setCurrentUser(user);
                //setCurrent user token

                Toast.makeText(getApplicationContext(), "유저 정보 수정 성공!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(), "유저 정보 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private UserDto createUserDtoFromEditText() {
        //UserDto생성
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
        UserDto userDto = new UserDto(email, password, nickname, gen);
        return userDto;
    }
    private User userDtoToUser(UserDto userDto) {

        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Gender gender = userDto.getGender();
        String imgUrl = userDto.getImgUrl();
        String name = userDto.getName();

        return new User(email, password, name, imgUrl, gender);
    }
}