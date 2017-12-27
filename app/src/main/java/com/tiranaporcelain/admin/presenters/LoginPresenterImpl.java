package com.tiranaporcelain.admin.presenters;

import com.tiranaporcelain.admin.interfaces.LoginView;
import com.tiranaporcelain.admin.models.LoginModel;
import com.tiranaporcelain.admin.models.rest.LoginRest;
import com.tiranaporcelain.admin.utils.Auth;

/**
 * Created by mphj on 10/15/2017.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginRest.LoginListener{

    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView){
        this.loginView = loginView;
    }

    @Override
    public void login(String username, String password) {
        loginView.showProgressBar();
        LoginRest.login(username, password, this);
    }

    @Override
    public void onSuccess(LoginModel loginModel) {
        Auth.login(loginModel);
        loginView.loginSucceed();
        loginView.hideProgressBar();
    }

    @Override
    public void onFailed(LoginModel loginModel) {
        if (loginModel.getStatus() == -1){
            loginView.networkFailed();
        } else if (loginModel.getStatus() == 403){
            loginView.badUsernameOrPassword();
        } else {
            loginView.unknownProblem(loginModel.getStatus());
        }
        loginView.hideProgressBar();
    }

    @Override
    public void onResume() {
        // Do nothing
    }

    @Override
    public void onDestroy() {
        // Do nothing!
    }
}
