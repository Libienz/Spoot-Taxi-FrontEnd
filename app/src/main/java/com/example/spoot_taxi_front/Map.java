package com.example.spoot_taxi_front;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;


public class Map extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MapView mapView = new MapView(requireContext());
        ViewGroup mapViewContainer = view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        return view;
    }
}