package com.tiranaporcelain.admin.interfaces;

/**
 * Created by mphj on 10/20/2017.
 */

public interface DashboardView {
    void startProgress();
    void endProgress();
    void showFab();
    void showFabMenu();
    void hideFabMenu();
    void hideFab();
    void showNewCategoryActivity();
    void showNewCustomerActivity();
}
