package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;


@Deprecated
public class YzSingleLineBitmapFont implements Disposable {
    public LazyBitmapFont font;
    public float FontHeight;    //height of font
    protected float x=0,y=0;
    public YzSingleLineBitmapFont(){
        font = new LazyBitmapFont(YzCenterControl.DEFAULT_FONT_SIZE);
        FontHeight=font.getAscent()+font.getCapHeight();
    }
    public YzSingleLineBitmapFont(int size){
        font = new LazyBitmapFont(size);
        FontHeight=font.getAscent()+font.getCapHeight();
    }
    public void setColor(Color color){
        font.setColor(color);
    }
    public void clear(){
        font.getCache().clear();
    }
    //文字数据
    public GlyphLayout glyphLayout;
    public String text="";
    /**
     * @param text 输入的文字
     * @param X 文字绘制的x坐标
     * @param Y 文字绘制的y坐标
     * */
    public void setText(String text,float X,float Y){
        this.x=X;this.y=Y;
        this.text=text;
        Y+=FontHeight;
        font.getCache().clear();
        glyphLayout = font.getCache().setText(text, X, Y);
    }
    public void setText(String text,float X,float Y,float targetWidth,int hAlign){
        this.x=X;this.y=Y;
        this.text=text;
        Y+=FontHeight;
        font.getCache().clear();
        glyphLayout = font.getCache().setText(text, X, Y,targetWidth,hAlign,true);
    }
    /**
     * need setPosition before calling
     * */
    @Deprecated
    public void setText(String text){
        float X=this.x,Y=this.y;
        clear();
        Y+=FontHeight;
        this.text=text;
        font.getCache().clear();
        glyphLayout = font.getCache().setText(text, X, Y);
    }
    /**
     * set the write position of text;
     * */
    public void setPosition(float X,float Y){
        this.x=X;this.y=Y;
        Y+=FontHeight;
        font.getCache().clear();
        glyphLayout=font.getCache().setText(text,X,Y);
    }
    public void draw(Batch batch,float alpha){
        font.getCache().draw(batch,alpha);
    }
    public float getWidth(){
        return glyphLayout.width;
    }
    public float getHeight(){
        return FontHeight+font.getAscent();
    }
    public void setSize(int height){
        font.dispose();
        font = new LazyBitmapFont(height);
        FontHeight=font.getAscent()+font.getCapHeight();
        setText(this.text);
    }
    public int getCharEndIndex(int runs){
        if(glyphLayout.runs.size==0){
            return -1;
        }
        FloatArray fa=glyphLayout.runs.get(runs).xAdvances;
        int c=-1;
        for(int i=1;i<fa.size;i++){
            if(fa.get(i)!=0){
                c++;
            }
        }
        return c;
    }
    public Vector2 getCharacterPosition(int runs,int index){
        if(index==-1||glyphLayout.runs.size==0){
            return new Vector2(this.x,this.y);
        }
        FloatArray fa=glyphLayout.runs.get(runs).xAdvances;
        float X=this.x;
        for(int i = 0; i<(Math.min(index + 2, fa.size)); i++){
            X+=fa.get(i);
        }
        return new Vector2(X,y);
    }
    @Override
    public void dispose() {
        clear();
        font.dispose();
    }
}
