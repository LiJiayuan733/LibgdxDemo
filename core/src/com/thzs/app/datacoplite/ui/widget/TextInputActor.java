package com.thzs.app.datacoplite.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.FloatArray;
import com.thzs.app.datacoplite.core.Path;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;
import com.thzs.app.datacoplite.util.grapices.XmlPixmap;
/**
 * 未知的内存泄露 取消使用 有关 LazyBitmapFont TextChanged()
 * */
@Deprecated
public class TextInputActor extends Actor {
    public FloatArray sizes=new FloatArray();
    public boolean isFocus=false;
    public int FocusCursor=-1;
    public LazyBitmapFont font;
    private String defaultString = "";
    public XmlPixmap pixmap,pixmap2;
    public Rectangle range;
    //TODO: to finish tomorrow
    public TextInputActor(String defaultString){
        super();
        this.defaultString=defaultString;
        font= new LazyBitmapFont(20);
        pixmap=new XmlPixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888,Gdx.files.internal(Path.XML_PATH+"MoreShape.xml"),"background_rect");
        pixmap2=new XmlPixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888,Gdx.files.internal(Path.XML_PATH+"MoreShape.xml"),"background_rect_focus");
        range=new Rectangle();
        TextChanged();
    }
    public void CompleteFocusPointAndChange(float x,float y){
        float ix=0;
        for(int i=0;i<sizes.size;i++){
            ix+=sizes.get(i);
            if(i< sizes.size-1&&ix+sizes.get(i+1)>x&&ix<x){
                FocusCursor=i;
                break;
            }else {
                FocusCursor=sizes.size-1;
            }
        }
        if (FocusCursor == -1){
            return;
        }
        drawCursor();
    }
    public void drawCursor(){
        sizeChanged();
        pixmap.setColor(Color.BLACK);
        float ix=0;
        for(int i=0;i<=FocusCursor;i++){
            ix+=sizes.get(i);
        }
        pixmap.fillRectangle((int) ix, 0, 4, (int) getHeight());
    }
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        font= new LazyBitmapFont((int) getHeight());
        pixmap=new XmlPixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888,Gdx.files.internal(Path.XML_PATH+"MoreShape.xml"),"background_rect");
        pixmap2=new XmlPixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGB888,Gdx.files.internal(Path.XML_PATH+"MoreShape.xml"),"background_rect_focus");
        range.setSize(getWidth(),getHeight());
        TextChanged();
    }
    @Override
    protected void positionChanged() {
        super.positionChanged();
        range.setPosition(getX(),getY());
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        stage.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(range.contains(x,y)){
                    setFocus(true);
                    CompleteFocusPointAndChange(x-getX(),y-getY());
                }else {
                    setFocus(false);
                }
                return isFocus();
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(range.contains(x,y)){
                    setFocus(true);
                }else {
                    setFocus(false);
                }
            }
        });
    }
    public void TextChanged(){
        font.getCache().clear();
        font.getCache().addText(defaultString, 0, 0);
        sizes=font.getCache().getLayouts().get(0).runs.get(0).xAdvances;
    }
    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isFocus) {
            batch.draw(new Texture(pixmap), getX(), getY());
        }else {
            batch.draw(new Texture(pixmap2), getX(), getY());
        }
        font.draw(batch,defaultString,getX(),getY()+font.getAscent()+font.getCapHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
