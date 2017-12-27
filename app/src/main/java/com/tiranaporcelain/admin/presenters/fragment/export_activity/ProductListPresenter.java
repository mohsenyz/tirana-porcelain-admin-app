package com.tiranaporcelain.admin.presenters.fragment.export_activity;

import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.presenters.BasePresenter;

import java.util.List;

/**
 * Created by mphj on 11/10/2017.
 */

public interface ProductListPresenter extends BasePresenter {
    void loadList();
    void addProduct(Product product);
    List<Product> getList();
}
