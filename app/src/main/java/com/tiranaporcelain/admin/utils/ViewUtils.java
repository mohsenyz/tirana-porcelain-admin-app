package com.tiranaporcelain.admin.utils;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mphj on 1/14/18.
 */

public class ViewUtils {

    public static boolean isInViewBound(View view, MotionEvent motionEvent) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (rect.contains(view.getLeft() + (int) motionEvent.getX(),
                view.getTop() + (int) motionEvent.getY()))
            return true;
        return false;
    }

}
