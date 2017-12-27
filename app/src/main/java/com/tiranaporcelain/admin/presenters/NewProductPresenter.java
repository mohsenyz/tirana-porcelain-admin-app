package com.tiranaporcelain.admin.presenters;

import com.tiranaporcelain.admin.models.db.Category;

/**
 * Created by mphj on 10/20/2017.
 */

public interface NewProductPresenter extends BasePresenter {
    void createProduct(String name, String token, String price, String customerPrice, Category category, String count);
    void generateBarcode(String barcode);
    void generateBarcode();
}
