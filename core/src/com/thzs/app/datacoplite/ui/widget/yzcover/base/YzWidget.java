package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.core.Path;
import com.thzs.app.datacoplite.util.grapices.XmlPixmap;

import java.util.ArrayList;
import java.util.List;

public class YzWidget extends Actor implements YzCenterControl.YzControl, Disposable {
    protected boolean FGFocused=false;
    public List<Texture> SkinList=new ArrayList<>();
    public YzWidget() {
    }

    @Override
    public void CancelFocus(YzCenterControl.YzControl to) {
        FGFocused=false;
    }

    @Override
    public void SetFocus(YzCenterControl.YzControl from) {
        FGFocused=true;
        YzCenterControl.focusWidget=this;
    }

    @Override
    public boolean IsFocus() {
        return FGFocused;
    }

    /**
     * 会在Dispose()中自行清理存储
     * */
    public Texture loadSkin(int width, int height, FileHandle XmlFile, String ID){
        XmlPixmap pixmap=new XmlPixmap(width,height, Pixmap.Format.RGBA8888, XmlFile,ID);
        Texture skin=new Texture(pixmap);
        pixmap.dispose();
        SkinList.add(skin);
        return skin;
    }
    public void clearSkin(){
        for (Texture skin:SkinList) {
            skin.dispose();
        }
    }
    public Texture loadSkin(int width, int height, String XmlPath,String ID){
        return loadSkin(width,height, Gdx.files.internal(Path.XML_PATH+XmlPath),ID);
    }
    @Override
    public void dispose() {
        clearSkin();
    }
}
