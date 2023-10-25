package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;

public class YzCheckButton extends YzWidget {
    public interface YzCheckButtonListener{
        public void onCheckedChanged(boolean checked);
    }
    public static final String SKIN_CHECK_BUTTON_DEFAULT = "default";
    public static final String SKIN_CHECK_BUTTON_CHECKED = "checked";
    protected Texture Skin_Default;
    protected Texture Skin_Checked;
    protected boolean checked=false;
    protected YzCheckButtonListener listener=null;

    public YzCheckButton(){
        setSize(20,20);
        initSkin();
        initListener();
    }
    public YzCheckButton(float size){
        setSize(size,size);
        initSkin();
        initListener();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if(getWidth()!=getHeight()){
            float large=Math.max(getHeight(),getWidth());
            setSize(large,large);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (checked){
            batch.draw(Skin_Checked,getX(),getY(),getWidth(),getHeight());
        }else {
            batch.draw(Skin_Default,getX(),getY(),getWidth(),getHeight());
        }
    }

    protected void initSkin(){
        Skin_Default = loadSkin((int) getWidth(), (int) getHeight(),"YzCheckButtonSkin.xml",SKIN_CHECK_BUTTON_DEFAULT);
        Skin_Checked = loadSkin((int) getWidth(), (int) getHeight(),"YzCheckButtonSkin.xml",SKIN_CHECK_BUTTON_CHECKED);
    }
    public boolean isChecked(){
        return checked;
    }
    public void setYzCheckButtonListener(YzCheckButtonListener listener){
        this.listener = listener;
    }
    protected void initListener(){
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checked=!checked;
                if (listener!=null)
                    listener.onCheckedChanged(checked);
                super.clicked(event, x, y);
            }
        });
    }
    public void setCheck(boolean check){
        this.checked=check;
    }
}
