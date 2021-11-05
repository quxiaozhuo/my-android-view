package com.library.view.dialog;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.library.view.R;


/**
 * 左边DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public abstract class LeftDialogFragment <V extends ViewDataBinding> extends BaseDialogFragment<V> {


    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.START);
        mWindow.setWindowAnimations(R.style.LeftDialog);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
