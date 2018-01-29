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
    public void createCustomer(String name,
                               String phone,
                               String fixedPhone,
                               String address,
                               String description) {
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
        customer.setFixedPhone(fixedPhone);
        customer.setAddress(address);
        customer.setDescription(description);
        customer.setCreatedAt(System.currentTimeMillis() / 1000L);
        customerDao.save(customer);

        view.finishActivity();
    }


    @Override
    public void updateCustomer(String name, String phone, String fixedPhone, String address, String description, int customerId) {
        if (TextUtils.isEmpty(name)) {
            view.invalidName();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            view.invalidPhone();
            return;
        }

        CustomerDao customerDao = DaoManager.session().getCustomerDao();

        Customer customer = customerDao.load((long) customerId);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setFixedPhone(fixedPhone);
        customer.setAddress(address);
        customer.setDescription(description);
        customer.setCreatedAt(System.currentTimeMillis() / 1000L);
        customerDao.save(customer);

        view.finishActivity();
    }
}
