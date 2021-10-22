
package com.library.view.loadview.help;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;


/**
 * 用于切换布局,用一个新的布局覆盖在原布局之上
 *
 * @author LuckyJayce
 */
public class VaryViewHelperX {

    private IVaryViewHelper helper;
    private View view;

    public VaryViewHelperX(@NonNull View view) {
        super();
        this.view = view;
        ViewGroup group = (ViewGroup) view.getParent();
        LayoutParams layoutParams = view.getLayoutParams();
        FrameLayout frameLayout = new FrameLayout(view.getContext());
        if (group != null) {
            int index = group.indexOfChild(view);
            group.removeView(view);
            frameLayout.setBackground(view.getBackground());
            group.addView(frameLayout, index, layoutParams);
//            group.addView(frameLayout,  layoutParams);
        }
        frameLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        helper = new VaryViewHelper(view);
    }

    public View getCurrentLayout() {
        return view;
    }

    public void restoreView() {
        helper.restoreView();
    }

    public synchronized void showLayout(@NonNull View view) {
        helper.showLayout(view);
    }

    public View inflate(int layoutId) {
        return helper.inflate(layoutId);
    }

    public Context getContext() {
        return helper.getContext();
    }


    public View getView() {
        return view;
    }

    public void release() {
        if (helper != null) {
            helper.release();
        }
    }
}
