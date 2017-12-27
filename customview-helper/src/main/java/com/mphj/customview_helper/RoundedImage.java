package com.mphj.customview_helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.mphj.customview_helper.utils.Circle;
import com.mphj.customview_helper.utils.Coordinate;

/**
 * Created by mphj on 10/29/2017.
 */

public class RoundedImage extends android.support.v7.widget.AppCompatImageView {

    private boolean shadow = false;
    private int shadowRadius = 5;
    private int circleOffset = 50;
    private final Paint croppedBitmapPaint = new Paint();
    private final Paint antiAliasPaint = new Paint();
    private Bitmap drawableBitmap;
    private Bitmap roundBitmap;
    private int lastWidth, lastHeight;

    public RoundedImage(Context context) {
        super(context);
    }

    public RoundedImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public RoundedImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RoundedImage,
                0, 0);

        try {
            shadow = a.getBoolean(R.styleable.RoundedImage_shadow, shadow);
            shadowRadius = a.getDimensionPixelSize(R.styleable.RoundedImage_shadowRadius, shadowRadius);
            circleOffset = a.getDimensionPixelSize(R.styleable.RoundedImage_radiusOffset, circleOffset);
            if (!shadow){
                shadowRadius = 0;
            }
        } finally {
            a.recycle();
        }

        croppedBitmapPaint.setAntiAlias(true);
        croppedBitmapPaint.setFilterBitmap(true);
        croppedBitmapPaint.setDither(true);

        antiAliasPaint.setAntiAlias(true);
    }

    private Bitmap getDrawableBitmap(){
        if (drawableBitmap == null){
            Bitmap b = ((BitmapDrawable) getDrawable()).getBitmap();
            //drawableBitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            //b.recycle();
            drawableBitmap = b;
        }
        return drawableBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long i = System.currentTimeMillis();
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        if (lastWidth != width || lastHeight != height || roundBitmap == null){
            if (roundBitmap != null){
                roundBitmap.recycle();
            }
            roundBitmap = getCroppedBitmap(getDrawableBitmap(), width, height);
            lastHeight = height;
            lastWidth = width;
        }
        canvas.drawBitmap(roundBitmap, 0, 0, antiAliasPaint);
        Log.i("hello", "" + (System.currentTimeMillis() - i));
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int width, int height) {
        Bitmap sbmp;

        if (bmp.getWidth() != width || bmp.getHeight() != height) {
            /*sbmp = Bitmap.createScaledBitmap(bmp,
                    width,
                    height - shadowRadius, false);*/
            sbmp = Bitmap.createBitmap(bmp, 0, 0, width, height);
            /*Matrix matrix = new Matrix();
            matrix.postRotate(5);
            sbmp = Bitmap.createBitmap(sbmp, 0, 0, sbmp.getWidth(), sbmp.getHeight(), matrix, true);*/
        } else {
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(width, height - shadowRadius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Rect rect = new Rect(0, 0, width, height - shadowRadius);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((float) circleCenter.x, (float) circleCenter.y - shadowRadius, circleRadius, croppedBitmapPaint);
        croppedBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, croppedBitmapPaint);
        croppedBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        croppedBitmapPaint.setShadowLayer(shadowRadius, 0, 0, Color.BLACK);
        canvas.drawCircle((float) circleCenter.x, (float) circleCenter.y - shadowRadius, circleRadius, croppedBitmapPaint);
        sbmp.recycle();
        return output;
    }


    private Coordinate circleCenter;
    private int circleRadius;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Circle circle = new Circle(0, h - circleOffset - shadowRadius,
                w / 2, h - shadowRadius,
                w, h - circleOffset - shadowRadius);
        circleCenter = circle.getCenter();
        circleRadius = (int)Math.round(circle.getRadius());
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
