package com.example.spoot_taxi_front.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spoot_taxi_front.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RallyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RallyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RallyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RallyFragment newInstance(String param1, String param2) {
        RallyFragment fragment = new RallyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRallyInformationImg();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rally, container, false);
    }


    private void setRallyInformationImg() {
        //집회 정보 이미지 요청 api 부분
        // 이미지를 받아올 API 요청
/*
        String imageUrl = "http://example.com/api/image"; // 이미지를 반환하는 API의 URL
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // API 요청이 성공한 경우 이미지 뷰에 이미지 적용
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // API 요청이 실패한 경우 에러 처리
                        Toast.makeText(getApplicationContext(), "이미지 로딩 실패", Toast.LENGTH_SHORT).show();
                    }
                });

// API 요청을 큐에 추가
        requestQueue.add(imageRequest);



 */

    }

}