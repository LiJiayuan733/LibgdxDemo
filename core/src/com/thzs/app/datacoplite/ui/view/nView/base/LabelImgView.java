package com.thzs.app.datacoplite.ui.view.nView.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.handle.LabelImgViewHandle;
import com.thzs.app.datacoplite.util.yolotran.YoloV5TranToolV2;

public class LabelImgView extends View {
    protected LabelImgViewHandle handle;
    public YoloV5TranToolV2 yoloV5TranTool;
    public boolean FGYoloV5=false;
    @Override
    public void create() {
        handle = new LabelImgViewHandle(this);
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
        if(FGYoloV5&&yoloV5TranTool!=null){
            yoloV5TranTool.update(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        handle.dispose();
        if(yoloV5TranTool!=null)
            yoloV5TranTool.dispose();
        System.gc();
    }
    @Override
    public String toString() {
        return "LabelImageView";
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
