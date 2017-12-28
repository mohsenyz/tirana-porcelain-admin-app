package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.interfaces.dialog.CategorySettingView;
import com.tiranaporcelain.admin.models.SimpleListModel;
import com.tiranaporcelain.admin.models.db.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphj on 10/23/2017.
 */

public class CategorySettingPresenterImpl implements CategorySettingPresenter {
    CategorySettingView view;

    public CategorySettingPresenterImpl(CategorySettingView view){
        this.view = view;
    }
    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList(final Category category) {
        List<SimpleListModel> list = new ArrayList<>();
        SimpleListModel model = new SimpleListModel("ویرایش", R.drawable.ic_gray_edit);
        list.add(model);
        list.add(model);
        list.add(model);
        view.setAdapter(list);
    }
}
