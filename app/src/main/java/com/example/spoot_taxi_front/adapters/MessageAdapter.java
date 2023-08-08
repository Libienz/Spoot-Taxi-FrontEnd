package com.example.spoot_taxi_front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 뷰 홀더 유형을 나타내는 상수
    private static final int TYPE_SYSTEM_MESSAGE = 0;
    private static final int TYPE_USER_MESSAGE = 1;
    private List<ChatMessage> chatMessages;

    public MessageAdapter() {
        this.chatMessages = new ArrayList<>();
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }
    public void addChatMessages(ChatMessage chatMessage) {
        int newPosition = chatMessages.size();
        this.chatMessages.add(chatMessage);
        notifyItemInserted(newPosition);
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // viewType 값에 따라서 시스템 메시지용 뷰 홀더와 일반 메시지용 뷰 홀더를 생성하도록 변경
        View itemView;
        if (viewType == TYPE_SYSTEM_MESSAGE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_message, parent, false);
            return new SystemMessageViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(itemView);
        }
    }
    //메세지의 필드중 isSystem이 null이면 유저, true면 시스템
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage.getSystem() != null && chatMessage.getSystem()) {
            return TYPE_SYSTEM_MESSAGE;
        } else {
            return TYPE_USER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 해당 위치의 채팅 메시지 데이터를 가져와서 뷰에 설정
        ChatMessage chatMessage = chatMessages.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(chatMessage);
        } else if (holder instanceof SystemMessageViewHolder) {
            ((SystemMessageViewHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // 새로운 내부 클래스로 SystemMessageViewHolder를 추가
    public class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView systemMessageTextView;

        public SystemMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            systemMessageTextView = itemView.findViewById(R.id.systemMessageTextView);
        }

        public void bind(ChatMessage message) {
            systemMessageTextView.setText(message.getMessage());
        }
    }
    public class UserMessageViewHolder extends RecyclerView.ViewHolder {
        //아이템 레이아웃 요소들 선언

        //수신한 메시지인 경우 사용할 tv들
        private TextView textViewSenderName;
        private TextView textViewRecivedMessage;
        private TextView textViewRecivedTime;

        //보내는 메시지인 경우 사용할 tv들
        private TextView textViewMyMessage;
        private TextView textViewSentTime;
        private CardView profileCardView;
        private ImageView profileImageView;



        public void setLayoutGravity(int gravity) {
//            itemView.setGravity(gravity);
        }
        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 아이템 뷰의 뷰 요소 초기화
            textViewSenderName = itemView.findViewById(R.id.textViewSenderName);
            textViewRecivedMessage = itemView.findViewById(R.id.textViewRecivedMessage);
            textViewRecivedTime = itemView.findViewById(R.id.textViewRecivedTime);
            textViewMyMessage = itemView.findViewById(R.id.textViewMyMessage);
            textViewSentTime = itemView.findViewById(R.id.textViewSentTime);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            profileCardView = itemView.findViewById(R.id.profileCardView);

        }


        public void bind(ChatMessage chatMessage) {
            // 채팅 메시지를 해당 뷰 요소에 설정
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (chatMessage.getSenderId().equals(currentUser.getEmail())) {
                // 내가 보낸 메시지인 경우, 왼쪽
                textViewMyMessage.setVisibility(View.VISIBLE);
                textViewSentTime.setVisibility(View.VISIBLE);

                textViewMyMessage.setText(chatMessage.getMessage());
                textViewSentTime.setText(chatMessage.getSentTime()+"");

                //숨김
                textViewRecivedMessage.setVisibility(View.INVISIBLE);
                textViewSenderName.setVisibility(View.INVISIBLE);
                textViewRecivedTime.setVisibility(View.INVISIBLE);
                profileImageView.setVisibility(View.INVISIBLE);
                profileCardView.setVisibility(View.INVISIBLE);

            } else {
                // 상대방이 보낸 메시지인 경우, 오른쪽
                textViewRecivedMessage.setVisibility(View.VISIBLE);
                textViewSenderName.setVisibility(View.VISIBLE);
                textViewRecivedTime.setVisibility(View.VISIBLE);
                profileImageView.setVisibility(View.VISIBLE);
                profileCardView.setVisibility(View.VISIBLE);

                textViewSenderName.setText(chatMessage.getSenderName());
                textViewRecivedMessage.setText(chatMessage.getMessage());
                textViewRecivedTime.setText(chatMessage.getSentTime() +"");

                Glide.with(itemView.getContext()).load(chatMessage.getSenderProfileImageUrl()).into(profileImageView);
                //숨김
                textViewMyMessage.setVisibility(View.INVISIBLE);
                textViewSentTime.setVisibility(View.INVISIBLE);
            }

        }
    }
}
