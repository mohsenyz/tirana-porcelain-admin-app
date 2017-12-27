package com.tiranaporcelain.admin.interfaces.dialog;

import com.tiranaporcelain.admin.models.SimpleListModel;

import java.util.List;

/**
 * Created by mphj on 10/23/2017.
 */

public interface ProductSettingView {
    void setAdapter(List<SimpleListModel> list);
    void increase();
    void increasedSuccessfully();
}
