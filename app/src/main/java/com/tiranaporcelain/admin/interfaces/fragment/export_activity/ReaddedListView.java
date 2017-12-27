package com.tiranaporcelain.admin.interfaces.fragment.export_activity;

import com.tiranaporcelain.admin.models.db.TransactionReadded;

import java.util.List;

/**
 * Created by mphj on 11/10/2017.
 */

public interface ReaddedListView {
    void loadList(List<TransactionReadded> list);
    void addNew(TransactionReadded transactionReadded);
    List<TransactionReadded> getList();
}
