package com.tiranaporcelain.admin.interfaces;

import android.graphics.Bitmap;

import com.tiranaporcelain.admin.models.db.Category;

/**
 * Created by mphj on 10/20/2017.
 */

public interface NewProductView {
    void invalidName();
    void invalidSerial();
    void invalidPrice();
    void invalidCustomerPrice();
    void invalidCategory();
    void invalidCount();
    void setSerial(Bitmap bitmap);
    void setSerial(String serial);
    void setCategory(Category category);
    void finishActivity();
}
