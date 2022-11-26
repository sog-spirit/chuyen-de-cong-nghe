package com.example.blogandroid.apiservice;

import com.example.blogandroid.models.ChatListModel;
import com.example.blogandroid.models.CommentModel;
import com.example.blogandroid.models.MessageModel;
import com.example.blogandroid.models.NewChatListModel;
import com.example.blogandroid.models.NewChatResultModel;
import com.example.blogandroid.models.PostModel;
import com.example.blogandroid.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.12:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                    new OkHttpClient().newBuilder()
                            .cookieJar(new SessionCookieJar()).build()
            )
            .build()
            .create(APIService.class);

    class SessionCookieJar implements CookieJar {
        private List<Cookie> cookies;


        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url.encodedPath().endsWith("login")) {
                this.cookies = new ArrayList<>(cookies);
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (!url.encodedPath().endsWith("login") && cookies != null) {
                return cookies;
            }
            return Collections.emptyList();
        }
    }
    @GET("user/info")
    Call<UserModel> getUserId();

    @GET("users")
    Call<List<NewChatListModel>> getUsers();

    @POST("user/login")
    Call<Void> loginUser(@Body HashMap<String, String> userLoginData);

    @POST("user/register")
    Call<Void> registerUser(@Body HashMap<String, String> userRegisterData);

    @POST("user/logout")
    Call<Void> logoutUser();

    @GET("post")
    Call<List<PostModel>> getPosts();

    @POST("post")
    Call<Void> createPost(@Body HashMap<String, String> newPostData);

    @GET("posts")
    Call<List<PostModel>> getCurrentUserPosts();

    @POST("posts")
    Call<PostModel> getPostById(@Body HashMap<String, Integer> postIdData);

    @PATCH("posts")
    Call<Void> savePost(@Body HashMap<String, Object> editPostData);

    @HTTP(method = "DELETE", hasBody = true, path = "posts")
    Call<Void> deletePost(@Body HashMap<String, Integer> postIdData);

    @POST("comments")
    Call<List<CommentModel>> getCommentsFromPost(@Body HashMap<String, Integer> postIdData);

    @POST("comment")
    Call<Void> createCommentFromPost(@Body HashMap<String, Object> newCommentData);

    @GET("comment")
    Call<CommentModel> getCommentById(@Query("comment_id") int commentId);

    @PATCH("comment")
    Call<Void> editComment(@Body HashMap<String, Object> commentEditData);

    @HTTP(method = "DELETE", hasBody = true, path = "comment")
    Call<Void> deleteComment(@Body HashMap<String, Integer> commentId);

    @GET("chats")
    Call<List<ChatListModel>> getChats();

    @POST("chat")
    Call<NewChatResultModel> getChatId(@Body HashMap<String, Integer> userTwoId);

    @POST("chats")
    Call<NewChatResultModel> createChat(@Body HashMap<String, Integer> userTwoId);

    @POST("messages/get/newest")
    Call<MessageModel> getNewestMessage(@Body HashMap<String, Integer> userTwoId);
}
