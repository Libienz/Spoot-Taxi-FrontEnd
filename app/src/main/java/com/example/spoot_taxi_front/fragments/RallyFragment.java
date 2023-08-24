package com.example.spoot_taxi_front.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.activities.LoginActivity;
import com.example.spoot_taxi_front.adapters.RallyAdapter;
import com.example.spoot_taxi_front.network.api.RallyApi;
import com.example.spoot_taxi_front.network.dto.RallyInformationDto;
import com.example.spoot_taxi_front.network.dto.responses.RallyResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RallyFragment extends Fragment {

    private RecyclerView rallyRecyclerView;
    private RallyAdapter rallyAdapter;
    private List<RallyInformationDto.RallyDetailsDto> rallyDetailsDtoList = new ArrayList<>();
    RallyApi rallyApi;
    private TextView rallyDateTextView;
    private TextView commentTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rally, container, false);
        rallyApi = ApiManager.getInstance().createRallyApi(SessionManager.getInstance().getJwtToken());
        // 뷰 요소 초기화
        commentTextView = view.findViewById(R.id.textViewComment);
        rallyRecyclerView = view.findViewById(R.id.rallyRecyclerView);
        rallyDateTextView = view.findViewById(R.id.rallyDateTextView);
        rallyDateTextView.setText("\u2B50 " + LocalDateTime.now().getMonthValue() + "월 " + LocalDateTime.now().getDayOfMonth() + "일 주요집회" + " \u2B50");
        rallyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rallyAdapter = new RallyAdapter();
        getRallyInformationFromServer();
        rallyAdapter.setRallyDetails(rallyDetailsDtoList);
        rallyRecyclerView.setAdapter(rallyAdapter);
        return view;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("chatFragment", "onHiddenChanged: ");
        if (hidden == false) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("chatFragment", "ui update re");
                    getRallyInformationFromServer();
                }
            });

        }
    }

    private void getRallyInformationFromServer() {

        Call<RallyResponse> call = rallyApi.getRallyInfo();

        call.enqueue(new Callback<RallyResponse>() {
            @Override
            public void onResponse(Call<RallyResponse> call, Response<RallyResponse> response) {
                handleRallyResponse(response.code(), response.body());
            }

            @Override
            public void onFailure(Call<RallyResponse> call, Throwable t) {
                Toast.makeText(getContext(), "집회정보 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "API 호출에 실패하였습니다.", t);
            }
        });



    }
    private void handleRallyResponse(int statusCode, RallyResponse responseBody) {
        switch (statusCode) {
            case 200:
                RallyInformationDto rallyInformationDto = responseBody.getRallyInformationDto();
                Log.d("rallyFragment", "comment: " + rallyInformationDto.getComment());
                commentTextView.setText(rallyInformationDto.getComment());
                rallyDetailsDtoList = rallyInformationDto.getRallyDetailsDtoList();
                rallyAdapter.setRallyDetails(rallyDetailsDtoList);
                break;
            case 403:
                Toast.makeText(getActivity(), "서비스 이용을 위해 재로그인 해주세요", Toast.LENGTH_SHORT).show();
                Intent reAuthneticateIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(reAuthneticateIntent);
            default:
                Toast.makeText(getContext(), "집회정보를 받아올수 없습니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}