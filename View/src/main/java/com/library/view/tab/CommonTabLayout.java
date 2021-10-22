package com.library.view.tab;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.library.view.R;
import com.library.view.tab.listener.OnTabSelectListener;
import com.library.view.tab.model.CustomTabModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 没有继承HorizontalScrollView不能滑动,对于ViewPager无依赖
 */
public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private final ArrayList<CustomTabModel> mTabEntityS = new ArrayList<>();
    private final LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private final Rect mIndicatorRect = new Rect();
    private final GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private final Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;
    private int mIndicatorGravity;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextSize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    /**
     * icon
     */
    private boolean mIconVisible;
    private int mIconGravity;
    private float mIconWidth;
    private float mIconHeight;
    private float mIconMargin;


    /**
     * anim
     */
    private final ValueAnimator mValueAnimator;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator(1.5f);


    public CommonTabLayout(Context context) {
        this(context, null, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);
        obtainAttributes(context, attrs);

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastP, mCurrentP);
        mValueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);
        mIndicatorStyle = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_style, 0);
        mIndicatorColor = ta.getColor(R.styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_height,
                SizeUtils.dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_width, SizeUtils.dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_corner_radius, SizeUtils.dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_left, SizeUtils.dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_top, SizeUtils.dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_right, SizeUtils.dp2px(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_bottom, SizeUtils.dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_anim_enable, true);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_bounce_enable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_anim_duration, -1);
        mIndicatorGravity = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_gravity, Gravity.BOTTOM);

        mUnderlineColor = ta.getColor(R.styleable.CommonTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_underline_height, SizeUtils.dp2px(0));
        mUnderlineGravity = ta.getInt(R.styleable.CommonTabLayout_tl_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.CommonTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_width, SizeUtils.dp2px(0));
        mDividerPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_padding, SizeUtils.dp2px(12));

        mTextSize = ta.getDimension(R.styleable.CommonTabLayout_tl_textsize, SizeUtils.sp2px(13f));
        mTextSelectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        mTextUnselectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
        mTextBold = ta.getInt(R.styleable.CommonTabLayout_tl_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.CommonTabLayout_tl_textAllCaps, false);

        mIconVisible = ta.getBoolean(R.styleable.CommonTabLayout_tl_iconVisible, true);
        mIconGravity = ta.getInt(R.styleable.CommonTabLayout_tl_iconGravity, Gravity.TOP);
        mIconWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_iconWidth, SizeUtils.dp2px(0));
        mIconHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_iconHeight, SizeUtils.dp2px(0));
        mIconMargin = ta.getDimension(R.styleable.CommonTabLayout_tl_iconMargin, SizeUtils.dp2px(2.5f));

        mTabSpaceEqual = ta.getBoolean(R.styleable.CommonTabLayout_tl_tab_space_equal, true);
        mTabWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_width, SizeUtils.dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? SizeUtils.dp2px(0) : SizeUtils.dp2px(10));

        ta.recycle();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position) {
        View tabView;
        if (mIconGravity == Gravity.LEFT) {
            tabView = View.inflate(getContext(), R.layout.layout_tab_left, null);
        } else if (mIconGravity == Gravity.RIGHT) {
            tabView = View.inflate(getContext(), R.layout.layout_tab_right, null);
        } else if (mIconGravity == Gravity.BOTTOM) {
            tabView = View.inflate(getContext(), R.layout.layout_tab_bottom, null);
        } else {
            tabView = View.inflate(getContext(), R.layout.layout_tab_top, null);
        }
        tabView.setTag(position);
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(mTabEntityS.get(position).getTabTitle());
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mTabEntityS.get(position).getTabUnselectedIcon());

        tabView.setOnClickListener(this::onClick);

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }

    private void onClick(View v) {
        int position = mTabsContainer.indexOfChild(v);
        if (position == -1 || mListener == null) {
            return;
        }
        if (mListener.onInterruptSelect(this,position)) {
            return;
        }
        if (mCurrentTab != position) {
            int prePosition = mCurrentTab;
            setCurrentTab(position);
            mListener.onTabSelect(this,position, prePosition);
        } else {
            mListener.onTabReselect(this,position);
        }
    }

    public void updateTabText() {
        for (int position = 0; position < mTabCount; position++) {
            View tabView = mTabsContainer.getChildAt(position);
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setText(mTabEntityS.get(position).getTabTitle());
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            iv_tab_icon.setImageResource(mTabEntityS.get(position).getTabUnselectedIcon());
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
//            tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            if (mTextAllCaps) {
                tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
            }

            if (mTextBold == TEXT_BOLD_BOTH) {
                tv_tab_title.getPaint().setFakeBoldText(true);
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tv_tab_title.getPaint().setFakeBoldText(false);
            } else if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tv_tab_title.getPaint().setFakeBoldText(i == mCurrentTab);
            }

            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            if (mIconVisible) {
                iv_tab_icon.setVisibility(View.VISIBLE);
                CustomTabModel tabEntity = mTabEntityS.get(i);
                iv_tab_icon.setImageResource(i == mCurrentTab ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
                LinearLayout.LayoutParams lp;
                if (tabEntity.getTabSelectedIcon() == 0 && tabEntity.getTabUnselectedIcon() == 0) {
                    lp = new LinearLayout.LayoutParams(
                            0, 0);
                } else {
                    lp = new LinearLayout.LayoutParams(
                            mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
                            mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
                }
                if (mIconGravity == Gravity.LEFT) {
                    lp.rightMargin = (int) mIconMargin;
                } else if (mIconGravity == Gravity.RIGHT) {
                    lp.leftMargin = (int) mIconMargin;
                } else if (mIconGravity == Gravity.BOTTOM) {
                    lp.topMargin = (int) mIconMargin;
                } else {
                    lp.bottomMargin = (int) mIconMargin;
                }

                iv_tab_icon.setLayoutParams(lp);
            } else {
                iv_tab_icon.setVisibility(View.GONE);
            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            CustomTabModel tabEntity = mTabEntityS.get(i);
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tab_title.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    private void calcOffset() {
        if (mCurrentTab != -1) {
            final View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
            mCurrentP.left = currentTabView.getLeft();
            mCurrentP.right = currentTabView.getRight();
        }
        if (mLastTab != -1) {
            final View lastTabView = mTabsContainer.getChildAt(this.mLastTab);
            mLastP.left = lastTabView.getLeft();
            mLastP.right = lastTabView.getRight();
        }
//        Log.d("AAA", "mLastP--->" + mLastP.left + "&" + mLastP.right);
//        Log.d("AAA", "mCurrentP--->" + mCurrentP.left + "&" + mCurrentP.right);
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP);
            if (mIndicatorBounceEnable) {
                mValueAnimator.setInterpolator(mInterpolator);
            }

            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = mIndicatorBounceEnable ? 500 : 250;
            }
            mValueAnimator.setDuration(mIndicatorAnimDuration);
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        if (mCurrentTab == -1) {
            return;
        }
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();
        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        if (mIndicatorWidth > 0) { //indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mCurrentTab == -1) {
            return;
        }
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;

        if (mIndicatorWidth > 0) {   //indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = p.left + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
        invalidate();
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line
        if (mIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }


        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            } else {

            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }

                mIndicatorDrawable.setColor(mIndicatorColor);
                mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                        (int) mIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                        (int) (mIndicatorMarginTop + mIndicatorHeight));
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        } else {
               /* mRectPaint.setColor(mIndicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + mIndicatorRect.left, getHeight() - mIndicatorHeight,
                        mIndicatorRect.right + getPaddingLeft(), getHeight(), mRectPaint);*/

            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor);
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    public void setNewTabs(List<CustomTabModel> tabEntitys) {
        if (tabEntitys == null || tabEntitys.size() == 0) {
            throw new IllegalStateException("TabEntitys can not be NULL or EMPTY !");
        }
        this.mTabEntityS.clear();
        this.mTabEntityS.addAll(tabEntitys);
        this.mTabCount = mTabEntityS.size();
        notifyDataSetChanged();
    }

    public void addTabData(CustomTabModel tabEntity) {
        if (tabEntity == null) {
            throw new IllegalStateException("TabEntitys can not be NULL or EMPTY !");
        }

        this.mTabEntityS.add(tabEntity);
        addTab(mTabCount);
        this.mTabCount = mTabEntityS.size();
    }

    public ArrayList<CustomTabModel> getTabEntityList() {
        return mTabEntityS;
    }


    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        for (int i = 0; i < mTabCount; i++) {
            addTab(i);
        }
        updateTabStyles();
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        mLastTab = this.mCurrentTab;
        this.mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (currentTab == -1) {
            return;
        }
        if (mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPadding = SizeUtils.dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = SizeUtils.dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = SizeUtils.dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = SizeUtils.dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = SizeUtils.dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = SizeUtils.dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = SizeUtils.dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = SizeUtils.dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = SizeUtils.dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.mIndicatorAnimDuration = indicatorAnimDuration;
    }

    public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.mIndicatorAnimEnable = indicatorAnimEnable;
    }

    public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.mIndicatorBounceEnable = indicatorBounceEnable;
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = SizeUtils.dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = SizeUtils.dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.mDividerPadding = SizeUtils.dp2px(dividerPadding);
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.mTextSize = SizeUtils.sp2px(textSize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setIconVisible(boolean iconVisible) {
        this.mIconVisible = iconVisible;
        updateTabStyles();
    }

    public void setIconGravity(int iconGravity) {
        this.mIconGravity = iconGravity;
        notifyDataSetChanged();
    }

    public void setIconWidth(float iconWidth) {
        this.mIconWidth = SizeUtils.dp2px(iconWidth);
        updateTabStyles();
    }

    public void setIconHeight(float iconHeight) {
        this.mIconHeight = SizeUtils.dp2px(iconHeight);
        updateTabStyles();
    }

    public void setIconMargin(float iconMargin) {
        this.mIconMargin = SizeUtils.dp2px(iconMargin);
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }


    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public long getIndicatorAnimDuration() {
        return mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return mIndicatorBounceEnable;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public int getIconGravity() {
        return mIconGravity;
    }

    public float getIconWidth() {
        return mIconWidth;
    }

    public float getIconHeight() {
        return mIconHeight;
    }

    public float getIconMargin() {
        return mIconMargin;
    }

    public boolean isIconVisible() {
        return mIconVisible;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }


    class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
    }

}
