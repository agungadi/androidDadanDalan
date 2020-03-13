package com.agungadi.myapplication.Helper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {

    String REGIURL = "http://android.adicloud.my.id/";
    @FormUrlEncoded
    @POST("register.php")
    Call<String> getUserRegi(
            @Field("name") String name,
            @Field("email") String email,
            @Field("noHP") String noHP,
            @Field("password") String password
    );

}