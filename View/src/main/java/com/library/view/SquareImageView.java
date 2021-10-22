package com.library.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by qu_xi on 2018/3/14.
 * 正方形ImageView
 */

public class SquareImageView extends AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
