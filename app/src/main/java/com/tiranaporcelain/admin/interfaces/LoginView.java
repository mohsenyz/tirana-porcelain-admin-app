package com.tiranaporcelain.admin.interfaces;

/**
 * Created by mphj on 10/15/2017.
 */

public interface LoginView {
    void showLoginContainer();
    void hideLoginContainer();
    void showForgetPasswordContainer();
    void hideForgetPasswordContainer();
    void showProgressBar();
    void hideProgressBar();
    void loginSucceed();
    void badUsernameOrPassword();
    void networkFailed();
    void unknownProblem(int status);
    void badEmail();
}
