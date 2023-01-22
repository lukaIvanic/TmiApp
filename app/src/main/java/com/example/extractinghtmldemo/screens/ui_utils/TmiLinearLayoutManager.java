package com.example.extractinghtmldemo.screens.ui_utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class TmiLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public TmiLinearLayoutManager(Context context) {
        super(context);
    }

    public TmiLinearLayoutManager(Context context, boolean isScrollEnabled) {
        super(context);
        this.isScrollEnabled = isScrollEnabled;
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}