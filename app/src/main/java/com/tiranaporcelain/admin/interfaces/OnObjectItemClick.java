package com.tiranaporcelain.admin.interfaces;

import android.view.View;

/**
 * Created by mphj on 10/23/2017.
 */

public interface OnObjectItemClick<T> {
    void onClick(View v, T object);
}
