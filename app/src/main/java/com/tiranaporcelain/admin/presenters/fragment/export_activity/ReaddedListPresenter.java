package com.tiranaporcelain.admin.presenters.fragment.export_activity;

import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.presenters.BasePresenter;

import java.util.List;

/**
 * Created by mphj on 11/10/2017.
 */

public interface ReaddedListPresenter extends BasePresenter {
    void loadList();
    void addNew(TransactionReadded transactionReadded);
    void delete(TransactionReadded transactionReadded);
    List<TransactionReadded> getList();
}
