package com.thzs.app.datacoplite.ui.view.nView.base;

import com.badlogic.gdx.Input;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.ServerControlViewHandle;

public class ServerControlView extends View {
    public ServerControlViewHandle handle;
    @Override
    public void create() {
        handle=new ServerControlViewHandle(this);
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
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode== Input.Keys.ESCAPE){
            remove();
        }
        return super.keyDown(keycode);
    }
}
