package com.tiranaporcelain.admin.presenters;

/**
 * Created by mphj on 10/20/2017.
 */

public interface DashboardPresenter extends BasePresenter {
    void onPageChanged(int position);
    void onFabClick();
}
