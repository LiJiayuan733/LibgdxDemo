package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzCenterControl;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzSingleLineBitmapFont;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;

@Deprecated
public class YzEditArea extends YzWidget {
    public static final String SKIN_INDEX = "index";
    public int index=0;
    public int StartIndex=-1,EndIndex=0;
    protected YzSingleLineBitmapFont font;
    protected String text;
    protected Texture cursorTexture;
    public YzEditArea (String initText){
        font =new YzSingleLineBitmapFont(YzCenterControl.DEFAULT_FONT_SIZE);
        font.setColor(YzCenterControl.WINDOW_EDITAREA_FONT_COLOR);
        setText(initText);
        EndIndex=(index=getCharEndIndex(0));
        cursorTexture=loadSkin(10,(int) getHeight(), "YzEditAreaSkin.xml",SKIN_INDEX);
        //TODO: 完成输入框
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
    public String getText(){
        return text;
    }
    public GlyphLayout glyphLayout;
    public void setText(String text){
        this.text = text;
        font.setText(text);
        EndIndex= font.getCharEndIndex(0);
    }
    public void delChar(int index){
        if(index==-1||text.equals("")){
            return;
        }
        StringBuffer sb = new StringBuffer(text);
        sb.deleteCharAt(index);
        this.index-=this.index==0?0:1;
        setText(sb.toString());
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch,parentAlpha);
        Vector2 vector=font.getCharacterPosition(0,index);
        batch.draw(cursorTexture,vector.x,vector.y);
    }

    @Override
    protected void sizeChanged() {
        font.setSize((int) getHeight());
        cursorTexture=loadSkin(10,(int) getHeight(), "YzEditAreaSkin.xml",SKIN_INDEX);
        super.sizeChanged();
    }

    @Override
    protected void positionChanged() {
        font.setPosition(getX(),getY());
        super.positionChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }

    @Override
    public float getHeight() {
        return font.getHeight();
    }
}
