package com.thzs.app.datacoplite.ui.view.nView.base;

import com.badlogic.gdx.Input;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.StartViewHandle;

public class StartView extends View {
    public StartViewHandle handle;
    @Override
    public void create() {
        handle=new StartViewHandle(this);
        //初始化窗口主体
        handle.ViewInit();
        //设置监听
        handle.ViewListener();
    }
    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode== Input.Keys.ESCAPE){
            remove();
        }
        return super.keyDown(keycode);
    }

    @Override
    public void act() {
        stage.act();
    }

    @Override
    public void onRemove() {
        //销毁资源
        handle.dispose();
        super.onRemove();
        //清理垃圾
        Runtime.getRuntime().gc();
    }

    @Override
    public String toString() {
        return "StartView";
    }
}
