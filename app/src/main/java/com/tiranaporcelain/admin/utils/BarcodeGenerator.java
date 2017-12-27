package com.tiranaporcelain.admin.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mphj on 10/21/2017.
 */

public class BarcodeGenerator {
    public interface OnLoadListener{
        void onBitmapLoad(Bitmap bitmap);
    }

    public static void create(final String serial, final BarcodeFormat barcodeFormat, final int width, final int height, final OnLoadListener onLoadListener){
        Observable
                .create(new ObservableOnSubscribe<Bitmap>() {

                    @Override
                    public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(serial, barcodeFormat, width, height);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            e.onNext(bitmap);
                            e.onComplete();
                        } catch (WriterException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        onLoadListener.onBitmapLoad(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // DO NOTHING
                    }
                });
    }

    public static void create(String serial, int width, int height, OnLoadListener onLoadListener){
        create(serial, BarcodeFormat.CODE_128, width, height, onLoadListener);
    }

    public static void bind(final ImageView imageView, String serial, int width, int height){
        create(serial, BarcodeFormat.CODE_128, width, height, new OnLoadListener() {
            @Override
            public void onBitmapLoad(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public static void bind(final ImageView imageView, final String serial, int width, int height, final DualCache<Bitmap> cache){
        Bitmap bitmap = cache.get(serial);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        create(serial, BarcodeFormat.CODE_128, width, height, new OnLoadListener() {
            @Override
            public void onBitmapLoad(Bitmap bitmap) {
                cache.put(serial, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public static String random(int size){
        return MathUtils.randomIntString(size);
    }
}
