package com.tiranaporcelain.admin.activity;

import android.os.Bundle;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.AlertView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertActivity extends BaseActivity implements AlertView {

    public static final int REJECT = 1, SUBMIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.submit)
    void onSubmit() {
        closeAlert(SUBMIT);
    }


    @OnClick(R.id.reject)
    void reject() {
        closeAlert(REJECT);
    }


    @Override
    public void closeAlert(int type) {
        setResult(type);
        finish();
    }
}
