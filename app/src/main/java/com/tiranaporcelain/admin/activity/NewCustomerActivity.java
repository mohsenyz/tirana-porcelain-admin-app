package com.tiranaporcelain.admin.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.NewCustomerView;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.presenters.NewCustomerPresenter;
import com.tiranaporcelain.admin.presenters.NewCustomerPresenterImpl;
import com.tiranaporcelain.admin.utils.DaoManager;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCustomerActivity extends BaseActivity implements NewCustomerView{


    @BindView(R.id.input_name)
    EditText name;

    @BindView(R.id.input_phone)
    EditText phone;

    @BindView(R.id.input_fixed_phone)
    EditText fixedPhone;

    @BindView(R.id.input_address)
    EditText address;

    @BindView(R.id.input_description)
    EditText description;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.submit)
    Button submit;

    @BindString(R.string.err_input_not_valid)
    String errInputNotValid;

    NewCustomerPresenter presenter;

    int customerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        ButterKnife.bind(this);
        presenter = new NewCustomerPresenterImpl(this);
        customerId = getIntent().getIntExtra("id", -1);
        if (customerId != -1) {
            title.setText("ویرایش شخص");
            Customer customer = DaoManager.session().getCustomerDao().load((long) customerId);
            name.setText(customer.getName());
            phone.setText(customer.getPhone());
            fixedPhone.setText(customer.getFixedPhone());
            address.setText(customer.getAddress());
            description.setText(customer.getDescription());
            submit.setText("ویرایش");
        }
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
        if (customerId == -1) {
            presenter.createCustomer(
                    name.getText().toString(),
                    phone.getText().toString(),
                    fixedPhone.getText().toString(),
                    address.getText().toString(),
                    description.getText().toString()
            );
        } else {
            presenter.updateCustomer(
                    name.getText().toString(),
                    phone.getText().toString(),
                    fixedPhone.getText().toString(),
                    address.getText().toString(),
                    description.getText().toString(),
                    customerId
            );
        }
    }
}
