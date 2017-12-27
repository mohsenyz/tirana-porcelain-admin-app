package com.tiranaporcelain.admin.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by mphj on 12/27/17.
 */

public class SimpleDrawable extends Drawable implements Animatable, Runnable {

    Paint paint = new Paint();

    public SimpleDrawable() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    int radius = 0;
    int maxRadius = -1;
    int durationMillis = 500;
    int backgroundColor = Color.YELLOW;
    int rippleColor = Color.BLUE;
    final int FRAME = 1000 / 60;

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (isRunning()) {
            paint.setColor(backgroundColor);
        } else {
            paint.setColor(rippleColor);
        }
        canvas.drawRect(0, 0, getBounds().right, getBounds().bottom,  paint);
        if (isRunning()) {
            paint.setColor(rippleColor);
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), radius, paint);
            //canvas.restoreToCount(canvas.save());
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        maxRadius = (int) Math.round(
                Math.ceil(
                        Math.sqrt(
                                Math.pow(Math.ceil(getBounds().width() / 2), 2) +
                                        Math.pow(Math.ceil(getBounds().height() / 2), 2)
                        )
                )
        );
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        isRunning = true;
        invalidateSelf();
        scheduleSelf(this, SystemClock.uptimeMillis() + FRAME);
    }


    @Override
    public void run() {
        if (radius >= maxRadius) {
            radius = 0;
            isRunning = false;
            stop();
        } else {
            radius += getStep();
            invalidateSelf();
            scheduleSelf(this, SystemClock.uptimeMillis() + FRAME);
        }
    }

    public int getStep() {
        int maxDrawCount = Math.round(durationMillis / FRAME);
        int step = Math.max(Math.round(maxRadius / maxDrawCount), 1);
        return step;
    }

    @Override
    public void stop() {
        unscheduleSelf(this);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setDurationMillis(int durationMillis) {
        this.durationMillis = durationMillis;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    boolean isRunning = false;

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
