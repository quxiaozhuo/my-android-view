package com.library.view.dialog;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.library.view.R;


/**
 * 底部DialogFragment
 */
public abstract class BottomDialogFragment<V extends ViewDataBinding> extends BaseDialogFragment<V> {

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setWindowAnimations(R.style.BottomDialog);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
