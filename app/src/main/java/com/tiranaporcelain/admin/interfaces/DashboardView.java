package com.tiranaporcelain.admin.interfaces;

/**
 * Created by mphj on 10/20/2017.
 */

public interface DashboardView {
    void startProgress();
    void endProgress();
    void showFab();
    void hideFab();
    void showNewProductActivity();
    void showNewCategoryActivity();
    void showNewCustomerActivity();
    void showExportProductActivity();
}
