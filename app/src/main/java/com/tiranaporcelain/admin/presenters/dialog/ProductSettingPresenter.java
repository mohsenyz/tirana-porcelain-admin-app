package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.presenters.BasePresenter;

/**
 * Created by mphj on 10/23/2017.
 */

public interface ProductSettingPresenter extends BasePresenter {
    void loadList();
    void increaseProduct(Product product, int count);
}
