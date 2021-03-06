package com.agungadi.myapplication.Helper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JalanInterface {
    String JALANIURL = "http://android.adicloud.my.id/";
    @FormUrlEncoded
    @POST("jalan.php")
    Call<String> getJALAN(
            @Field("id") String id,
            @Field("image") String image,
            @Field("deskripsi") String deskripsi,
            @Field("alamat") String alamat,
            @Field("kecamatan") String kecamatan,
            @Field("latitude") String latitude,
            @Field("longtitude") String longtitude
    );

}
