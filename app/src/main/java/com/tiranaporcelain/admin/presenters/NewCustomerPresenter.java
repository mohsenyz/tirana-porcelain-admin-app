package com.tiranaporcelain.admin.presenters;

/**
 * Created by mphj on 10/20/2017.
 */

public interface NewCustomerPresenter extends BasePresenter {
    void createCustomer(String name, String phone, String fixedPhone, String address, String description);
    void updateCustomer(String name, String phone, String fixedPhone, String address, String description, int customerId);
}