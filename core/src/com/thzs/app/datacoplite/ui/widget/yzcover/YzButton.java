package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzSingleLineBitmapFont;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;

public class YzButton extends YzWidget {
    public static final String SKIN_HOVER = "hover";
    public static final String SKIN_PRESS_ED = "press_ed";
    public static final String SKIN_PRESS_UN = "press_un";

    protected String text;
    protected YzSingleLineBitmapFont font;
    protected Texture Press_ed,Press_un,Hover;
    protected boolean FlagOver=false;
    protected boolean FlagPress=false;
    public YzButton(String text){
        //加载画笔
        font =new YzSingleLineBitmapFont();
        //设置文字
        setText(text);
        //设置Table
        setTouchable(Touchable.enabled);
        //设置背景
        Press_ed=loadSkin((int)getWidth(),(int)getHeight(),"YzButtonSkin.xml",SKIN_PRESS_ED);
        Press_un=loadSkin((int)getWidth(),(int)getHeight(), "YzButtonSkin.xml",SKIN_PRESS_UN);
        Hover=loadSkin((int)getWidth(),(int)getHeight(), "YzButtonSkin.xml",SKIN_HOVER);
        addListener(new ClickListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                FlagPress=true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                FlagPress=false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                FlagOver=true;
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                FlagOver=false;
                super.exit(event, x, y, pointer, toActor);
            }
        });
        font.setPosition(getX()+paw/2,getY()+pah/2);
    }
    public void setText(String text){
        font.setText(text, getX(), getY());
    }

    public YzSingleLineBitmapFont getFont(){
        return font;
    }
    public int pah=10,paw=16;
    @Override
    protected void positionChanged() {
        super.positionChanged();
        font.setPosition(getX()+paw/2,getY()+pah/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(FlagPress){
            batch.draw(Press_ed,getX(),getY(),getWidth(),getHeight());
        }else if(FlagOver){
            batch.draw(Hover,getX(),getY(),getWidth(),getHeight());
        }else{
            batch.draw(Press_un,getX(),getY(),getWidth(),getHeight());
        }
        font.draw(batch,parentAlpha);
    }
    @Override
    public float getHeight() {
        return Math.max(super.getHeight(),font.getHeight()+pah);
    }

    @Override
    public float getWidth() {
        return Math.max(super.getWidth(),font.getWidth()+paw);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        font.setColor(color);
    }
    @Override
    public void setColor(float r, float g, float b, float a) {
        super.setColor(r, g, b, a);
        font.setColor(new Color(r,g,b,a));
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }
}
