package com.tiranaporcelain.admin.interfaces;

/**
 * Created by mphj on 10/23/2017.
 */

public interface ImportProductView {
    void productAlreadyExists();
    void productNotFound();
    void showGetCountActivity();
    void setStorageName(String name);
    void notifyDataSetChanged();
    void finishActivity();
}
