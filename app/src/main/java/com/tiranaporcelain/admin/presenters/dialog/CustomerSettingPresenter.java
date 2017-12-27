package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.models.db.Customer;
import com.tiranaporcelain.admin.presenters.BasePresenter;

/**
 * Created by mphj on 10/23/2017.
 */

public interface CustomerSettingPresenter extends BasePresenter {
    void loadList(Customer customer);
}
