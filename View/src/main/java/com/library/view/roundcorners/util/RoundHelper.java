package com.library.view.roundcorners.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.library.view.R;


public class RoundHelper {

    private Context mContext;
    private View mView;

    private Paint mPaint;
    private RectF mRectF;
    private RectF mStrokeRectF;

    private Path mPath;
    private Path mTempPath;

    private Xfermode mXfermode;

    private boolean isCircle;

    private float[] mRadii;
    private float[] mStrokeRadii;

    private int mWidth;
    private int mHeight;
    private int mStrokeColor;
    private float mStrokeWidth;
    private boolean mStrokeDottedLine;
    private float mStrokeDottedLineIntervals;
    private float mStrokeDottedNullIntervals;

    private float mRadiusTopLeft;
    private float mRadiusTopRight;
    private float mRadiusBottomLeft;
    private float mRadiusBottomRight;

    public void init(Context context, AttributeSet attrs, View view) {
        if (view instanceof ViewGroup && view.getBackground() == null) {
            view.setBackgroundColor(Color.parseColor("#00000000"));
        }
        mContext = context;
        mView = view;
        mRadii = new float[8];
        mStrokeRadii = new float[8];
        mPaint = new Paint();
        mRectF = new RectF();
        mStrokeRectF = new RectF();
        mPath = new Path();
        mTempPath = new Path();
        mXfermode = new PorterDuffXfermode(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PorterDuff.Mode.DST_OUT : PorterDuff.Mode.DST_IN);
        mStrokeColor = Color.WHITE;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundCorner);
        if (array == null) {
            return;
        }
        float radius = array.getDimension(R.styleable.RoundCorner_rRadius, 0);
        float radiusLeft = array.getDimension(R.styleable.RoundCorner_rLeftRadius, radius);
        float radiusRight = array.getDimension(R.styleable.RoundCorner_rRightRadius, radius);
        float radiusTop = array.getDimension(R.styleable.RoundCorner_rTopRadius, radius);
        float radiusBottom = array.getDimension(R.styleable.RoundCorner_rBottomRadius, radius);

        mRadiusTopLeft = array.getDimension(R.styleable.RoundCorner_rTopLeftRadius, radiusTop > 0 ? radiusTop : radiusLeft);
        mRadiusTopRight = array.getDimension(R.styleable.RoundCorner_rTopRightRadius, radiusTop > 0 ? radiusTop : radiusRight);
        mRadiusBottomLeft = array.getDimension(R.styleable.RoundCorner_rBottomLeftRadius, radiusBottom > 0 ? radiusBottom : radiusLeft);
        mRadiusBottomRight = array.getDimension(R.styleable.RoundCorner_rBottomRightRadius, radiusBottom > 0 ? radiusBottom : radiusRight);

        mStrokeWidth = array.getDimension(R.styleable.RoundCorner_rStrokeWidth, 0);
        mStrokeColor = array.getColor(R.styleable.RoundCorner_rStrokeColor, mStrokeColor);
        mStrokeDottedLine = array.getBoolean(R.styleable.RoundCorner_rStrokeDottedLine, false);
        mStrokeDottedLineIntervals = array.getDimension(R.styleable.RoundCorner_rStrokeDottedLineIntervals, 5);
        mStrokeDottedNullIntervals = array.getDimension(R.styleable.RoundCorner_rStrokeDottedNullIntervals, 5);

