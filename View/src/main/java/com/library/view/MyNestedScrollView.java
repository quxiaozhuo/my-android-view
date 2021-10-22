package com.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.core.widget.NestedScrollView;


/**
 * @author 崔亚运
 * @data 2020/6/4
 * @description
 */
public class MyNestedScrollView extends NestedScrollView {
    private int downX;
    private int downY;
    private int moveX;
    private int moveY;
    private int mTouchSlop;

    public MyNestedScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initialize(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initialize(context, attrs);
    }

    private int mMaxHeight;

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyNestedScrollView);
        mMaxHeight = typedArray.getLayoutDimension(R.styleable.MyNestedScrollView_maxHeight, mMaxHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) e.getRawY();
                moveX = (int) e.getRawX();
                if (Math.abs(moveX - downX) > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}

