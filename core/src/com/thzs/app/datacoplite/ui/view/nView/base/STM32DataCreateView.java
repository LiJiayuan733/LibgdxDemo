package com.thzs.app.datacoplite.ui.view.nView.base;

import com.badlogic.gdx.Input;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.STM32DataCreateViewHandle;

public class STM32DataCreateView extends View {
    protected STM32DataCreateViewHandle handle;
    @Override
    public void create() {
        handle=new STM32DataCreateViewHandle(this);
        handle.ViewInit();
        handle.ViewListener();
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        handle.dispose();
    }

    @Override
    public void act() {
        stage.act();
    }
    public boolean keyDown(int keycode) {
        if(Input.Keys.ESCAPE==keycode){
            Views.addView(WhiteView.class);
            remove();
        }
        return super.keyDown(keycode);
    }

    @Override
    public String toString() {
        return "STM 32 数据生成界面";
    }
}
