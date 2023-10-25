package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Timer;
import com.thzs.app.datacoplite.core.UI;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.util.input.InputProcessorEx;
import com.thzs.app.datacoplite.util.position.PositionConvert;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectList<T extends Actor> extends SelectGroup{
    public Texture bg;
    public Texture con;
    public float offsetX=0,offsetY=0;
    public float scrollRate=15;
    public View view;
    public Rectangle range;
    public SelectList instance;
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor((Color) UIManager.get(UI.bgTopBarColor));
        pixmap.fill();
        bg=new Texture(pixmap);
        pixmap.dispose();
        range=new Rectangle(getX(),getY(),getWidth(),getHeight());
        synthesized();
    }

    public SelectList() {
        flagAddChildToStage=false;
    }

    public SelectList(View view) {
        super(view.stage);
        this.view=view;
        view.addProcessor(new InputProcessorEx(){
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector2 vec= PositionConvert.mouse2origin(new Vector2(screenX,screenY));
                if(range.contains(vec)){
                    if(!focus){
                        viewStage.setScrollFocus(instance);
                        setDebug(true);
                    }
                    focus=true;
                    return true;
                }else {
                    if(focus){
                        viewStage.setScrollFocus(null);
                        setDebug(false);
                    }
                    return false;
                }
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                offsetX+=offsetX+amountX*scrollRate>=0?amountX*scrollRate:0;
                offsetY+=offsetY+amountY*scrollRate>=0?amountY*scrollRate:0;
                return super.scrolled(amountX, amountY);
            }
        });
        flagAddChildToStage=false;
    }

    public boolean hasSelectListSettingSupported(T child){
        return child instanceof SelectListSettingSupport;
    }
    /**
     * 修改成员添加方式
     * */
    //================================================================
    @Override
    public void addChildByAsync(Actor child) {
        if(child instanceof SelectListSettingSupport) {
            super.addChildByAsync(child);
        }
    }
    @Override
    public void addChild(Actor child) {
        if(child instanceof SelectListSettingSupport) {
            super.addChild(child);
            updateChildIndexData();
        }
    }
    @Override
    public void addChild(Actor child, int index) {
        if(child instanceof SelectListSettingSupport) {
            super.addChild(child, index);
            SelectListSettingSupport sls2= (SelectListSettingSupport) child;
            sls2.setData(this,SelectListSettingSupport.DATA_INDEX,index);
        }
    }
    public void addChildByAsync(final Actor child, final Map<String,Object> data) {
        if(child instanceof SelectListSettingSupport){
            Timer.post(new Timer.Task() {
                @Override
                public void run() {
                    addChild(child,data);
                }
            });
        }
    }
    public void addChild(Actor child,Map<String,Object> data) {
        if(child instanceof SelectListSettingSupport){
            SelectListSettingSupport sls2= (SelectListSettingSupport) child;
            this.addChild(child);
            updateChildIndexData();
            setData(sls2.getIndex(this),data);
            refresh(sls2.getIndex(this));
            synthesized();
        }
    }
    public void addChild(Actor child,Map<String,Object> data, int index) {
        if(child instanceof SelectListSettingSupport){
            this.addChild(child, index);
            data.put(SelectListSettingSupport.DATA_INDEX, index);
            setData(index,data);
            refresh(index);
            synthesized();
        }
    }

    @Override
    public void remove(Actor child) {
        super.remove(child);
        if (child instanceof SelectListSettingSupport){
            childrenFrames.remove(((SelectListSettingSupport) child).getData(this,SelectListSettingSupport.DATA_FRAME_INDEX));
            updateChildIndexData();
            synthesized();
        }
    }
    //o(N)
    public void remove(int index){
        remove(children.get(index));
    }
    //===================================================================
    /**
     *  用于实现SelectListSettingSupport refresh(),setData()
     * */
    public Map<Integer,Pixmap> childrenFrames=new HashMap<>();
    public void refresh(int index){
        if (children.get(index) instanceof SelectListSettingSupport){
            SelectListSettingSupport sl=((SelectListSettingSupport)children.get(index));
            Rectangle rectangle=sl.getPreSize(this);
            if(rectangle.width<=0||rectangle.height<=0){
                return;
            }
            Pixmap pixmap=new Pixmap((int) rectangle.width, (int) rectangle.height, Pixmap.Format.RGBA8888);
            int ind=sl.getIndex(this);
            childrenFrames.put(ind,sl.refresh(this,pixmap));
            sl.setData(this,SelectListSettingSupport.DATA_FRAME_INDEX,ind);
        }
    }
    public void refreshAll(){
        for (int i = 0; i < children.size(); i++) {
           refresh(i);
        }
    }
    public void setData(int index, Map<String, Object> maps) {
        if (children.get(index) instanceof SelectListSettingSupport) {
            ((SelectListSettingSupport)children.get(index)).setData(this,maps);
        }
    }
    public void setDataList(List<Map<String, Object>> lists) {
        for (int i = 0; i < Math.min(children.size(), lists.size()); i++) {
            setData(i,lists.get(i));
        }
    }
    /**
     * 获取预计贴图大小
     * */
    public Rectangle getPreSize(){
        float width=0,height=0;
        for(Actor i: children){
            if (i instanceof SelectListSettingSupport) {
                Rectangle rectangle=((SelectListSettingSupport)i).getPreSize(this);
                if (type==TYPE.HOR) {
                    width+=rectangle.width+childrenPadding;
                    height=Math.max(height,rectangle.height);
                }else if (type==TYPE.VER){
                    height+=rectangle.height+childrenPadding;
                    width=Math.max(width,rectangle.width);
                }
            }
        }
        return new Rectangle(0,0,width+paddingLeft+paddingRight,height+paddingBottom);
    }
    /**
     * 用于成员设置自身大小
     * */
    public float getWidthForChild(){
        return getWidth()-paddingRight-paddingLeft;
    }
    public float getHeightForChild(){
        return getHeight()-paddingLeft-paddingBottom;
    }
    //合成图片缓存
    protected Pixmap synTemp;
    /**
     * 用于合成最终展示贴图，减小CPU，GPU占用，展示过多时可能产生内存占用过大，有待优化
     * */
    public void synthesized(){
        Rectangle rectangle=getPreSize();
        int offsetY= (int) (rectangle.getHeight()-getHeight());
        if(offsetY<0){
            offsetY=0;
        }
        synTemp=new Pixmap((int) rectangle.getWidth(), (int) rectangle.getHeight(), Pixmap.Format.RGBA8888);
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof SelectListSettingSupport) {
                Actor a=children.get(i);
                int x= (int) (a.getX()-getX()),y= (int) (a.getY()-getY()+a.getHeight()+childrenPadding)+offsetY;
                //to origin position
                y= (int) (rectangle.height-y);
                Pixmap tmp=childrenFrames.get(((SelectListSettingSupport)a).getData(this,SelectListSettingSupport.DATA_FRAME_INDEX));
                synTemp.drawPixmap(tmp,x,y);
            }
        }
        this.con=new Texture(synTemp);
        synTemp.dispose();
    }
    /**
     * SelectListSettingSupport 支持获取INDEX
     * 本函数用于更新成员索引，在成员发生改变时 o(N)
     * */
    public synchronized void updateChildIndexData(){
        for (int i = 0; i < children.size(); i++){
            if(children.get(i) instanceof SelectListSettingSupport){
                SelectListSettingSupport selected= (SelectListSettingSupport) children.get(i);
                selected.setData(this,SelectListSettingSupport.DATA_INDEX,i);
            }
        }
    }
    @Override
    public void completeChildrenV(){
        float x=getX()+paddingLeft;
        float y=getY()+getHeight()-paddingTop;
        float maxWidth=getWidth()-paddingLeft-paddingRight;
        for (Actor actor : children) {
            //超出控件边界隐藏
            float maxHeight=y - getY()-paddingBottom;
            if(maxHeight<=ChildrenMinSize){
                actor.setVisible(false);
            }else {
                actor.setVisible(true);
            }
            //计算长宽
            float height = Math.min(actor.getHeight(), maxHeight);
            float width;
            if(actor instanceof SelectGroupSettingSupport){
                width = ((SelectGroupSettingSupport) actor).flagSetToMax?maxWidth:actor.getWidth();
            }else {
                width = Math.min(actor.getWidth(), maxWidth);
            }
            if(actor instanceof SelectListSettingSupport){
                Rectangle rectangle=((SelectListSettingSupport)actor).getPreSize(this);
                width =rectangle.width;
                height =rectangle.height;
                actor.setVisible(true);
            }
            float Y = y - height;
            actor.setBounds(x, Y, width, height);
            y = y - height - childrenPadding;
        }
    }
    @Override
    public void completeChildrenH(){
        float x=getX()+paddingLeft;
        float y=getY()+paddingBottom;
        float maxHeight=getWidth()-paddingTop-paddingBottom;
        for (Actor actor:children) {
            //检测是否有足够空间放置控件
            float maxWidth=getX()+getWidth()-paddingRight-x;
            if(maxWidth<=ChildrenMinSize){
                actor.setVisible(false);
            }else {
                actor.setVisible(true);
            }
            //计算长宽
            float width = Math.min(actor.getWidth(),maxWidth);
            float height;
            if(actor instanceof SelectGroupSettingSupport){
                height = ((SelectGroupSettingSupport) actor).flagSetToMax?maxHeight:actor.getHeight();
            }else {
                height = Math.min(actor.getHeight(), maxHeight);
            }
            if(actor instanceof SelectListSettingSupport){
                Rectangle rectangle=((SelectListSettingSupport)actor).getPreSize(this);
                width =rectangle.width;
                height =rectangle.height;
                actor.setVisible(true);
            }
            actor.setBounds(x,y,width,height);
            x=x+actor.getWidth()+childrenPadding;
        }
    }
    public boolean focus=false;
    /**
     * 设置专属事件监听
     * */
    @Override
    public void onSelectListener() {
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 vec=new Vector2(x,y);
                vec.x-=offsetX;
                vec.y-=offsetY;
                for (Map.Entry<Actor, Rectangle> entry : testRange.entrySet()){
                    if(entry.getKey() instanceof SelectGroupSettingSupport && entry.getKey().isVisible()){
                        if (entry.getValue().contains(vec.x,vec.y)){
                            ((SelectGroupSettingSupport)entry.getKey()).OnSetFocus(true);
                        }else{
                            ((SelectGroupSettingSupport)entry.getKey()).OnSetFocus(false);
                        }
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int y= (int) (con.getHeight()-getHeight());
        batch.draw(bg,getX(),getY(),getWidth(),getHeight());
        batch.draw(con,getX(),getY(), (int) offsetX, (int) offsetY, (int) getWidth(), (int) getHeight());
    }

    @Override
    public void dispose() {
        if(!synTemp.isDisposed()){
            synTemp.dispose();
        }
        for (Map.Entry<Integer,Pixmap> c:childrenFrames.entrySet()){
            if(c.getValue()==null||!c.getValue().isDisposed()){
                continue;
            }
            c.getValue().dispose();
        }
        con.dispose();
        super.dispose();
    }
}