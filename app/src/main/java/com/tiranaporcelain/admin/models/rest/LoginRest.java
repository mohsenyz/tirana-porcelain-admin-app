package com.tiranaporcelain.admin.models.rest;

import com.tiranaporcelain.admin.AccountryApplication;
import com.tiranaporcelain.admin.models.LoginModel;
import com.tiranaporcelain.admin.rest.ApiClient;
import com.tiranaporcelain.admin.rest.ApiInterface;
import com.tiranaporcelain.admin.utils.DeviceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mphj on 10/15/2017.
 */

public class LoginRest {
    public interface LoginListener{
        void onSuccess(LoginModel loginModel);
        void onFailed(LoginModel loginModel);
    }

    public static void login(String username, String password, final LoginListener loginListener){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.login(
                DeviceUtils.getUnique(AccountryApplication.context()),
                username,
                password)
                .enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if (response.body().isSucceed()){
                            loginListener.onSuccess(response.body());
                        } else {
                            loginListener.onFailed(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        loginListener.onFailed(new LoginModel());
                    }
                });
    }
}
