package com.tiranaporcelain.admin.presenters;

import com.tiranaporcelain.admin.adapter.SectionsPagerAdapter;
import com.tiranaporcelain.admin.interfaces.DashboardView;
import com.tiranaporcelain.admin.utils.ArrayUtils;

/**
 * Created by mphj on 10/20/2017.
 */

public class DashboardPresenterImpl implements DashboardPresenter {

    DashboardView view;
    private static final int[] UNFABBED_PAGES = {SectionsPagerAdapter.HOME};
    int currentPosition;

    public DashboardPresenterImpl(DashboardView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPageChanged(int position) {
        currentPosition = position;
        if (currentPosition == 2) {
            isFabMenuShown = false;
        }
        if(ArrayUtils.contains(UNFABBED_PAGES, position)){
            view.hideFab();
        } else {
            view.showFab();
        }
    }

    boolean isFabMenuShown = false;

    @Override
    public void onFabClick() {
        switch (currentPosition){
            case SectionsPagerAdapter.CUSTOMERS:
                view.showNewCustomerActivity();
                break;
            case SectionsPagerAdapter.PRODUCTS:
                view.showNewCategoryActivity();
                break;
            case SectionsPagerAdapter.REPORTS:
                if (isFabMenuShown) {
                    view.hideFabMenu();
                } else {
                    view.showFabMenu();
                }
                isFabMenuShown = !isFabMenuShown;
                break;
            default:
                throw new RuntimeException(
                        "Nothing implemented for position " + currentPosition
                );
        }
    }
}
