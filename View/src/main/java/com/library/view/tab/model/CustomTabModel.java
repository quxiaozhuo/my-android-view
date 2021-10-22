package com.library.view.tab.model;


import androidx.annotation.DrawableRes;

public interface CustomTabModel {
    String getTabTitle();

    void setTabTitle(String title);

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}