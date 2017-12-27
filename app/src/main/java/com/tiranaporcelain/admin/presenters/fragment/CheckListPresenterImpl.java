package com.tiranaporcelain.admin.presenters.fragment;

import com.tiranaporcelain.admin.interfaces.fragment.CheckListView;
import com.tiranaporcelain.admin.models.db.CheckDao;
import com.tiranaporcelain.admin.utils.DaoManager;

/**
 * Created by mphj on 11/20/17.
 */

public class CheckListPresenterImpl implements CheckListPresenter {

    CheckListView view;

    public CheckListPresenterImpl(CheckListView view) {
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
        CheckDao checkDao = DaoManager.session().getCheckDao();
        view.setAdapter(checkDao.loadAll());
    }
}
