package com.example.spoot_taxi_front.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.models.ChatRoom;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.activities.ChatRoomActivity;
import com.example.spoot_taxi_front.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatViewHolder> {

    private List<ChatRoom> chatRooms; // 채팅방 목록 데이터


    public ChatRoomAdapter() {
        this.chatRooms = new ArrayList<>();
    }
    // 채팅방 목록 데이터 갱신 겸 set
    public void setChatRoomAdapter(List<ChatRoom> chatRooms) {
        Collections.sort(chatRooms, new Comparator<ChatRoom>() {
            @Override
            public int compare(ChatRoom chatRoom1, ChatRoom chatRoom2) {
                // getLastSentTime()을 사용하여 LocalDateTime을 비교
                return chatRoom2.getLastSentTime().compareTo(chatRoom1.getLastSentTime());
            }
        });
        this.chatRooms = chatRooms;
        notifyDataSetChanged(); //왜 갱신이 바로바로 안될까...특히 처음올때 화면과 채팅방에서 back해서 돌아올때 바로바로 왜 안바뀌는거지;;
    }

    // 아이템 뷰를 생성하고 뷰홀더를 반환하는 메서드
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 뷰를 생성하여 뷰홀더에 바인딩
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatViewHolder(itemView);
    }

    // 아이템 뷰의 데이터를 설정하는 메서드
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        // 해당 위치의 채팅방 데이터를 가져와서 뷰에 설정
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);

        // 각 아이템 뷰에 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이템 클릭 시 동작할 코드 작성
                // 채팅방으로 이동하면서 채팅방id도 같이 보낸다.
                Intent intent = new Intent(holder.itemView.getContext(), ChatRoomActivity.class);
                intent.putExtra("chatRoomId", chatRoom.getRoomId());
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }


    // 데이터 목록의 크기를 반환하는 메서드
    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    // 아이템 뷰의 뷰홀더 클래스
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        // 아이템 뷰의 뷰 요소 선언
        private TextView roomNameTextView;
        private TextView lastMessageTextView;
        private TextView timeTextView;
        private TextView nonReadCountTextView;
        private ImageView profileImageView1;
        private ImageView profileImageView2;
        private ImageView profileImageView3;
        private ImageView profileImageView4;
        private List<ImageView> profileImageViews;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            // 아이템 뷰의 뷰 요소 초기화
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            nonReadCountTextView = itemView.findViewById(R.id.nonReadCountTextView);
            profileImageView1 = itemView.findViewById(R.id.profileImageView1);
            profileImageView2 = itemView.findViewById(R.id.profileImageView2);
            profileImageView3 = itemView.findViewById(R.id.profileImageView3);
            profileImageView4 = itemView.findViewById(R.id.profileImageView4);
            profileImageViews = new ArrayList<>();
            profileImageViews.add(profileImageView1);
            profileImageViews.add(profileImageView2);
            profileImageViews.add(profileImageView3);
            profileImageViews.add(profileImageView4);

        }

        // 아이템 뷰에 데이터를 설정하는 메서드
        public void bind(ChatRoom chatRoom) {
            // 아이템 뷰에 데이터 설정
            roomNameTextView.setText(chatRoom.getRoomName());
            lastMessageTextView.setText(chatRoom.getLastMessage());
            timeTextView.setText(chatRoom.getLastSentTime());
            if (chatRoom.getNonReadMessageCount() > 0) {
                nonReadCountTextView.setVisibility(View.VISIBLE);
                nonReadCountTextView.setText(chatRoom.getNonReadMessageCount()+"");
            } else {
                nonReadCountTextView.setVisibility(View.INVISIBLE);
            }
            List<User> participants = chatRoom.getParticipants();
            for (int i = 0; i < participants.size(); i++) {
                User cur = participants.get(i);
                String imgUrl = cur.getImgUrl();
                Log.d("imgUrlCheck", "bind: " + imgUrl);
                if (imgUrl != null) {
                    Glide.with(itemView.getContext()).load(imgUrl).into(profileImageViews.get(i));
                }
            }

        }
    }
}

