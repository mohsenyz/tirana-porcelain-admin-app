package com.tiranaporcelain.admin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tiranaporcelain.admin.fragment.PlaceHolderFragment;
import com.tiranaporcelain.admin.fragment.export_activity.InfoFragment;
import com.tiranaporcelain.admin.fragment.export_activity.ProductListFragment;
import com.tiranaporcelain.admin.fragment.export_activity.ReaddedListFragment;

/**
 * Created by mphj on 11/10/2017.
 */

public class ExportActivityPagerAdapter extends FragmentPagerAdapter {

    ProductListFragment exportActivity_productListFragment;
    ReaddedListFragment exportActivity_readdedListFragment;
    InfoFragment exportActivity_infoFragment;

    public ExportActivityPagerAdapter(FragmentManager fragmentManager,
                                      ProductListFragment fragment1,
                                      ReaddedListFragment fragment2,
                                      InfoFragment fragment3) {
        super(fragmentManager);
        exportActivity_productListFragment = fragment1;
        exportActivity_readdedListFragment = fragment2;
        exportActivity_infoFragment = fragment3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return exportActivity_productListFragment;
            case 1:
                return exportActivity_readdedListFragment;
            case 2:
                return exportActivity_infoFragment;
        }
        return PlaceHolderFragment.newInstance(position + 1);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "لیست محصولات";
            case 1:
                return "اضافات و کسورات";
            case 2:
                return "مشخصات فاکتور";
        }
        return null;
    }



}
