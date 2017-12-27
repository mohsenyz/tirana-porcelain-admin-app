package com.tiranaporcelain.admin.presenters.fragment.export_activity;

import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ProductListView;
import com.tiranaporcelain.admin.interfaces.fragment.export_activity.ReaddedListView;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.TransactionReadded;
import com.tiranaporcelain.admin.presenters.BasePresenter;

import java.util.List;

/**
 * Created by mphj on 11/17/17.
 */

public interface InfoPresenter extends BasePresenter{
    void setTransactionReaddedList(List<TransactionReadded> list);
    void setProductList(List<Product> list);
    void configWith(ProductListView view);
    void configWith(ReaddedListView view);
    void load();
}
