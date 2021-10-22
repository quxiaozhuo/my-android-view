package com.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 编辑者：baba
 * Date:2021-02-01
 */
public class MyRecyclerView extends RecyclerView {
    private int mMaxHeight;
    private int mMaxWidth;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRecyclerView);
        mMaxHeight = typedArray.getLayoutDimension(R.styleable.MyRecyclerView_maxHeight, mMaxHeight);
        mMaxWidth = typedArray.getLayoutDimension(R.styleable.MyRecyclerView_maxWidth, mMaxWidth);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int limitHeight = getMeasuredHeight();
        int limitWith = getMeasuredWidth();
        if (mMaxHeight > 0 && getMeasuredHeight() > mMaxHeight) {
            limitHeight = mMaxHeight;
        }
        if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
            limitWith = mMaxWidth;
        }
        setMeasuredDimension(limitWith, limitHeight);
    }
}
