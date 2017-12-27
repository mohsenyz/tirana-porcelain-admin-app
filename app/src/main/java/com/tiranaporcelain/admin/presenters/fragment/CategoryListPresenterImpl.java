package com.tiranaporcelain.admin.presenters.fragment;

import com.tiranaporcelain.admin.interfaces.fragment.CategoryListView;
import com.tiranaporcelain.admin.models.db.CategoryDao;
import com.tiranaporcelain.admin.utils.DaoManager;


/**
 * Created by mphj on 10/20/2017.
 */

public class CategoryListPresenterImpl implements CategoryListPresenter {

    CategoryListView view;

    public CategoryListPresenterImpl(CategoryListView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList() {
        CategoryDao dao = DaoManager.session().getCategoryDao();
        view.setAdapter(dao.loadAll());
    }
}
