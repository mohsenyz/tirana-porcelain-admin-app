package com.tiranaporcelain.admin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tiranaporcelain.admin.R;
import com.tiranaporcelain.admin.fragment.ProductListFragment;
import com.tiranaporcelain.admin.interfaces.OnObjectItemClick;
import com.tiranaporcelain.admin.models.db.Product;

import org.parceler.Parcels;

public class SelectProductActivity extends BaseActivity implements OnObjectItemClick<Product>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProductListFragment fragment = ProductListFragment.newInstance(true);
        fragment.setOnObjectItemClickListener(this);
        ft.add(R.id.fragment, fragment);
        ft.commit();
        setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v, Product object) {
        Intent i = new Intent();
        i.putExtra("product", Parcels.wrap(Product.class, object));
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
