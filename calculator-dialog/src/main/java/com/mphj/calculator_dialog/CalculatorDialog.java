package com.mphj.calculator_dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by mphj on 11/17/17.
 */

public class CalculatorDialog {
    public static void show(Activity activity, int requestCode) {
        activity.startActivityForResult(
                new Intent(activity, MainActivity.class), requestCode);
    }



    @Nullable
    public static String parseResult(Intent data, int responseCode) {
        if (responseCode == Activity.RESULT_OK) {
            return String.valueOf(data.getIntExtra("result", 0));
        }
        return null;
    }
}
