package com.tiranaporcelain.admin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.tiranaporcelain.admin.R;

/**
 * Created by mphj on 10/16/2017.
 */

public class BorderedCard extends CardView {

    int borderColor;

    public BorderedCard(Context context) {
        super(context);
    }

    public BorderedCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BorderedCard,
                0, 0);

        try {
            borderColor = a.getColor(R.styleable.BorderedCard_borderColor, Color.GREEN);
        } finally {
            a.recycle();
        }

    }

    public BorderedCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BorderedCard,
                0, 0);

        try {
            borderColor = a.getColor(R.styleable.BorderedCard_borderColor, Color.GREEN);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(borderColor);
        p.setStrokeWidth(10);
        canvas.drawLine(getWidth(), 3, getWidth(), getHeight() - 2, p);
    }
}
