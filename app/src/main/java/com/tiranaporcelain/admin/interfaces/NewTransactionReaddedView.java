package com.tiranaporcelain.admin.interfaces;

import com.tiranaporcelain.admin.models.db.TransactionReadded;

/**
 * Created by mphj on 11/11/2017.
 */

public interface NewTransactionReaddedView {
    void invalidDescription();
    void invalidPrice();
    void finishActivity(TransactionReadded result);
}
