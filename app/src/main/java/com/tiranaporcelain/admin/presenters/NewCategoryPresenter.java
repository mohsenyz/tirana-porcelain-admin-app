package com.tiranaporcelain.admin.presenters;

/**
 * Created by mphj on 10/20/2017.
 */

public interface NewCategoryPresenter extends BasePresenter{
    void newStorage(String name);
    void updateStorage(String name, int categoryId);
}
