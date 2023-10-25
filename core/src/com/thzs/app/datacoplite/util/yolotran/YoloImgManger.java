package com.thzs.app.datacoplite.util.yolotran;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;

public class YoloImgManger implements Disposable {
    public Pixmap pixmap;
    public YoloImgManger(int width,int height){
        pixmap=new Pixmap(width,height, Pixmap.Format.RGBA8888);
    }

    @Override
    public void dispose() {
        if (pixmap==null)
            return;
        pixmap.dispose();
        pixmap=null;
    }
}
