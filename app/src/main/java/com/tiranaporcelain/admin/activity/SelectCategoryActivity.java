package com.tiranaporcelain.admin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.fragment.CategoryListFragment;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Category;

import org.parceler.Parcels;

public class SelectCategoryActivity extends BaseActivity implements OnObjectItemClick<Category>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_storage);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CategoryListFragment fragment = CategoryListFragment.newInstance(true);
        fragment.setOnObjectItemClickListener(this);
        ft.add(R.id.fragment, fragment);
        ft.commit();
        setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v, Category object) {
        Intent i = new Intent();
        i.putExtra("category", Parcels.wrap(Category.class, object));
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
