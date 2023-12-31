package com.example.spoot_taxi_front.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.utils.ChatRoomDataChange;
import com.example.spoot_taxi_front.utils.LocalChatRoomManager;
import com.example.spoot_taxi_front.adapters.ChatRoomAdapter;
import com.example.spoot_taxi_front.network.socket.WebSocketViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private WebSocketViewModel webSocketViewModel;
    private LocalChatRoomManager localChatRoomManager;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // 뷰 요소 초기화
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRoomAdapter = new ChatRoomAdapter();
        chatRecyclerView.setAdapter(chatRoomAdapter);

        //웹소켓 요소 초기화
        Log.d("ChatFragment", "onCreateView: ");
        webSocketViewModel = WebSocketViewModel.getInstance();
        // WebSocket 연결
        webSocketViewModel.connectWebSocket();

        localChatRoomManager = localChatRoomManager.getInstance();
        chatRoomAdapter.setChatRoomList(localChatRoomManager.getChatRooms());
        return view;
    }

    public void onResume() {
        super.onResume();
        Log.d("chatFragment", "onResume: ");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("chatFragment", "ui update onResume");
                chatRoomAdapter.setChatRoomList(localChatRoomManager.getChatRooms());;
            }
        });
        Log.d("ChatFragment","onResume실행");
        View view = getView();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatRoomDataChange(ChatRoomDataChange event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("ChatFragment", "run: " + localChatRoomManager.getChatRooms().size());
                chatRoomAdapter.setChatRoomList(localChatRoomManager.getChatRooms());
            }
        });
    }


}