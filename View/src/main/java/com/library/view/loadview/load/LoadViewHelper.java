
package com.library.view.loadview.load;

import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.library.view.loadview.help.OnLoadViewListener;
import com.library.view.loadview.help.VaryViewHelperX;


/**
 * Created by yangc on 2017/4/30.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 */
public class LoadViewHelper implements OnClickListener {
    private VaryViewHelperX helper;
    private View addLoad;
    private View loadError;
    private View loadEmpty;
    private View loadIng;
    private OnLoadViewListener listener;

    public LoadViewHelper(@NonNull View view) {
        this(new VaryViewHelperX(view));
    }

    private LoadViewHelper(@Nullable VaryViewHelperX helper) {
        super();
        this.helper = helper;
    }


    /****
     * 显示错误页
     * 默认
     ***/
    public void showError() {
        helper.showLayout(loadError);
    }


    /**
     * 显示空白页
     *
     *****/
    public void showEmpty() {
        helper.showLayout(loadEmpty);
    }


    /***
     * 没有加载文本
     **/
    public void showLoading() {
        helper.showLayout(loadIng);
    }


    public void showContent() {
        helper.restoreView();
    }

    @NonNull
    public View getLoadError() {
        return loadError;
    }

    public void setLoadError(@NonNull View loadError) {
        this.loadError = loadError;
        this.loadError.setOnClickListener(this);
    }

    public void setLoadError(@LayoutRes int loadErrorRes) {
        this.loadError = helper.inflate(loadErrorRes);
        this.loadError.setOnClickListener(this);
    }


    public void addView(View addLoad) {
        this.addLoad = addLoad;
    }

    public void showAddView(){
        helper.showLayout(addLoad);
    }


    @NonNull
    public View getLoadEmpty() {
        return loadEmpty;
    }

    public void setLoadEmpty(@NonNull View loadEmpty) {
        this.loadEmpty = loadEmpty;
        //        loadEmpty.setOnClickListener(this);
    }

    public void setLoadEmpty(@LayoutRes int loadEmptyRes) {
        this.loadEmpty = helper.inflate(loadEmptyRes);
        //        loadEmpty.setOnClickListener(this);
    }

    @NonNull
    public View getLoadIng() {
        return loadIng;
    }

    public void setLoadIng(@LayoutRes int loadIngRes) {
        this.loadIng = helper.inflate(loadIngRes);
    }

    public void setListener(@NonNull OnLoadViewListener listener) {
        this.listener = listener;
    }

    public void onDestroy() {
        if (helper != null) {
            helper.release();
        }
        loadError = null;
        loadEmpty = null;
        loadIng = null;
        addLoad = null;
        listener = null;
        helper = null;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRetryClick();
        }
    }


}
