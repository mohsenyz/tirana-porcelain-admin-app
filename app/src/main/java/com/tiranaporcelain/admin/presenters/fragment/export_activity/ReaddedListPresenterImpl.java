package com.tiranaporcelain.admin.presenters.fragment.export_activity;

import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ReaddedListView;
import com.tiranaporcelain.admin.models.db.TransactionReadded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphj on 11/10/2017.
 */

public class ReaddedListPresenterImpl implements ReaddedListPresenter {

    ReaddedListView view;
    List<TransactionReadded> list = new ArrayList<>();

    public ReaddedListPresenterImpl(ReaddedListView view) {
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
        view.loadList(list);
    }

    @Override
    public void addNew(TransactionReadded transactionReadded) {
        list.add(transactionReadded);
        loadList();
    }

    @Override
    public List<TransactionReadded> getList() {
        return list;
    }


    @Override
    public void delete(TransactionReadded transactionReadded) {
        list.remove(transactionReadded);
    }
}
