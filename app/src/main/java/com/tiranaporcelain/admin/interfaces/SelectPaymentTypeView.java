package com.tiranaporcelain.admin.interfaces;

import com.tiranaporcelain.admin.models.db.Check;

/**
 * Created by mphj on 11/20/17.
 */

public interface SelectPaymentTypeView {
    void showCheckInfo();
    void hideCheckInfo();
    void finishActivity(Check check, int paymentType);
}
