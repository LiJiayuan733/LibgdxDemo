package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzCenterControl;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;

public class YzLabel extends YzWidget {
    protected LazyBitmapFont font;
    protected Texture backgroundTexture;
    protected String text="";
    protected Color fontColor=Color.WHITE;
    protected boolean FGWrap=true;
    protected int fontSize;
    public float FontPad=10;
    public int textLine=4;
    public YzLabel(){
        font = new LazyBitmapFont(YzCenterControl.DEFAULT_FONT_SIZE);
        fontSize=YzCenterControl.DEFAULT_FONT_SIZE;
        setSize(1,(font.getAscent()+font.getCapHeight()+FontPad*2)*textLine);
        setColor(Color.WHITE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(backgroundTexture!=null)
            batch.draw(backgroundTexture,getX(),getY(),getWidth(),getHeight());
        if(glyphLayout!=null)
            font.getCache().draw(batch,parentAlpha);
    }
    protected GlyphLayout glyphLayout=null;
    public void setText(String text){
        this.text = text;
        font.getCache().clear();
        font.setColor(fontColor);
        glyphLayout=font.getCache().setText(text,getX()+FontPad,getY()+getHeight()-FontPad+font.getDescent(),getWidth()-FontPad*2, Align.left,FGWrap);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if(getWidth()>0){
            setText(text);
            setColor(getColor());
        }
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        setText(text);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        Pixmap pixmap=new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        backgroundTexture=new Texture(pixmap);
        pixmap.dispose();
    }
    public void setFontColor(Color color){
        fontColor=color;
        setText(text);
    }
    @Override
    public void dispose() {
        super.dispose();
        glyphLayout=null;
        font.dispose();
        backgroundTexture.dispose();
    }
}
