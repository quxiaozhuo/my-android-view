package com.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * 编辑者：baba
 * Date:2020-12-11
 */
public class DottedLineView extends View {

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private int orientation;

    public DottedLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int dashGap, dashLength, dashThickness;
        int color;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DottedLineView, 0, 0);

        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DottedLineView_dashGap, 5);
            dashLength = a.getDimensionPixelSize(R.styleable.DottedLineView_dashLength, 5);
            dashThickness = a.getDimensionPixelSize(R.styleable.DottedLineView_dashThickness, 3);
            color = a.getColor(R.styleable.DottedLineView_divider_line_color, 0xff000000);
            orientation = a.getInt(R.styleable.DottedLineView_divider_orientation, 1);
        } finally {
            a.recycle();
        }

        DashPathEffect dashPathEffect1 = new DashPathEffect(new float[]{dashGap, dashLength}, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dashThickness);
        mPaint.setColor(color);
        mPaint.setPathEffect(dashPathEffect1);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (orientation == 1) {
            // 绘制横虚线
            canvas.drawLine(0, 0, mWidth, 0, mPaint);
        } else {
            // 绘制竖虚线
            canvas.drawLine(0, 0, 0, mHeight, mPaint);
        }
    }
}
