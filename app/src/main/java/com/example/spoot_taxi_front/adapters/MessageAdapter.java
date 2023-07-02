package com.example.spoot_taxi_front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spoot_taxi_front.dto.ChatMessage;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.dto.User;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<ChatMessage> chatMessages;

    public MessageAdapter() {
        this.chatMessages = new ArrayList<>();
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 뷰를 생성하여 뷰홀더에 바인딩
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // 해당 위치의 채팅 메시지 데이터를 가져와서 뷰에 설정
        ChatMessage chatMessage = chatMessages.get(position);
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        //아이템 레이아웃 요소들 선언

        //수신한 메시지인 경우 사용할 tv들
        private TextView textViewSenderName;
        private TextView textViewRecivedMessage;
        private TextView textViewRecivedTime;

        //보내는 메시지인 경우 사용할 tv들
        private TextView textViewMyMessage;
        private TextView textViewSentTime;
        private ImageView profileImgView;


        public void setLayoutGravity(int gravity) {
//            itemView.setGravity(gravity);
        }
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 아이템 뷰의 뷰 요소 초기화
            textViewSenderName = itemView.findViewById(R.id.textViewSenderName);
            textViewRecivedMessage = itemView.findViewById(R.id.textViewRecivedMessage);
            textViewRecivedTime = itemView.findViewById(R.id.textViewRecivedTime);
            textViewMyMessage = itemView.findViewById(R.id.textViewMyMessage);
            textViewSentTime = itemView.findViewById(R.id.textViewSentTime);
            profileImgView = itemView.findViewById(R.id.profileImageView);

        }

        public void bind(ChatMessage chatMessage) {
            // 채팅 메시지를 해당 뷰 요소에 설정
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (chatMessage.getSenderId().equals(currentUser.getEmail())) {
                // 내가 보낸 메시지인 경우, 왼쪽
                textViewMyMessage.setText(chatMessage.getMessage());
                textViewSentTime.setText(chatMessage.getSentTime()+"");

                //숨김
                textViewRecivedMessage.setVisibility(View.INVISIBLE);
                textViewSenderName.setVisibility(View.INVISIBLE);
                textViewRecivedTime.setVisibility(View.INVISIBLE);
                profileImgView.setVisibility(View.INVISIBLE);
            } else {
                // 상대방이 보낸 메시지인 경우, 오른쪽
                textViewSenderName.setText(chatMessage.getSenderName());
                textViewRecivedMessage.setText(chatMessage.getMessage());
                textViewRecivedTime.setText(chatMessage.getSentTime() +"");

                //숨김
                textViewMyMessage.setVisibility(View.INVISIBLE);
                textViewSentTime.setVisibility(View.INVISIBLE);

            }

        }
    }
}
