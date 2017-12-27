package com.tiranaporcelain.admin.models;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by mphj on 10/23/2017.
 */

public class SimpleListModel {
    private String title;
    private int icon;
    private Bitmap iconBitmap;
    private View.OnClickListener onClickListener;

    public SimpleListModel(String title, Bitmap iconBitmap, View.OnClickListener onClickListener) {
        this.title = title;
        this.iconBitmap = iconBitmap;
        this.onClickListener = onClickListener;
    }

    public SimpleListModel(String title, int icon, View.OnClickListener onClickListener) {
        this.title = title;
        this.icon = icon;
        this.onClickListener = onClickListener;
    }

    public SimpleListModel(String title, Bitmap iconBitmap) {
        this.title = title;
        this.iconBitmap = iconBitmap;
    }


    public SimpleListModel(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public SimpleListModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
