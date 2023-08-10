package com.example.spoot_taxi_front.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.network.api.RallyApi;
import com.example.spoot_taxi_front.network.dto.RallyInformationDto;
import com.example.spoot_taxi_front.network.dto.responses.RallyResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RallyFragment extends Fragment {

    RallyApi rallyApi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rally, container, false);
        Button updateButton = view.findViewById(R.id.button_update);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("버튼눌림","눌렸음");
                setRallyInformation();
            }
        });

        return view;
    }


    private void setRallyInformation() {

        //Api Client 생성
        rallyApi = ApiManager.getInstance().createRallyApi(SessionManager.getInstance().getJwtToken());

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
                setRallyInfo(rallyInformationDto);

                break;
            default:
                Toast.makeText(getContext(), "집회정보를 받아올수 없습니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void setRallyInfo(RallyInformationDto responseBody) {
        TableLayout tableLayout =getView().findViewById(R.id.rallyTL);
        removeCurrentRally(tableLayout);

        TextView date = getView().findViewById(R.id.rallyDateTv);
        date.setText(responseBody.getDate().getMonthValue() + "월" + responseBody.getDate().getDayOfMonth() + "일");

        List<RallyInformationDto.RallyDetailsDto> rallyDetailsList = responseBody.getRallyDetailsList();
        for (RallyInformationDto.RallyDetailsDto details : rallyDetailsList) {
            String time =details.getStartTime().getHour()+"시"+details.getStartTime().getMinute()+"분"+" ~ "+details.getEndTime().getHour()+"시"+details.getEndTime().getMinute()+"분";
            addTableRow(tableLayout,time,details.getLocation(),details.getRallyScale(),details.getJurisdiction());

        }
    }

    private static void removeCurrentRally(TableLayout tableLayout) {
        int childCount = tableLayout.getChildCount();
        if(childCount>3){
            for(int i =childCount-1; i>=3; i--){
                tableLayout.removeViewAt(i);
            }
        }
    }

    // Fragment 내에서 동적으로 TableRow를 추가하는 메소드
    private void addTableRow(TableLayout tableLayout, String time, String location,String rallyAttendance,String policeStation) {
        TableRow tableRow = new TableRow(getContext());

        TextView textViewTime = createTextView(time);
        TextView textViewLocation = createTextView(location);
        TextView textViewRallyAttendance = createTextView(rallyAttendance);
        TextView textViewPoliceStation = createTextView(policeStation);

        // TableRow에 TextView 추가
        tableRow.addView(textViewTime);
        tableRow.addView(textViewLocation);
        tableRow.addView(textViewRallyAttendance);
        tableRow.addView(textViewPoliceStation);

        // TableLayout에 TableRow 추가
        tableLayout.addView(tableRow);
    }
    // TextView를 생성하고 스타일을 적용하는 메소드
    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1));
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10, 10, 10, 10);
        return textView;
    }
}