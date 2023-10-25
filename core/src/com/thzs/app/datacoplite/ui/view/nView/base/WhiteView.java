package com.thzs.app.datacoplite.ui.view.nView.base;

import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.WhiteViewHandle;

public class WhiteView extends View {
    public WhiteViewHandle handle;

    @Override
    public void create() {
        handle=new WhiteViewHandle(this);
        handle.ViewInit();
        handle.ViewListener();
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void act() {
        stage.act();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        handle.dispose();
        Runtime.getRuntime().gc();
    }

    @Override
    public String toString() {
        return "WhiteView";
    }
}
