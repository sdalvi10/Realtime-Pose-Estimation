package com.example.holistictracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("camera")
    Call<String> sendCameraOutput(@Field("image") String encodedImage);
}