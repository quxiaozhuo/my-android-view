package com.library.view.dialog;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.library.view.R;


/**
 * 顶部DialogFragment
 */
public abstract class TopDialogFragment<V extends ViewDataBinding> extends BaseDialogFragment<V> {


    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP);
        mWindow.setWindowAnimations(R.style.TopDialog);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }
}
