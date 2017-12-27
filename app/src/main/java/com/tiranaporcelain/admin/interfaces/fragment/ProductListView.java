package com.tiranaporcelain.admin.interfaces.fragment;

import com.tiranaporcelain.admin.models.db.Product;

import java.util.List;

/**
 * Created by mphj on 10/22/2017.
 */

public interface ProductListView {
    void setAdapter(List<Product> realmResults);
}
