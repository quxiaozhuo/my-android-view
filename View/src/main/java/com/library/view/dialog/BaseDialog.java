package com.library.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.library.view.R;


/**
 * qu_xi
 * 2020-01-17
 */
public abstract class BaseDialog<V extends ViewDataBinding> extends AlertDialog {

    public BaseDialog(Context context) {
        this(context, R.style.dialog_style);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        mBinding = DataBindingUtil.bind(View.inflate(
                context, getLayoutId(), null
        ));
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected V mBinding;
    protected int gravity = Gravity.CENTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置屏幕的宽度
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(gravity);
        setContentView(mBinding.getRoot());
    }

    protected void onClick(View v) {
    }
}
