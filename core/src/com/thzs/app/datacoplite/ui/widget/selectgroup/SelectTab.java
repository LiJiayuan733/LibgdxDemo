package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.FloatArray;
import com.thzs.app.datacoplite.core.UI;

import javax.swing.*;

public class SelectTab extends Actor implements SelectGroupSettingSupport {
    public boolean FLAG_SHOW_FONT_SPLIT_LINE=false;
    public String text;
    public SelectBitmapFont font;
    public Texture textures;
    public SelectTab(String text){
        this.text = text;
        font = new SelectBitmapFont(30);
        textChanged();
        setHeight(font.font.getLineHeight());
    }
    boolean flagFocus=false;
    @Override
    public boolean isFlagFocus() {
        return flagFocus;
    }
    public void setText(String text){
        this.text = text;
        textChanged();
    }

    @Override
    public float getWidth() {
        return super.getWidth();
    }

    @Override
    protected void positionChanged() {
        textChanged();
    }

    @Override
    protected void sizeChanged() {
        textChanged();
    }
    public float font_x,font_y;
    public float offsetX=0,offsetY=0;
    public GlyphLayout glyphLayout;
    public void textChanged(){
        float font_height=font.font.getAscent()+font.font.getCapHeight();

        font_x=getX();
        font_y=getY()+font_height;

        font.font.getCache().clear();
        glyphLayout = font.font.getCache().addText(text, font_x, font_y);

        FloatArray fa=glyphLayout.runs.get(0).xAdvances;
        float font_width=glyphLayout.runs.get(0).width;

        offsetX=getWidth()/2-font_width/2;
        offsetY=getHeight()/2-font_height/2;

        float x=offsetX,y=0;
        Pixmap pixmap=new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor((Color) UIManager.get(UI.bgSelectGroupColor));
        pixmap.fill();
        pixmap.setColor(Color.RED);
        for(int i=0;i<fa.size;i++){
            x+=fa.get(i);
            if(FLAG_SHOW_FONT_SPLIT_LINE) {
                pixmap.drawLine((int) x, (int) y, (int) x, (int) (y + getHeight()));
            }
        }
        textures=new Texture(pixmap);
        pixmap.dispose();

        font.font.getCache().clear();
        font.font.getCache().addText(text, font_x+offsetX, font_y+offsetY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textures, getX(), getY(), getWidth(), getHeight());
        font.font.getCache().draw(batch);
    }

    @Override
    public void OnSetFocus(boolean flag) {
        this.flagFocus=flag;
        if(flag){
            font.font.setColor(Color.RED);
        }else{
            font.font.setColor(Color.BLACK);
        }
        font.font.getCache().clear();
        font.font.getCache().addText(text, font_x+offsetX, font_y+offsetY);
    }

    @Override
    public void SelectInit() {

    }
}
