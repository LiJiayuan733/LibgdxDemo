package com.thzs.app.datacoplite.ui.widget;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;

import java.util.ArrayList;
import java.util.List;
/**
 *  the below Event method should be called
 *  Method::touchDown
 *  Method::touchUp
 *  Method::touchDragged
 *  Method::scrolled
 * */
public class ConsoleText extends Actor implements Disposable {
    public static boolean flagYCanMove=false;
    //背景色
    protected Color backGroundColor = Color.valueOf("#FFFFFFFF");
    //分割线颜色
    protected Color spiteLineColor = Color.valueOf("#77AAFFFF");
    //滑块颜色
    protected Color scrollBarColor = Color.valueOf("#3a3a3aaa");
    //内边距
    protected Vector2 padding = new Vector2(10,0);
    //速度
    protected Vector2 vertices =new Vector2(8,8);
    //背景,分割线
    public Texture bg,line,scrollBar;
    protected float scrollBarWidthPercent = 0.02f;
    //显示图片的区域
    protected Rectangle showRanges = new Rectangle(0,0,getWidth(),getHeight());
    //显示滑块的区域
    protected Rectangle showScrollBar = new Rectangle(getWidth()*(1.0f-scrollBarWidthPercent),0,getWidth()*scrollBarWidthPercent,getHeight());
    //字体
    protected LazyBitmapFont font;
    //记录
    protected List<String> logs=new ArrayList<String>();
    public ConsoleText(int fontSize){
        //when the application running on Android device
        if(Gdx.app.getType()==Application.ApplicationType.Android){
            vertices.set(1,1);
        }
        font=new LazyBitmapFont(fontSize);
        font.setColor(new Color(0,0,0,1));
        completeBackground();
        completeLine();
        completeScrollBar();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //check showRanges isn't out of its bounds
        if(showRanges.y<0||maxHeight<getHeight()){
            showRanges.y=0;
            completeScrollBarPosition();
        }else if(showRanges.y>maxHeight-getHeight()){
            showRanges.y = maxHeight - getHeight();
            completeScrollBarPosition();
        }
    }

    /** 获取记录全部显示所需的高度 **/
    //it will be updated when log is updated
    public int maxHeight=0;
    public int getMaxHeight(){
        maxHeight=(int) (font.getLineHeight()*logs.size());
        return maxHeight;
    }
    public int maxWidth=0;
    public int getMaxWidth(){
        //TODO: complete the maxWidth
        return (int) getWidth();
    }

