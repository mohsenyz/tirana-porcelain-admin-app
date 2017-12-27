package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.models.db.Transaction;
import com.tiranaporcelain.admin.presenters.BasePresenter;

/**
 * Created by mphj on 11/20/17.
 */

public interface ReportSettingPresenter extends BasePresenter {
    void loadList(Transaction transaction);
}
