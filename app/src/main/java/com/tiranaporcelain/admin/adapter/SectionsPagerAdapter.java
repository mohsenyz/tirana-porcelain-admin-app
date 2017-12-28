package com.tiranaporcelain.admin.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tiranaporcelain.admin.activity.DashboardActivity;
import com.tiranaporcelain.admin.fragment.CategoryListFragment;
import com.tiranaporcelain.admin.fragment.CustomerListFragment;
import com.tiranaporcelain.admin.fragment.MainFragment;
import com.tiranaporcelain.admin.fragment.PlaceHolderFragment;
import com.tiranaporcelain.admin.models.db.CategoryDao;
import com.tiranaporcelain.admin.models.db.CustomerDao;
import com.tiranaporcelain.admin.utils.DaoManager;

/**
 * Created by mphj on 10/20/2017.
 */

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    DashboardActivity dashboardActivity;

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) { }

    public SectionsPagerAdapter(FragmentManager fm, DashboardActivity dashboardActivity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME:
                return MainFragment.newInstance();
            case PRODUCTS:
                CategoryDao categoryDao = DaoManager.session().getCategoryDao();
                if (categoryDao.count() == 0)
                    return PlaceHolderFragment.newInstance(0);
                return CategoryListFragment.newInstance();
            case CUSTOMERS:
                CustomerDao customerDao = DaoManager.session().getCustomerDao();
                if (customerDao.count() == 0)
                    return PlaceHolderFragment.newInstance(0);
                return CustomerListFragment.newInstance();
            case REPORTS:
                return PlaceHolderFragment.newInstance(0);
        }
        return PlaceHolderFragment.newInstance(position + 1);
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "خانه";
            case 1:
                return "گزارشات";
            case 2:
                return "محصولات";
            case 3:
                return "اشخاص";
            case 4:
                return "چک ها";
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public static final int HOME = 3, REPORTS = 2, PRODUCTS = 1, CUSTOMERS = 0;
}
