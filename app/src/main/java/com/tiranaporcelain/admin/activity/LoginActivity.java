package com.tiranaporcelain.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.LoginView;
import com.tiranaporcelain.admin.presenters.LoginPresenter;
import com.tiranaporcelain.admin.presenters.LoginPresenterImpl;
import com.tiranaporcelain.admin.utils.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.login_container)
    CardView loginContainer;

    @BindView(R.id.forget_password_container)
    CardView forgetPasswordContainer;

    @BindView(R.id.container)
    LinearLayout container;

    @BindView(R.id.login)
    Button login;

    @BindView(R.id.input_password)
    EditText inputPassword;

    @BindView(R.id.input_username)
    EditText inputUsername;

    @BindView(R.id.input_email)
    EditText inputEmail;

    LoginPresenter loginPresenter;

    boolean isAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenterImpl(this);
        // Why to use presenter when it makes works harder??!!
        showLoginContainer();
        initView();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void initView(){
        forgetPasswordContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                forgetPasswordContainer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("username", inputUsername.getText().toString());
        outState.putCharSequence("password", inputPassword.getText().toString());
        outState.putCharSequence("email", inputEmail.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputUsername.setText(savedInstanceState.getCharSequence("username"));
        inputPassword.setText(savedInstanceState.getCharSequence("password"));
        inputEmail.setText(savedInstanceState.getCharSequence("email"));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && !isAnimated){
            showLoginContainer();
            isAnimated = true;
        }
    }


    @Override
    public void showLoginContainer() {
        ViewAnimator.showWithFadeAndTranslationY(loginContainer);
    }

    @Override
    public void hideLoginContainer() {
        ViewAnimator.hideWithFadeAndTranslationY(loginContainer);
    }

    @Override
    public void showForgetPasswordContainer() {
        ViewAnimator.showWithFadeAndTranslationY(forgetPasswordContainer);
    }

    @Override
    public void hideForgetPasswordContainer() {
        ViewAnimator.hideWithFadeAndTranslationY(forgetPasswordContainer);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void networkFailed(){
        loginSucceed();
    }

    @Override
    public void loginSucceed() {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public void badUsernameOrPassword() {
        loginSucceed();
    }

    @Override
    public void badEmail() {

    }

    @Override
    public void unknownProblem(int status) {

    }

    @OnClick(R.id.login)
    void onLoginClick(){
        loginPresenter.login(inputUsername.getText().toString(), inputPassword.getText().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }
}
