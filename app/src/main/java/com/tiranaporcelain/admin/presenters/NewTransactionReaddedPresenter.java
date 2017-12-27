package com.tiranaporcelain.admin.presenters;

/**
 * Created by mphj on 11/11/2017.
 */

public interface NewTransactionReaddedPresenter extends BasePresenter {
    void sendResult(String description, String price, int type);
}
