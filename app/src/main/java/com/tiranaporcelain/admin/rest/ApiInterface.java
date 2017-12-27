package com.tiranaporcelain.admin.rest;

import com.tiranaporcelain.admin.models.LoginModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mphj on 10/15/2017.
 */

public interface ApiInterface {
    @GET("login")
    Call<LoginModel> login(@Query("imei") String imei,
                           @Query("username") String username,
                           @Query("password") String password);
}