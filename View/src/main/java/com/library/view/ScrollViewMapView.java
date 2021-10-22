package com.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ScrollViewMapView extends FrameLayout {
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);//拦截父类事件

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    public ScrollViewMapView(Context context) {
        super(context);
    }

    public ScrollViewMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
