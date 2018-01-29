package com.tiranaporcelain.admin.presenters;

import android.text.TextUtils;

import com.tiranaporcelain.admin.interfaces.NewCategoryView;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.models.db.CategoryDao;
import com.tiranaporcelain.admin.utils.DaoManager;

/**
 * Created by mphj on 10/20/2017.
 */

public class NewCategoryPresenterImpl implements NewCategoryPresenter {

    NewCategoryView view;

    public NewCategoryPresenterImpl(NewCategoryView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void newStorage(String name) {
        if (TextUtils.isEmpty(name)) {
            view.invalidStorageName();
            return;
        }
        CategoryDao dao = DaoManager.session().getCategoryDao();
        Category category = new Category();
        category.setName(name);
        dao.save(category);
        view.finishActivity();
    }


    @Override
    public void updateStorage(String name, int categoryId) {
        if (TextUtils.isEmpty(name)) {
            view.invalidStorageName();
            return;
        }
        CategoryDao dao = DaoManager.session().getCategoryDao();
        Category category = dao.load((long) categoryId);
        category.setName(name);
        dao.save(category);
        view.finishActivity();
    }
}
