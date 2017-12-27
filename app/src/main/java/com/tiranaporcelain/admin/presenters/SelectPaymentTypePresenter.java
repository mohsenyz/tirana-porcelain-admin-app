package com.tiranaporcelain.admin.presenters;

/**
 * Created by mphj on 11/20/17.
 */

public interface SelectPaymentTypePresenter extends BasePresenter {
    void submitResult(int paymentType, String serial, String price, long dueDate, String bank, String description);
}
