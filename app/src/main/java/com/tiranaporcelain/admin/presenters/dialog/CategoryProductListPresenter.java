package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.presenters.BasePresenter;

/**
 * Created by mphj on 10/24/2017.
 */

public interface CategoryProductListPresenter extends BasePresenter {

    void loadList(Category category);

}
