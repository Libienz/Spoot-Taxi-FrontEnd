package com.example.spoot_taxi_front.activities;



import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;

import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.responses.JoinResponse;
import com.example.spoot_taxi_front.network.dto.responses.UploadImageResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.InputChecker;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.databinding.ActivityJoinBinding;
import com.example.spoot_taxi_front.models.Gender;
import com.example.spoot_taxi_front.utils.SessionManager;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * authController에 noreply 재학생 인증 api 추가 필요
 * authController에 이미 존재하는 아이디인지 확인하는 api 추가 필요
 * entity 속성 정립 해야 함 -> 근데 하면서 하게 될 듯
 */
public class JoinActivity extends AppCompatActivity {


    private ActivityJoinBinding binding;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int ALBUM_PERMISSION_REQUEST_CODE = 1; // 앨범 접근 권한 요청 코드
    private static final int ALBUM_REQUEST_CODE = 2; // 앨범 액티비티 호출 요청 코드


    //회원가입에 기입할 정보들
    private String email;
    private String password;
    private String nickname;
    private String imgUrl;
    private Gender gen;

    //멀티파트 form으로 전송할 image file
    File imageFile;

    //갤러리 실행을 위한 런쳐
    private ActivityResultLauncher<String> galleryLauncher;

    //회원가입에 성공한 User
    private User user;

    //api caller
    AuthApi authApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        authApi = ApiManager.getInstance().getAuthApi();
        String email = getIntent().getStringExtra("email");
        binding.inputEmail.setText(email);

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
                    if (ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 없는 경우 권한 요청
                        ActivityCompat.requestPermissions(JoinActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, ALBUM_PERMISSION_REQUEST_CODE);

                    } else {
                        // 이미 권한이 있는 경우 앨범 액티비티 호출
                        galleryLauncher.launch("image/*");
                    }
                }
                //sdk 32 이하
                else {
                    if (ContextCompat.checkSelfPermission(JoinActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 없는 경우 권한 요청
                        ActivityCompat.requestPermissions(JoinActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ALBUM_PERMISSION_REQUEST_CODE);

                    } else {
                        // 이미 권한이 있는 경우 앨범 액티비티 호출
                        galleryLauncher.launch("image/*");
                    }
                }

            }
        });


        // 회원가입 버튼 클릭
        binding.joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 모든 입력란(Edit Text)에 대해 작성란이 비어있거나 format이 올바른지 확인
                if (checkJoinInputForms()) return;
                // 인풋 체크 완료. 회원가입 프로세스 진행
                joinProcess();

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

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALBUM_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 갤러리 액티비티 호출
                galleryLauncher.launch("image/*");
            } else {
                // 권한이 거부된 경우 처리할 로직 작성
            }
        }
    }
    private boolean checkJoinInputForms() {
        InputChecker ic = new InputChecker();


        // EmptyCheck: 빈칸없이 모두 채웠는지 확인
        boolean isEmailFilled = ic.checkEmailFilled(binding.inputEmail, binding.emailTv);
        boolean isPasswordFilled = ic.checkPasswordFilled(binding.inputPassword, binding.pwTv);
        boolean isPasswordConfirmFilled = ic.checkPasswordConfirmFilled(binding.inputPwck, binding.pwckTv);
        boolean isNicknameFilled = ic.checkNicknameFilled(binding.inputNickname, binding.nicknameTv);
        boolean isGenderFilled = ic.checkGenderFilled(binding.inputGender, binding.genderTv);


        if (!(isEmailFilled && isPasswordFilled && isPasswordConfirmFilled && isNicknameFilled && isGenderFilled)) {
            return true;
        }

        // MalformCheck: 모두 빈칸 없이 채웠다면 form 체크
        boolean isSamePassword = ic.checkSamePassword(binding.inputPassword, binding.inputPwck, binding.pwckTv);
        boolean isValidPassword = ic.checkPasswordValidity(binding.inputPassword, binding.inputEmail, binding.pwTv);
        boolean isCorrectEmailFormat = ic.checkEmailFormat(binding.inputEmail, binding.emailTv);
        if (!(isSamePassword && isValidPassword && isCorrectEmailFormat)) {
            return true;
        }
        return false;
    }

    private void joinProcess() {

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
                    callJoinApi(userDto);
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
            callJoinApi(userDto);
        }
    }

    private void callJoinApi(UserDto userDto) {
        //회원가입 요청
        Call<JoinResponse> joinCall = authApi.join(userDto);
        joinCall.enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                handleJoinResponse(response.code(), response.body());
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "회원가입 요청에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleJoinResponse(int statusCode, JoinResponse responseBody) {
        switch (statusCode) {
            case 200:
                String token = responseBody.getToken();
                UserDto userDto = responseBody.getUserDto();
                //UserDto -> User
                User user = userDtoToUser(userDto);
                //setCurrent user
                SessionManager.getInstance().setCurrentUser(user);
                //setCurrent user token
                SessionManager.getInstance().setJwtToken(token);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case 409: //실상 이메일 인증할 때 이메일 체크를 했기에 여기로 유저흐름이 올 가능성은 낮음
                binding.emailTv.setText("이미 가입되어 있는 메일입니다");
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



}