package com.thzs.app.datacoplite.ui.view.nView.base;

import com.badlogic.gdx.Input;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.AIResultViewHandle;

public class AIResultView extends View {
    protected AIResultViewHandle handle;
    @Override
    public void create() {
        handle=new AIResultViewHandle(this);
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
        if(Input.Keys.ESCAPE==keycode){
            Views.addView(WhiteView.class);
            remove();
        }
        return super.keyDown(keycode);
    }
}
