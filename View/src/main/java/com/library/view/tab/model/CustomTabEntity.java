package com.library.view.tab.model;


public class CustomTabEntity implements CustomTabModel {
    private String title;
    private int selectedIcon = 0;
    private int unSelectedIcon = 0;

    public CustomTabEntity(String title) {
        this.title = title;
    }

    public CustomTabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public void setTabTitle(String title) {
        this.title = title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}