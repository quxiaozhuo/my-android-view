package com.library.view.dialog;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.library.view.R;


/**
 * 右边DialogFragment
 */
public abstract class RightDialogFragment <V extends ViewDataBinding> extends BaseDialogFragment<V>  {


    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.END);
        mWindow.setWindowAnimations(R.style.RightDialog);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
