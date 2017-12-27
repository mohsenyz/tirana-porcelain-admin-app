package com.tiranaporcelain.admin.utils;

import android.graphics.Typeface;

import com.tiranaporcelain.admin.AccountryApplication;

/**
 * Created by mphj on 11/19/17.
 */

public class TypefaceUtils {
    public static Typeface def() {
        return Typeface.createFromAsset(AccountryApplication.context().getAssets(), "fonts/iran.ttf");
    }
}
