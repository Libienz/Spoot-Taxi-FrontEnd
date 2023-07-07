package com.example.spoot_taxi_front.network.dto.requests;

public class LoginRequest {
    private String email;
    private String password;

    // 생성자, 게터/세터 등 필요한 메서드 작성

    // 아이디와 비밀번호를 받아서 LoginRequest 객체를 생성하는 정적 메서드
    public static LoginRequest create(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
