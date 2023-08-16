package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.responses.ChatRoomMessageResponse;
import com.example.spoot_taxi_front.network.dto.responses.LeaveChatParticipantResponse;
import com.example.spoot_taxi_front.network.dto.responses.UpdateChatParticipantResponse;
import com.example.spoot_taxi_front.network.dto.responses.UserJoinedChatRoomResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatApi {
    //유저가 참가한 ChatRoom의 이름과 Id 반환 API
    @GET("/api/chatrooms")
    Call<UserJoinedChatRoomResponse> getUserChatRooms(@Query("email")String email);

    @GET("/api/chatrooms/{roomId}/messages")
    Call<ChatRoomMessageResponse> getChatRoomMessages(@Path ("roomId") Long chatRoomId, @Query("userEmail")String userEmail);

    @DELETE("/api/chatrooms/{roomId}/participants/{participantId}")
    Call<LeaveChatParticipantResponse> leaveChatParticipant(@Path("roomId")Long chatRoomId, @Path("participantId")Long participantId);

    @PUT("/api/chatrooms/{roomId}/participants/{participantId}/room-exit-time")
    Call<UpdateChatParticipantResponse> updateChatParticipant(@Path("roomId")Long chatRoomId, @Path("participantId")Long participantId);
}
