package com.tiranaporcelain.admin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by mphj on 12/20/17.
 */

public class PermissionCompat {

    public interface Callback {
        void onReject();
        void onGrant();
    }

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    public static boolean isGranted(Context context) {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void request(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, requestCode);
    }


    public static void verify(int[] grantResult, Callback callback) {
        boolean allGranted = true;
        for (int grant : grantResult) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            callback.onGrant();
        } else {
            callback.onReject();
        }
    }

}
