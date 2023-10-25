package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.UI;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzCenterControl;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzSingleLineBitmapFont;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;
import com.thzs.app.datacoplite.util.input.InputProcessorEx;
import com.thzs.app.datacoplite.util.position.PositionConvert;

import javax.swing.*;
/**
 * 窗口上标题 <br/>
 * Type default is horizontal
 * 最好在最后添加
 * */
public class YzWindowTopBar extends YzWidget {
    enum State{
        MOUSE_ON_BUTTON3,MOUSE_ON_BUTTON2,MOUSE_ON_BUTTON1,MOUSE_NOT_ON,MOUSE_ON_TOP_BAR
    }
    public static final int TYPE_HORIZONTAL = 1;
    public static final int TYPE_VERTICAL = 2;
    public static float barHeightPercent = 0.04f;
    public static float barWidthPercent = 0.06f;
    //显示类型
    public int Type=TYPE_HORIZONTAL;
    public Pixmap tempPixmap;
    public Texture tempTexture;
    public Texture button1_un,button1_on,button2_un,button2_on;
    public Color bgColor;
    //used by Method::createBg()
    public boolean SizeChangedFlag=true;
    public State state=State.MOUSE_NOT_ON;
    //button1Rect 为关闭按钮 button2Rect 为最小化按钮 topBar为框体
    public View viewBody;
    public Rectangle button1Rect=new Rectangle(0,0,0,0),button2Rect=new Rectangle(0,0,0,0),topBar=new Rectangle(0,0,0,0);
    public Rectangle iconRect=new Rectangle(0,0,0,0),fontRect=new Rectangle(0,0,0,0);
    public Texture icon;
    public YzSingleLineBitmapFont font;
    public String title;
    public YzWindowTopBar() {
        bgColor= (Color) UIManager.get(UI.bgTopBarColor);
        icon = new Texture(Gdx.files.internal("logo.png"));
    }
    public YzWindowTopBar(View viewBody) {
        this(viewBody,TYPE_HORIZONTAL);
    }
    public YzWindowTopBar(View viewBody,int Type){
        this();
        this.Type=Type;
        this.viewBody=viewBody;
        Stage bgStage=viewBody.stage;
        viewBody.addProcessor(new WindowTopBarListener(this));

        //计算和加载Skin
        complete(bgStage);
        loadSkins();
        createBg();
        //bgStage.addActor(this);
        addListener(new InputListener(){
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                state=State.MOUSE_NOT_ON;
            }
        });
        //加载字体 字体大小需再做考量
        font = new YzSingleLineBitmapFont(YzCenterControl.DEFAULT_WINDOW_TOP_BAR_FONT_SIZE);
        font.setColor(YzCenterControl.WINDOW_TOP_BAR_FONT_COLOR);
    }

    public void setTitle(String text){
        this.title=text;
        font.setText(this.title);
        fontRect.set(getX()+getWidth()/2-font.getWidth()/2,getY()+iconRect.getHeight()/2-font.FontHeight/2,font.getWidth(),getHeight());
        font.setPosition(fontRect.getX(), fontRect.getY());
    }
    public Texture getSkin(int width, int height,String file,String ID) {
        return loadSkin(width, height, file, ID);
    }
    protected void complete(Stage bgStage) {
        float height = bgStage.getHeight(),width =  bgStage.getWidth();
        if(Type == TYPE_HORIZONTAL) {
            setBounds(0,height*(1-barHeightPercent),width,height*barHeightPercent);
            button1Rect.set(getX()+getWidth()-getHeight(),getY(),getHeight(),getHeight());
            button2Rect.set(getX()+getWidth()-getHeight()*2,getY(),getHeight(),getHeight());
            topBar.set(getX(),getY(),getWidth(),getHeight());
        }else if(Type == TYPE_VERTICAL) {
            setBounds(0,0,width*barWidthPercent,height);
            button1Rect.set(getX(),getY(),getWidth(),getWidth());
            button2Rect.set(getX(),getY()+getWidth(),getWidth(),getWidth());
            topBar.set(getX(),getY(),getWidth(),getHeight());
        }
        iconRect.set(getX()+getHeight()*0.1f,getY()+getHeight()*0.1f,getHeight()*0.8f,getHeight()*0.8f);
    }
    protected void loadSkins(){
        if(Type == TYPE_HORIZONTAL){
            int mHeight = (int) getHeight();
            button1_un=getSkin(mHeight, mHeight,"icon.xml","topButton1_un");
            button1_on=getSkin(mHeight, mHeight,"icon.xml","topButton1_on");
            button2_un=getSkin(mHeight, mHeight,"icon.xml","topButton2_un");
            button2_on=getSkin(mHeight, mHeight,"icon.xml","topButton2_on");
        } else if (Type == TYPE_VERTICAL) {
            int mWidth = (int) getWidth();
            button1_un=getSkin(mWidth, mWidth,"icon.xml","topButton1_un");
            button1_on=getSkin(mWidth, mWidth,"icon.xml","topButton1_on");
            button2_un=getSkin(mWidth, mWidth,"icon.xml","topButton2_un");
            button2_on=getSkin(mWidth, mWidth,"icon.xml","topButton2_on");
        }
    }
    /**
     * 构建背景图
     * */
    protected void createBg(){
        if(SizeChangedFlag){
            tempPixmap=new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
            SizeChangedFlag=false;
        }
        if(Type == TYPE_HORIZONTAL){
            //paint background_color
            tempPixmap.setColor(bgColor);
            tempPixmap.fillRectangle(0,0, (int) getWidth(), (int) getHeight());
        }else if (Type == TYPE_VERTICAL) {
            tempPixmap.setColor(bgColor);
            tempPixmap.fillRectangle(0,0, (int) getWidth(), (int) getHeight());
        }
        tempTexture=new Texture(tempPixmap);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(tempTexture, getX(),getY(),getWidth(),getHeight());
        batch.draw(icon, iconRect.getX(), iconRect.getY(), iconRect.getWidth(), iconRect.getHeight());
        switch (state){
            case MOUSE_ON_TOP_BAR:
            case MOUSE_NOT_ON:
                batch.draw(button1_un,button1Rect.getX(),button1Rect.getY(),button1Rect.getWidth(),button1Rect.getHeight());
                batch.draw(button2_un,button2Rect.getX(),button2Rect.getY(),button2Rect.getWidth(),button2Rect.getHeight());
                break;
            case MOUSE_ON_BUTTON1:
                batch.draw(button1_on,button1Rect.getX(),button1Rect.getY(),button1Rect.getWidth(),button1Rect.getHeight());
                batch.draw(button2_un,button2Rect.getX(),button2Rect.getY(),button2Rect.getWidth(),button2Rect.getHeight());
                break;
            case MOUSE_ON_BUTTON2:
                batch.draw(button1_un,button1Rect.getX(),button1Rect.getY(),button1Rect.getWidth(),button1Rect.getHeight());
                batch.draw(button2_on,button2Rect.getX(),button2Rect.getY(),button2Rect.getWidth(),button2Rect.getHeight());
                break;
        }
        font.draw(batch,parentAlpha);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if(font!=null) {
            font.setSize((int) getHeight());
        }
        if(getStage()!=null) {
            complete(getStage());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void dispose() {
        if(tempPixmap!=null)
            tempPixmap.dispose();
        if(tempTexture!=null)
            tempTexture.dispose();
        if(icon!=null)
            icon.dispose();
        if(font!=null)
            font.dispose();
        super.dispose();
        bgColor=null;
        tempPixmap=null;
        tempTexture=null;
        button2_un=null;
        button2_on=null;
        button1_on=null;
        button1_un=null;
        icon=null;
        viewBody=null;
        button1Rect=null;
        button2Rect=null;
        topBar=null;
        System.gc();
    }

    class WindowTopBarListener extends InputProcessorEx {

        Lwjgl3Window Display=((Lwjgl3Graphics)Gdx.graphics).getWindow();
        public boolean translate(int x, int y) {
            // WINDOW_HEIGHT is a static variable on launcher code. In my case that would be enough but there is Display.getHeight function if someone needs.
            Display.setPosition(Display.getPositionX() + (Gdx.input.getX() - x), Display.getPositionY() + ( Gdx.input.getY() - y));
            return true;
        }
        public YzWindowTopBar topBar;
        public Vector2 mouseDownPosition2=new Vector2();
        public WindowTopBarListener(YzWindowTopBar topBar){
            this.topBar=topBar;
        }
        int offsetX;
        int offsetY;
        boolean flag=false;
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            super.touchDown(screenX, screenY, pointer, button);
            Vector2 vec= PositionConvert.mouse2origin(new Vector2(screenX,screenY));
            mouseDownPosition2.set(vec.x,vec.y);
            if(topBar.button1Rect.contains(vec.x,vec.y)) {
                return true;
            }else if(topBar.button2Rect.contains(vec.x,vec.y)){
                return true;
            }else if(topBar.topBar.contains(vec.x,vec.y)){
                offsetX = screenX;
                offsetY = screenY;
                flag=true;
                return true;
            }else {
                return true;
            }
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            Vector2 vec=PositionConvert.mouse2origin(new Vector2(screenX,screenY));
            if(flag){
                flag=false;
                offsetX = 0;
                offsetY = 0;
            }
            if(button1Rect.contains(vec.x,vec.y)&&button1Rect.contains(mouseDownPosition2.x,mouseDownPosition2.y)){
                for(View v:Views.views){
                    v.remove();
                }
                Gdx.app.exit();
                return true;
            }else if(button2Rect.contains(vec.x,vec.y)&&button2Rect.contains(mouseDownPosition2.x,mouseDownPosition2.y)){
                //it will do when button was clicked
                WinDef.HWND HWND_MINE= User32.INSTANCE.FindWindow(null, Game.APP_TITLE);
                User32.INSTANCE.SendMessage(HWND_MINE, User32.WM_SYSCOMMAND, new WinDef.WPARAM(User32.SC_MINIMIZE),new WinDef.LPARAM(0));
                return true;
            }
            if(topBar.topBar.contains(mouseDownPosition2)){
                return true;
            }
            return true;
        }
        int m=0;
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if(flag){
                translate(offsetX, offsetY);
                return true;
            }
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            super.mouseMoved(screenX, screenY);
            Vector2 vec=PositionConvert.mouse2origin(new Vector2(screenX,screenY));
            if(bgColor==null){ //防止Dispose()导致的奔溃
                return true;
            }
            if(button1Rect.contains(vec.x,vec.y)){
                topBar.state=State.MOUSE_ON_BUTTON1;
                return true;
            }else if(button2Rect.contains(vec.x,vec.y)){
                topBar.state=State.MOUSE_ON_BUTTON2;
                return true;
            }else if(topBar.topBar.contains(vec.x,vec.y)){
                topBar.state=State.MOUSE_ON_TOP_BAR;
                return true;
            }else {
                topBar.state=State.MOUSE_NOT_ON;
            }
            return true;
        }
    }
}
