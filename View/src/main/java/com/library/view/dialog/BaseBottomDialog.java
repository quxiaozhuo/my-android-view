package com.library.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import androidx.databinding.ViewDataBinding;

import com.library.view.R;


public abstract class BaseBottomDialog<V extends ViewDataBinding> extends BaseDialog<V> {
    public BaseBottomDialog(Context context) {
        super(context, R.style.bottom_dialog_style);
    }

    public BaseBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gravity = Gravity.BOTTOM;
        super.onCreate(savedInstanceState);
    }
}