    protected int pointer=-1,tempScreenY,tempScreenX;
    /** 滑动时发生的更新 <div color="red">需要手动调用<div/> **/
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(getX()<screenX&&screenX<getX()+getWidth()&&getY()<screenY&&screenY<getY()+getHeight()){
            this.pointer=pointer;
            tempScreenX=screenX;
            tempScreenY=screenY;
            return true;
        }else {
            return false;
        }

    }
    /** 滑动时发生的更新 <div color="red">需要手动调用<div/> **/
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(this.pointer==pointer){
            this.scrolled(screenX,screenY,(tempScreenX-screenX)*0.3f, (tempScreenY-screenY)*0.3f);
            tempScreenX=screenX;
            tempScreenY=screenY;
            return true;
        }else {
            return false;
        }
    }
    /** 滑动时发生的更新 <div color="red">需要手动调用<div/> **/
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(this.pointer == pointer){
            this.pointer =-1;
            tempScreenX=0;
            tempScreenY=0;
            return true;
        }
        return false;
    }
    /** 滑动时发生的更新 <div color="red">需要手动调用<div/> **/
    public boolean scrolled(float x, float y, float amountX, float amountY) {
        y= Gdx.graphics.getHeight() -y;
        //如果不让移动Y方向
        if(!flagYCanMove){
            amountX = 0;
        }
        //判断在控件区域内
        if(getX()<x&&x<getX()+getWidth()&&getY()<y&&y<getY()+getHeight()){
            //判断是否滑动到了 顶部和底部
            if((showRanges.y-vertices.y<0&&amountY<0)||(showRanges.y>(maxHeight-getHeight())&&amountY>0)){
                return true;
            }
            //更新显示区域
            showRanges.set(showRanges.x+amountX*vertices.x,showRanges.y+amountY*vertices.y,getWidth(),getHeight());
//            float tHeight=getHeight()-showScrollBar.getHeight();
//            float p=Math.min(1.0f-(showRanges.y/((float)maxHeight-getHeight())),1f);
//            showScrollBar.setY(tHeight-tHeight*p);
            completeScrollBarPosition();
        }
        return true;
    }
    /** 计算分割线所需的Texture **/
    public void completeLine(){
        //字体高度
        float lineHeight=font.getLineHeight();
        //大小为 Width Height+3*lineHeight
        Pixmap map=new Pixmap((int) getWidth(),(int)Math.min((logs.size() + 1) * lineHeight, getHeight()+3*lineHeight), Pixmap.Format.RGBA8888);
        map.setColor(spiteLineColor);
        for (int i = 0; i < Math.min(getHeight()/lineHeight+2,logs.size()); i++) {
            //当前记录左上角的y坐标
            int PointY= (int) (lineHeight*i-showRanges.getY());
            //超出顶部
            if(PointY<0){
                continue;
            }
            //超出底部 留出2个空位
            if(PointY-2*lineHeight>getHeight()){
                break;
            }
            //绘制分割线
            map.fillRectangle((int) (padding.x/2),PointY, (int) (getWidth()-padding.x), 2);
        }
        line=new Texture(map);
        map.dispose();
    }
    /** 计算背景所需的Texture **/
    public void completeBackground() {
        Pixmap map=new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        map.setColor(backGroundColor);
        map.fill();
        bg=new Texture(map);
        map.dispose();
    }
    /** 计算滑动块 **/
    public void completeScrollBar(){
        float barHeight=getHeight()*(Math.min(getHeight()/maxHeight,1));
        showScrollBar.setHeight(barHeight);
        showScrollBar.setWidth(getWidth()*scrollBarWidthPercent);
        showScrollBar.setX(getWidth()*(1.0f-scrollBarWidthPercent));
        Pixmap map=new Pixmap((int) (getWidth()*scrollBarWidthPercent), (int) barHeight, Pixmap.Format.RGBA8888);
        map.setColor(scrollBarColor);
        map.fill();
        scrollBar=new Texture(map);
        map.dispose();
    }
    /*** 计算更新滑动块位置 ***/
    public void completeScrollBarPosition(){
        float tHeight=getHeight()-showScrollBar.getHeight();
        float p=Math.min(1.0f-(showRanges.y/((float)maxHeight-getHeight())),1f);
        showScrollBar.setY(tHeight-tHeight*p);
    }
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        completeBackground();
        completeScrollBar();
        completeLine();
    }
    /** 添加记录 应该由 {@link AddLog} Action来完成 **/
    public void addLog(String log) {
        this.logs.add(log);
        completeLine();
        completeScrollBar();
        getMaxHeight();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //绘制背景
        batch.draw(bg, getX(),getY(),getWidth(),getHeight());
        //字体高度
        float lineHeight=font.getLineHeight();
        //字体偏移
        float fontOffsetY=10;
        for (int i = 0; i < logs.size(); i++) {
            int PointY= (int) (lineHeight*i-showRanges.getY());
            //超出上边界
            if(PointY<0){
                continue;
            }
            //超出下边界
            if(PointY>getHeight()-lineHeight){
                break;
            }
            //循环绘制分割线
            //batch.draw(line, getX(),getY(), (int)showRanges.getX() , (int) (showRanges.getY()%lineHeight), (int) getWidth(), (int) getHeight());
            //绘制记录
            //TODO: the font now can't move on x,you should released this function
            font.draw(batch,logs.get(i),getX()+padding.x/2,getY()+getHeight()-PointY-fontOffsetY,getWidth()-padding.x,Align.left,true);
            if(maxHeight>getHeight()){
                batch.draw(scrollBar,getX()+showScrollBar.getX(),getY()+getHeight()-showScrollBar.getHeight()-showScrollBar.getY(),showScrollBar.getWidth(),showScrollBar.getHeight());
            }
        }
    }

    public static class AddLog extends Action {
        protected String log;
        public AddLog(String log) {
            this.log = log;
        }
        @Override
        public boolean act(float delta) {
            if (log!=null) {
                if(getActor() instanceof ConsoleText){
                    ((ConsoleText) getActor()).addLog(log);
                }
            }
            return true;
        }
    }

    public void dispose() {
        font.dispose();
        bg.dispose();
        line.dispose();
        scrollBar.dispose();
        logs=null;
        backGroundColor=null;
        spiteLineColor=null;
        scrollBarColor=null;
        padding=null;
        vertices=null;
        showRanges=null;
        showScrollBar=null;
    }
}
