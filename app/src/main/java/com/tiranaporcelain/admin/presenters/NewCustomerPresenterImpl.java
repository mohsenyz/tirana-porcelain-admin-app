package com.tiranaporcelain.admin.presenters;

import android.text.TextUtils;

import com.tiranaporcelain.admin.interfaces.NewCustomerView;
import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.models.db.CustomerDao;
import com.tiranaporcelain.admin.utils.DaoManager;


/**
 * Created by mphj on 10/20/2017.
 */

public class NewCustomerPresenterImpl implements NewCustomerPresenter {

    NewCustomerView view;

    public NewCustomerPresenterImpl(NewCustomerView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void createCustomer(String name, String phone) {
        if (TextUtils.isEmpty(name)) {
            view.invalidName();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            view.invalidPhone();
            return;
        }

        CustomerDao customerDao = DaoManager.session().getCustomerDao();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setCreatedAt(System.currentTimeMillis() / 1000L);
        customerDao.save(customer);

        view.finishActivity();
    }
}
