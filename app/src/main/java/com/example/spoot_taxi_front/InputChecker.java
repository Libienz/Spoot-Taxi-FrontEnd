package com.example.spoot_taxi_front;

import android.graphics.Color;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputChecker {

    /* 빈칸 체크 메서드들 */
    public boolean checkEmailFilled(EditText edt, TextView tv) {

        if(isEditTextEmpty(edt)) {
            tv.setText("이메일을 기입하세요");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        tv.setText("");
        tv.setTextColor(Color.parseColor("#008000"));
        return true;
    }

    public boolean checkPasswordFilled(EditText edt, TextView tv) {
        if(isEditTextEmpty(edt)) {
            tv.setText("비밀번호를 기입하세요");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        tv.setText("");
        tv.setTextColor(Color.parseColor("#008000"));
        return true;
    }

    public boolean checkPasswordConfirmFilled(EditText edt, TextView tv) {
        if(isEditTextEmpty(edt)) {
            tv.setText("비밀번호 확인을 기입하세요");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        tv.setText("");
        tv.setTextColor(Color.parseColor("#008000"));
        return true;
    }


    public boolean checkNicknameFilled(EditText edt, TextView tv) {
        if(isEditTextEmpty(edt)) {
            tv.setText("닉네임을 기입하세요");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
        else {
            tv.setText("");
            tv.setTextColor(Color.parseColor("#008000"));
            return true;
        }
    }

    public boolean checkGenderFilled(RadioGroup radioGroup, TextView tv) {
        int genderId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(genderId);
        if (radioButton == null) {
            tv.setText("성별을 기입하세요.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        } else {
            tv.setText("");
            tv.setTextColor(Color.parseColor("#008000"));
            return true;
        }
    }



    /* 추가 체크 메서드들 */

    //비밀번호와 비밀번호 확인란의 입력이 같은지 체크
    public boolean checkSamePassword(EditText passwordEditText, EditText passwordConfirmEditText, TextView tv) {
        String password = passwordEditText.getText().toString();
        String confirmPassword = passwordConfirmEditText.getText().toString();

        if (password.equals(confirmPassword)) {
            tv.setText("");
            tv.setTextColor(Color.parseColor("#008000"));
            return true;
        } else {
            tv.setText("비밀번호가 일치하지 않습니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }
    }

    public boolean checkEmailFormat(EditText emailEditText, TextView tv) {
        String email = emailEditText.getText().toString();

        // 이메일 형식 체크
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        if (!emailPattern.matcher(email).matches()) {
            tv.setText("올바른 이메일 형식이 아닙니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        // 도메인 체크
        String domain = "@sangmyung.kr";
        if (!email.endsWith(domain)) {
            tv.setText("재학생 인증을 위해 학교 이메일을 사용해야 합니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        // 모든 체크 통과
        tv.setText("");
        tv.setTextColor(Color.parseColor("#008000"));
        return true;
    }



    //정규식을 이용하여 비밀번호가 충분히 복잡한지 검증
    public boolean checkPasswordValidity(EditText passwordEditText, EditText emailEditText, TextView tv) {
        String password = passwordEditText.getText().toString();
        String userId = emailEditText.getText().toString();

        String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(password);

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);

        if (!userId.equals("") && password.contains(userId)) {
            tv.setText("비밀번호에 아이디가 포함될 수 없습니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        if (password.contains(" ")) {
            tv.setText("비밀번호는 공백문자를 포함할 수 없습니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        if (matcher2.find()) {
            tv.setText("비밀번호는 같은 문자를 4개 이상 사용할 수 없습니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        if (!matcher.matches()) {
            tv.setText("비밀번호는 8~16자리의 영문과 숫자와 특수문자의 조합이어야 합니다.");
            tv.setTextColor(Color.parseColor("#B30000"));
            return false;
        }

        tv.setText("");
        tv.setTextColor(Color.parseColor("#008000"));
        return true;
    }

    public boolean checkDuplicateEmail(EditText emailEditText, TextView tv) {
        String email = emailEditText.getText().toString();
        //중복 이메일이 존재하는지 api로 확인

        //중복이메일이 존재한다면

        //존재하지 않다면 분기로 나누어 적절히 tv 셋팅

        return true;
    }


    public boolean isEditTextEmpty(EditText edt) {
        if (edt.getText().toString().equals("")) {
            return true;
        }
        else {
            return false;
        }
    }





}
