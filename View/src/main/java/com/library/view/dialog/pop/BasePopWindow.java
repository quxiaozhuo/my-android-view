package com.library.view.dialog.pop;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.widget.PopupWindowCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BasePopWindow<V extends ViewDataBinding> extends PopupWindow {
    protected V mBinding;
    protected int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 背景灰度  0-1  1表示全透明
     */
    protected float mAlpha = 1f;
    protected int mAnimationStyle = -1;

    //下面的几个变量只是位置处理外部点击事件（6.0以上）
    //是否只是获取宽高
    //getViewTreeObserver监听时
    private boolean isOnlyGetWH = true;
    private View mAnchorView;

    @PopVerticalPosition
    private int mVerticalGravity = PopVerticalPosition.BELOW;

    @PopHorizontalPosition
    private int mHorizontalGravity = PopHorizontalPosition.LEFT;
    private int mOffsetX = 0;
    private int mOffsetY = 0;
    private final Context mContext;

    public BasePopWindow(Context context, @LayoutRes int layoutId) {
        super(context);
        mContext = context;
        mBinding = DataBindingUtil.bind(LayoutInflater.from(context).inflate(layoutId, null));
        initView();
    }

    protected void initView() {
        setContentView(mBinding.getRoot());
        setHeight(mHeight);
        setWidth(mWidth);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mAnimationStyle != -1) {
            setAnimationStyle(mAnimationStyle);
        }
    }

    protected abstract void initData();

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        isOnlyGetWH = true;
        mAnchorView = parent;
        mOffsetX = x;
        mOffsetY = y;
        addGlobalLayoutListener(getContentView());
        super.showAtLocation(parent, gravity, x, y);
    }

    public void showAtAnchorView(@NonNull View anchorView, @PopVerticalPosition int verticalPos, @PopHorizontalPosition int horizontalPos) {
        showAtAnchorView(anchorView, verticalPos, horizontalPos, true);
    }

    public void showAtAnchorView(@NonNull View anchorView, @PopVerticalPosition int verticalPos, @PopHorizontalPosition int horizontalPos, boolean fitInScreen) {
        showAtAnchorView(anchorView, verticalPos, horizontalPos, 0, 0, fitInScreen);
    }

    public void showAtAnchorView(@NonNull View anchorView, @PopVerticalPosition int verticalPos, @PopHorizontalPosition int horizontalPos, int x, int y) {
        showAtAnchorView(anchorView, verticalPos, horizontalPos, x, y, true);
    }

    public void showAtAnchorView(@NonNull View anchorView, @PopVerticalPosition int verticalPos, @PopHorizontalPosition int horizontalPos, int x, int y, boolean fitInScreen) {
        initData();
        isOnlyGetWH = false;
        mAnchorView = anchorView;
        mOffsetX = x;
        mOffsetY = y;
        mVerticalGravity = verticalPos;
        mHorizontalGravity = horizontalPos;
        showBackgroundAnimator();
        final View contentView = getContentView();
        addGlobalLayoutListener(contentView);
        setClippingEnabled(fitInScreen);
        contentView.measure(makeDropDownMeasureSpec(getWidth()), makeDropDownMeasureSpec(getHeight()));
        final int measuredW = contentView.getMeasuredWidth();
        final int measuredH = contentView.getMeasuredHeight();
        if (!fitInScreen) {
            final int[] anchorLocation = new int[2];
            anchorView.getLocationInWindow(anchorLocation);
            x += anchorLocation[0];
            y += anchorLocation[1] + anchorView.getHeight();
        }
        y = calculateY(anchorView, verticalPos, measuredH, y);
        x = calculateX(anchorView, horizontalPos, measuredW, x);
        if (fitInScreen) {
            PopupWindowCompat.showAsDropDown(this, anchorView, x, y, Gravity.NO_GRAVITY);
        } else {
            showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
        }
    }

    /**
     * 根据垂直gravity计算y偏移
     */
    private int calculateY(View anchor, int verticalGravity, int measuredH, int y) {
        switch (verticalGravity) {
            case PopVerticalPosition.ABOVE:
                y -= measuredH + anchor.getHeight();
                break;
            case PopVerticalPosition.ALIGN_BOTTOM:
                y -= measuredH;
                break;
            case PopVerticalPosition.CENTER:
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case PopVerticalPosition.ALIGN_TOP:
                y -= anchor.getHeight();
                break;
            case PopVerticalPosition.BELOW:
                // Default position.
                break;
        }

        return y;
    }

    /**
     * 根据水平gravity计算x偏移
     */
    private int calculateX(View anchor, int horizontalGravity, int measuredW, int x) {
        switch (horizontalGravity) {
            case PopHorizontalPosition.LEFT:
                x -= measuredW;
                break;
            case PopHorizontalPosition.ALIGN_RIGHT:
                x -= measuredW - anchor.getWidth();
                break;
            case PopHorizontalPosition.CENTER:
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case PopHorizontalPosition.ALIGN_LEFT:
                // Default position.
                break;
            case PopHorizontalPosition.RIGHT:
                x += anchor.getWidth();
                break;
        }

        return x;
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), getDropDownMeasureSpecMode(measureSpec));
    }

    private static int getDropDownMeasureSpecMode(int measureSpec) {
        switch (measureSpec) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                return View.MeasureSpec.UNSPECIFIED;
            default:
                return View.MeasureSpec.EXACTLY;
        }
    }

    //监听器，用于PopupWindow弹出时获取准确的宽高
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mWidth = getContentView().getWidth();
            mHeight = getContentView().getHeight();
            //只获取宽高时，不执行更新操作
            if (isOnlyGetWH) {
                removeGlobalLayoutListener();
                return;
            }
            updateLocation(mWidth, mHeight, mAnchorView, mVerticalGravity, mHorizontalGravity, mOffsetX, mOffsetY);
            removeGlobalLayoutListener();
        }
    };

    private void updateLocation(int width, int height, @NonNull View anchor,
                                @PopVerticalPosition final int verticalGravity,
                                @PopHorizontalPosition int horizontalGravity,
                                int x, int y) {
        x = calculateX(anchor, horizontalGravity, width, x);
        y = calculateY(anchor, verticalGravity, height, y);
        update(anchor, x, y, width, height);
    }

    private void removeGlobalLayoutListener() {
        if (getContentView() != null) {
            getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
    }

    private void addGlobalLayoutListener(View contentView) {
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismissBackgroundAnimator();
        removeGlobalLayoutListener();
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    private void showBackgroundAnimator() {
        if (mAlpha >= 1f) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mAlpha);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            setWindowBackgroundAlpha(alpha);
        });
        animator.setDuration(360);
        animator.start();
    }

    /**
     * 窗口隐藏，窗口背景透明度渐变动画
     */
    private void dismissBackgroundAnimator() {
        if (mAlpha >= 1f) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mAlpha, 1.0f);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            setWindowBackgroundAlpha(alpha);
        });
        animator.setDuration(360);
        animator.start();
    }

    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof Activity) {
            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }

}
