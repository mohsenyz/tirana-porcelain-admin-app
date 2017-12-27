package com.tiranaporcelain.admin.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.NewCustomerView;
import com.tiranaporcelain.admin.presenters.NewCustomerPresenter;
import com.tiranaporcelain.admin.presenters.NewCustomerPresenterImpl;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCustomerActivity extends BaseActivity implements NewCustomerView{

    @BindView(R.id.input_phone)
    EditText phone;

    @BindView(R.id.input_name)
    EditText name;

    @BindString(R.string.err_input_not_valid)
    String errInputNotValid;

    NewCustomerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        ButterKnife.bind(this);
        presenter = new NewCustomerPresenterImpl(this);
    }


    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void invalidName() {
        name.setError(errInputNotValid);
    }

    @Override
    public void invalidPhone() {
        phone.setError(errInputNotValid);
    }


    @OnClick(R.id.submit)
    void onSubmit(){
        presenter.createCustomer(name.getText().toString(), phone.getText().toString());
    }
}
