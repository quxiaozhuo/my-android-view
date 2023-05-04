package com.library.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.library.view.R;


/**
 * DialogFragment 实现沉浸式的基类
 */
public abstract class BaseDialogFragment<V extends ViewDataBinding> extends DialogFragment {

    protected Activity mActivity;
    protected Window mWindow;
    public Integer[] mWidthAndHeight;
    protected V mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
        mWidthAndHeight = getWidthAndHeight(mWindow);
    }

    public Integer[] getWidthAndHeight(Window window) {
        Integer[] integer = new Integer[2];
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        integer[0] = dm.widthPixels;
        integer[1] = dm.heightPixels;
        return integer;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.bind(inflater.inflate(setLayoutId(), container, false));
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWidthAndHeight = getWidthAndHeight(mWindow);
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
    }


    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * view与数据绑定
     */
    protected void initView() {
    }


    protected void onClick(View view) {

    }

    public void show(@NonNull FragmentManager manager) {
        show(manager, getClass().getSimpleName());
    }

}


