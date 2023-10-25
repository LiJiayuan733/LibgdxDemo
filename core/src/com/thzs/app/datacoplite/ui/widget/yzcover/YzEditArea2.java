package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FloatArray;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzCenterControl;

public class YzEditArea2 extends YzLabel{
    public static final String SKIN_INDEX = "index";
    public static final String SKIN_BORDER_DEFAULT = "border_default";
    public Texture cursor,border;
    public int index=0;
    public int StartIndex=-1,EndIndex=0;
    public Vector2 cursorPosition;
    public boolean showCursor=true;

    public YzEditArea2(String initText) {
        //设置不换行
        FGWrap=false;
        //设置笔刷
        font = new LazyBitmapFont(YzCenterControl.DEFAULT_FONT_SIZE);
        //设置大小
        setSize(100,(int) (font.getAscent()+font.getCapHeight()+FontPad*2));
        //加载皮肤
        loadSkin();
        //设置背景为白色
        setColor(Color.WHITE);
        //设置画笔为棕色
        setFontColor(Color.BROWN);
        //设置文本
        setText(initText);
        //添加监听
        initListener();
    }
    public void loadSkin(){
        //加载光标样式
        cursor=loadSkin(5, (int) (font.getAscent()+font.getCapHeight()+FontPad), "YzEditAreaSkin.xml",SKIN_INDEX);
        border=loadSkin((int) getWidth(), (int) getHeight(), "YzEditAreaSkin.xml",SKIN_BORDER_DEFAULT);
    }
    public void setCursor(int index){
        if(index==this.index)
            return;
        this.index=index;
        completeCursor();
    }
    @Override
    protected void positionChanged() {
        super.positionChanged();
        completeCursor();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        completeCursor();
        clearSkin();
        loadSkin();
    }

    @Override
    public void setText(String text) {
        this.text = text;
        font.getCache().clear();
        font.setColor(fontColor);
        glyphLayout=font.getCache().setText(text,getX()+FontPad,getY()+getHeight()-FontPad+font.getDescent(),0,text.length(),getWidth()-FontPad*2,Align.left,FGWrap,"");
        //设置最大索引
        EndIndex=getCharEndIndex(0);
        //计算光标
        completeCursor();
    }

    public void completeCursor(){
        Vector2 v=getCharacterPosition(0,index);
        cursorPosition=new Vector2(v.x,v.y);
    }
    float cursorTemp=0f,LeftTemp=-1f,RightTemp=-1f,BackSpaceTemp=-1f;
    @Override
    public void act(float delta) {
        if(FGFocused) {
            cursorTemp += delta;
            if (cursorTemp >= 0.5f) {
                showCursor = !showCursor;
                cursorTemp = 0f;
            }
        }
        if(LeftTemp!=-1f){
            LeftTemp+=delta;
            if(LeftTemp>=0.60f){
                leftIndex();
                LeftTemp=0.5f;
            }
        }
        if(RightTemp!=-1){
            RightTemp+=delta;
            if(RightTemp>=0.60f){
                rightIndex();
                RightTemp=0.5f;
            }
        }
        if(BackSpaceTemp!=-1){
            BackSpaceTemp+=delta;
            if(BackSpaceTemp>=0.60f){
                del();
                BackSpaceTemp=0.5f;
            }
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(cursor!=null&&showCursor&&FGFocused)
            batch.draw(cursor, cursorPosition.x,cursorPosition.y);
        if(border!=null&&FGFocused)
            batch.draw(border,getX(),getY());
    }
    public Vector2 getCharacterPosition(int runs,int index){

        float offsetX=getX()+FontPad,offsetY=getY()+FontPad*0.5f;

        if(index==-1||glyphLayout.runs.size==0){
            return new Vector2(offsetX,offsetY);
        }
        FloatArray fa=glyphLayout.runs.get(runs).xAdvances;
        float X=offsetX;
        for(int i = 0; i<(Math.min(index + 2, fa.size)); i++){
            X+=fa.get(i);
        }
        return new Vector2(X,offsetY);
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
    public void leftIndex(){
        index=Math.max(index-1,StartIndex);
        completeCursor();
        showCursor=true;
    }
    public void rightIndex(){
        index=Math.min(index+1,EndIndex);
        completeCursor();
        showCursor=true;
    }
    public void del(){
        if(index==-1||text.equals("")){
            return;
        }
        StringBuilder sb = new StringBuilder(text);
        sb.deleteCharAt(index);
        setText(sb.toString());
        leftIndex();
    }
    public void add(char character){
        StringBuilder sb = new StringBuilder(text);
        sb.insert(index+1,character);
        setText(sb.toString());
        rightIndex();
    }
    public Integer getPos2Index(int runs,float x){
        if(glyphLayout.runs.size==0){
            return StartIndex;
        }
        FloatArray fa=glyphLayout.runs.get(runs).xAdvances;
        float X=getX(),temp_X;
        for(int i = 0; i<fa.size; i++){
            temp_X=X;
            X+=fa.get(i);
            if(X>=x){
                return X-x>x-temp_X?i-2:i-1;
            }
        }
        return EndIndex;
    }
    public void initListener(){
        addListener(new ClickListener(){
            public int startIndex=0,nowIndex;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startIndex=getPos2Index(0,x);
                setCursor(startIndex);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                nowIndex=getPos2Index(0,x);
                setCursor(nowIndex);
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                nowIndex=getPos2Index(0,x);
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(IsFocus()){
                    switch (keycode){
                        case Input.Keys.LEFT:
                            LeftTemp=0f;
                            return true;
                        case Input.Keys.RIGHT:
                            RightTemp=0f;
                            return true;
                        case Input.Keys.BACKSPACE:
                            BackSpaceTemp=0f;
                            return true;
                    }
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(IsFocus()){
                    switch (keycode){
                        case Input.Keys.LEFT:
                            if(LeftTemp<0.5f){
                                leftIndex();
                            }
                            LeftTemp=-1f;
                            return true;
                        case Input.Keys.RIGHT:
                            if(RightTemp<0.5f){
                                rightIndex();
                            }
                            RightTemp=-1f;
                            return true;
                        case Input.Keys.BACKSPACE:
                            if (BackSpaceTemp<0.5f){
                                del();
                            }
                            BackSpaceTemp=-1f;
                            return true;
                        case Input.Keys.TAB:
                            setText(Gdx.app.getClipboard().getContents());
                            return true;
                    }
                }
                return false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (character == 8) {
                    return IsFocus();
                }
                add(character);
                return IsFocus();
            }
        });
    }

    @Override
    public void CancelFocus(YzCenterControl.YzControl to) {
        super.CancelFocus(to);
        getStage().setKeyboardFocus(null);
    }

    @Override
    public void SetFocus(YzCenterControl.YzControl from) {
        super.SetFocus(from);
        getStage().setKeyboardFocus(this);
        showCursor=true;
    }
    public String getText() {
        return text;
    }
    /**
     * will return a value mean that showing all context of actor necessary width.
     * */
    public float getAdaptedWidth(){
        GlyphLayout gl=font.getCache().setText(text,getX()+FontPad,getY()+getHeight()-FontPad+font.getDescent(),0,text.length(),getWidth()-FontPad*2,Align.left,FGWrap);
        float aw=gl.width+FontPad*2;
        setText(text);
        return aw;
    }
}
