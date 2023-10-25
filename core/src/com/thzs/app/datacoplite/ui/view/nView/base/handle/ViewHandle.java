package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.thzs.app.datacoplite.ui.view.View;

public abstract class ViewHandle {
    protected View instance;
    public ViewHandle(View view){
        this.instance = view;
    }
    public abstract void ViewInit();
    public abstract void dispose();
    public abstract void ViewListener();
    public abstract void load();
    public abstract void save();
    public abstract String getName();
}
