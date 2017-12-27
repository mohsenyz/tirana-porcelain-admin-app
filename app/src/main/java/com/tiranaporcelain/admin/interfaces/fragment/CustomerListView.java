package com.tiranaporcelain.admin.interfaces.fragment;

import com.tiranaporcelain.admin.models.db.Customer;

import java.util.List;


/**
 * Created by mphj on 10/20/2017.
 */

public interface CustomerListView {
    void setAdapter(List<Customer> realmResults);
}
