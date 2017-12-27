package com.tiranaporcelain.admin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.tiranaporcelain.admin.AccountryApplication;
import com.tiranaporcelain.admin.BuildConfig;
import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.CacheSerializer;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.SizeOf;

import java.io.ByteArrayOutputStream;

/**
 * Created by mphj on 10/22/2017.
 */

public class CacheUtils {

    private static volatile DualCache<Bitmap> bitmapDualCache;

    private static DualCache<Bitmap> bitmap(){
        if (bitmapDualCache == null){
            synchronized (CacheUtils.class){
                if (bitmapDualCache != null){
                    bitmapDualCache = new Builder<Bitmap>("bitmap", BuildConfig.VERSION_CODE)
                            .enableLog()
                            .useReferenceInRam(getBitmapMemory(), new SizeOfBitmap())
                            .useSerializerInDisk(getBitmapDisk(), true, new BitmapCacheSerializer(), AccountryApplication.context())
                            .build();
                }
            }
        }
        return bitmapDualCache;
    }


    private static class SizeOfBitmap implements SizeOf<Bitmap>{

        @Override
        public int sizeOf(Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    }

    private static class BitmapCacheSerializer implements CacheSerializer<Bitmap>{

        @Override
        public Bitmap fromString(String s) {
            try {
                byte [] encodeByte = Base64.decode(s, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch(Exception e) {
                e.getMessage();
                return null;
            }
        }

        @Override
        public String toString(Bitmap bitmap) {
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }
    }


    private static int getBitmapMemory(){
        return DeviceUtils.getFreeMemory() / 8;
    }

    private static int getBitmapDisk(){
        int max = 16 * 1024 * 1024;
        if (DeviceUtils.getFreeDisk() < max)
            return (int)(DeviceUtils.getFreeDisk() / 2);
        return max;
    }
}
