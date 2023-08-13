package com.example.spoot_taxi_front.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.activities.LoginActivity;
import com.example.spoot_taxi_front.activities.UpdateActivity;
import com.example.spoot_taxi_front.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    //레이아웃 요소들
    View logoutButton;
    View userUpdateButton;
    TextView sessionEmailTextView;
    ImageView profileImageView;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        userUpdateButton = view.findViewById(R.id.userUpdateButton);
        logoutButton = view.findViewById(R.id.logOutButton);
        sessionEmailTextView = view.findViewById(R.id.sessionEmailTextView);
        profileImageView = view.findViewById(R.id.profileImageView);

        //뷰요소 렌더링
        Glide.with(this).load(SessionManager.getInstance().getCurrentUser().getImgUrl()).into(profileImageView);
        sessionEmailTextView.setText(SessionManager.getInstance().getCurrentUser().getEmail());
        //정보 수정 버튼 클릭
        userUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), UpdateActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃 버튼
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmPopup();
            }
        });
        return view;
    }

    private void showLogoutConfirmPopup() {
        // 팝업 레이아웃 인플레이션
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.dialog_confirm_logout, null);

        // 팝업 생성 및 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(popupView);
        builder.setCancelable(false); // 팝업 외부 클릭 시 닫히지 않도록 설정

        // 팝업 내부의 뷰 요소 가져오기
        Button logoutYesButton = popupView.findViewById(R.id.logoutYesButton);
        Button logoutNoButton = popupView.findViewById(R.id.logoutNoButton);

        // 팝업 표시
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        //정말 로그아웃 하시겠습니까? -> 예 버튼 클릭
        logoutYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance().logout(); //세션 종료
                alertDialog.dismiss();

                // 로그인 액티비티로 이동
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);

                //현재 Fragment를 호스팅하는 Activity 종료
                requireActivity().finish();


            }
        });

        //정말 로그아웃 하시겠습니까? -> 아니오 버튼 클릭
        logoutNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}