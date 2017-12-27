package com.tiranaporcelain.admin.presenters.fragment;

import com.tiranaporcelain.admin.interfaces.fragment.CustomerListView;
import com.tiranaporcelain.admin.models.db.CustomerDao;
import com.tiranaporcelain.admin.utils.DaoManager;

/**
 * Created by mphj on 10/20/2017.
 */

public class CustomerListPresenterImpl implements CustomerListPresenter {

    CustomerListView view;

    public CustomerListPresenterImpl(CustomerListView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList() {
        CustomerDao dao = DaoManager.session().getCustomerDao();
        view.setAdapter(dao.loadAll());
    }
}
