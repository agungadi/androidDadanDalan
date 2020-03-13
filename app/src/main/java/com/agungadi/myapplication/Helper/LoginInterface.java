package com.agungadi.myapplication.Helper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {


    String LOGINURL = "http://android.adicloud.my.id/";
    @FormUrlEncoded
    @POST("login.php")
    Call<String> getUserLogin(

            @Field("email") String email,
            @Field("password") String password
    );
}