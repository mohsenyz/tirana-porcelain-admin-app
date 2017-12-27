package com.tiranaporcelain.admin.interfaces.fragment.export_activity;

import com.tiranaporcelain.admin.models.db.Product;

import java.util.List;

/**
 * Created by mphj on 11/10/2017.
 */

public interface ProductListView {
    void loadList(List<Product> list);
    void addProduct(Product product);
    void errorInsufficientAmount(Product product);
    void errorSerialNotFound();
    List<Product> getList();
}
