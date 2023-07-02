package com.example.spoot_taxi_front.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spoot_taxi_front.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.CurrentLocationEventListener;
import net.daum.mf.map.api.MapView.MapViewEventListener;

public class MatchingFragment extends Fragment implements CurrentLocationEventListener, MapViewEventListener {

    private MapView mapView;
    private ViewGroup mapViewContainer;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;

    private Button matchingButton;

    private double curLongitude;
    private double curLaitude;


    public MatchingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 카카오 맵 API 초기화
        mapView = new MapView(requireContext());
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matching, container, false);

        mapViewContainer = view.findViewById(R.id.mapViewContainer);
        mapViewContainer.addView(mapView);


        matchingButton = view.findViewById(R.id.matchingButton);

        //매칭 버튼 클릭
        matchingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 버튼 클릭 이벤트 처리
                if (curLaitude != 0.0 && curLongitude != 0.0) {
                    // 매칭 버튼 클릭 시 팝업 표시
                    showMatchingProgressPopup();

                    //서버에 매칭 요성 by api

                } else {
                    Toast.makeText(requireContext(), "위치를 계산중입니다. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }



    private void showMatchingProgressPopup() {
        // 팝업 레이아웃 인플레이션
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.dialog_matching_progress, null);

        // 팝업 생성 및 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(popupView);
        builder.setCancelable(false); // 팝업 외부 클릭 시 닫히지 않도록 설정

        // 팝업 내부의 뷰 요소 가져오기
        ImageView gifImageView = popupView.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.raw.progress).into(gifImageView);

        Button cancelButton = popupView.findViewById(R.id.cancelButton);

        // 팝업 표시
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        // 매칭 취소 버튼 클릭 시 팝업 닫기
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMatchingSuccessPopup();
                alertDialog.dismiss();
                //수정지점: 취소누르면 매칭 성공 액티비티 띄우기

            }
        });
    }



    private void showMatchingSuccessPopup() {
        // 팝업 레이아웃 인플레이션
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.dialog_matching_success, null);

        // 팝업 생성 및 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(popupView);
        builder.setCancelable(false); // 팝업 외부 클릭 시 닫히지 않도록 설정

        // 팝업 내부의 뷰 요소 가져오기
        Button toChatRoom = popupView.findViewById(R.id.toChatRoomButton);
        ImageView gifImageView = popupView.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(R.raw.hello).into(gifImageView);

        // 팝업 표시
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        // 채팅방으로 이동 버튼 클릭 시 채팅방 목록 Fragment로 이동
        toChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ChatFragment로 이동: 클릭 발생시키기
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.navigationView);
                MenuItem chatItem = bottomNavigationView.getMenu().findItem(R.id.chatFragment);
                chatItem.setChecked(true); // 아이템을 선택한 상태로 설정
                bottomNavigationView.setSelectedItemId(chatItem.getItemId()); // 클릭 이벤트 발생
                alertDialog.dismiss();
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        // 현위치 권한 체크 및 권한 요청
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // 권한이 있는 경우, 현위치 가져오기 시작
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 현위치 추적 정지
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracy) {
        // 현위치가 업데이트될 때 호출되는 메소드
        // 현위치 근방을 보여주도록 지도 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(currentLocation));

        // 현재 위치 정보 업데이트
        curLaitude = currentLocation.getMapPointGeoCoord().latitude;
        curLongitude = currentLocation.getMapPointGeoCoord().longitude;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 프래그먼트가 제거될 때 맵뷰를 제거해야 함
        if (mapViewContainer != null) {
            mapViewContainer.removeAllViews();
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    // 다른 메소드들

    @Override
    public void onMapViewInitialized(MapView mapView) {
        // 지도 초기화가 완료되었을 때 호출되는 메소드
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    // 다른 메소드들




}
