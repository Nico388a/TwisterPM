package com.example.twisterpm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

public interface TwisterService {
    //Message
    @GET("api/messages")
    Call<List<Message>> getMessages();
    @POST("api/messages")
    Call<Message> postMessage(@Body Message message);
    @DELETE("api/messages/{Id}")
    Call <Message> deleteMessage(@Path("Id") int messageId);

    //Comment
    @GET("api/messages/{messageId}/comments")
    Call<List<Comment>> getComments(@Path("messageId") int messageId);
    @POST("api/messages/{messageId}/comments")
    Call<Comment> postComment(@Path("messageId")int messagId, @Body Comment comment);
    @DELETE("api/messages/{messageId}/comments/{commentId}")
    Call <Comment> deleteComment(@Path("messageId") int messageId, @Path("commentId") int commentId);

}
