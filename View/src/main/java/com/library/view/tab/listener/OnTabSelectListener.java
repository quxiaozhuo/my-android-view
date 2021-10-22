package com.library.view.tab.listener;

import android.view.View;

public interface OnTabSelectListener {
    void onTabSelect(View view, int index, int prePosition);

    void onTabReselect(View view, int position);

    boolean onInterruptSelect(View view, int position);
}