        array.recycle();
        if (!isCircle) {
            setRadius();
        }
    }

    private void setRadius() {
        mRadii[0] = mRadii[1] = mRadiusTopLeft;
        mRadii[2] = mRadii[3] = mRadiusTopRight;
        mRadii[4] = mRadii[5] = mRadiusBottomRight;
        mRadii[6] = mRadii[7] = mRadiusBottomLeft;

        mStrokeRadii[0] = mStrokeRadii[1] = mRadiusTopLeft;
        mStrokeRadii[2] = mStrokeRadii[3] = mRadiusTopRight;
        mStrokeRadii[4] = mStrokeRadii[5] = mRadiusBottomRight;
        mStrokeRadii[6] = mStrokeRadii[7] = mRadiusBottomLeft;
    }

    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;

        if (isCircle) {
            float radius = Math.min(height, width) * 1f / 2;
//            float radius = Math.min(height, width) * 1f / 2 - mStrokeWidth;
            mRadiusTopLeft = radius;
            mRadiusTopRight = radius;
            mRadiusBottomRight = radius;
            mRadiusBottomLeft = radius;
            setRadius();
        }
        if (mRectF != null) {
            mRectF.set(0, 0, mWidth, mHeight);
        }
        if (mStrokeRectF != null) {
//            mStrokeRectF.set(0, 0, width, height);
            mStrokeRectF.set((mStrokeWidth / 1.5f), (mStrokeWidth / 1.5f), width - mStrokeWidth / 1.5f, height - mStrokeWidth / 1.5f);
        }
    }

    public void preDraw(Canvas canvas) {
        canvas.save();
        canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);
//        if (mStrokeWidth > 0) {
//            float sx = (mWidth - 2 * mStrokeWidth) / mWidth;
//            float sy = (mHeight - 2 * mStrokeWidth) / mHeight;
//            // 缩小画布，使图片内容不被borders覆盖
//            canvas.scale(sx, sy, mWidth / 2.0f, mHeight / 2.0f);
//        }
    }

    public void drawPath(Canvas canvas) {
        mPaint.reset();
        mPath.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(mXfermode);
        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mPaint.setAntiAlias(true);
            mTempPath.reset();
            mTempPath.addRect(mRectF, Path.Direction.CCW);
            mTempPath.op(mPath, Path.Op.DIFFERENCE);
            canvas.drawPath(mTempPath, mPaint);
        } else {
            mPaint.setAntiAlias(true);
            canvas.drawPath(mPath, mPaint);
        }
        mPaint.setXfermode(null);
        canvas.restore();

        // draw stroke
        if (mStrokeWidth > 0) {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(mStrokeColor);

            if (mStrokeDottedLine) {
                mPaint.setPathEffect(new DashPathEffect(new float[]{mStrokeDottedLineIntervals, mStrokeDottedNullIntervals}, 1));
            }
            mPath.reset();
            mPath.addRoundRect(mStrokeRectF, mStrokeRadii, Path.Direction.CCW);
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
        if (mContext == null) {
            return;
        }
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadius(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = SizeUtils.dp2px(radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusTopRight = radiusPx;
        mRadiusBottomLeft = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadius(float radiusTopLeftDp, float radiusTopRightDp, float radiusBottomLeftDp, float radiusBottomRightDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopLeft = SizeUtils.dp2px(radiusTopLeftDp);
        mRadiusTopRight = SizeUtils.dp2px(radiusTopRightDp);
        mRadiusBottomLeft = SizeUtils.dp2px(radiusBottomLeftDp);
        mRadiusBottomRight = SizeUtils.dp2px(radiusBottomRightDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = SizeUtils.dp2px(radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusBottomLeft = radiusPx;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = SizeUtils.dp2px(radiusDp);
        mRadiusTopRight = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusTop(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = SizeUtils.dp2px(radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusTopRight = radiusPx;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusBottom(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = SizeUtils.dp2px(radiusDp);
        mRadiusBottomLeft = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusTopLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopLeft = SizeUtils.dp2px(radiusDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusTopRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopRight = SizeUtils.dp2px(radiusDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusBottomLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusBottomLeft = SizeUtils.dp2px(radiusDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setRadiusBottomRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusBottomRight = SizeUtils.dp2px(radiusDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setStrokeWidth(float widthDp) {
        if (mContext == null) {
            return;
        }
        mStrokeWidth = SizeUtils.dp2px(widthDp);
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setStrokeColor(int color) {
        mStrokeColor = color;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    public void setStrokeWidthColor(float widthDp, int color) {
        if (mContext == null) {
            return;
        }
        mStrokeWidth = SizeUtils.dp2px(widthDp);
        mStrokeColor = color;
        if (mView != null) {
            setRadius();
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }
}
