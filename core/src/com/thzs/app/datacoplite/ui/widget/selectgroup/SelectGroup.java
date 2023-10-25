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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.thzs.app.datacoplite.core.Query.$;
import com.thzs.app.datacoplite.core.Query.RemoveTest;
import com.thzs.app.datacoplite.core.UI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelectGroup extends Actor implements Disposable {
    public enum TYPE{
      VER,HOR
    };
    public Texture bg;
    public Color bgColor;
    public Stage viewStage;
    public List<Actor> children;
    public ConcurrentHashMap<Actor, Rectangle> testRange;
    public TYPE type= TYPE.VER;
    public SelectGroup(){
        onSelectListener();
        children = new ArrayList<Actor>();
        testRange=new ConcurrentHashMap<Actor, Rectangle>();
        bgColor = (Color) UIManager.get(UI.bgSelectGroupColor);
        complete();
    }
    public SelectGroup(Stage stage){
        this();
        this.viewStage=stage;
        stage.addActor(this);
    }
    public void onSelectListener(){
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 vec=new Vector2(x,y);
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
    public void addChildByAsync(final Actor child){
        Timer.post(new Timer.Task() {
            @Override
            public void run() {
                addChild(child);
            }
        });
    }
    public  boolean flagAddChildToStage=true;
    public void addChild(Actor child){
        children.add(child);
        if(flagAddChildToStage) {
            viewStage.addActor(child);
        }
        complete();
        initChild(child);
    }
    public void addChild(Actor child,int index){
        children.add(index,child);
        if(flagAddChildToStage) {
            viewStage.addActor(child);
        }
        complete();
        initChild(child);
    }
    public void initChild(Actor child){
        final SelectGroup selectGroup=this;
        if(child instanceof SelectGroupSettingSupport){
            ((SelectGroupSettingSupport) child).SelectInit();
            child.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return selectGroup.fire(event);
                }
            });
        }
    }
    public void remove(final Actor child){
        $.removeIf(children, new RemoveTest<Actor>() {
            @Override
            public boolean test(Actor object) {
                return child.equals(object);
            }
        });
        if(!flagAddChildToStage){
            $.removeIf(viewStage.getActors(), new RemoveTest<Actor>() {
                @Override
                public boolean test(Actor object) {;
                    return child.equals(object);
                }
            });
        }
        testRange.remove(child);
        complete();
    }
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        complete();
    }
    public void complete(){
        switch (type){
            case VER:
                completeV();
                completeRanges();
                break;
            case HOR:
                completeH();
                completeRanges();
                break;
        }
    }
    public void completeRanges(){
        for (Actor child : children) {
            testRange.put(child, new Rectangle(child.getX(), child.getY(), child.getWidth(),child.getHeight()));
        }
    }
    public void completeH(){
        createBgH();
        completeChildrenH();
    }
    public void completeV(){
        createBgV();
        completeChildrenV();
    }
    public void createBgH(){
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(bgColor);
        pixmap.fill();
        bg = new Texture(pixmap);
    }
    public void createBgV(){
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(bgColor);
        pixmap.fill();
        bg = new Texture(pixmap);
    }
    public int childrenPadding=2;
    public int paddingLeft=2;
    public int paddingRight=2;
    public int paddingTop=0;
    public int paddingBottom=0;
    //如果剩余空间小于ChildrenMinSize则隐藏
    public int ChildrenMinSize=0;
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
            float Y = y - height;
            actor.setBounds(x, Y, width, height);
            y = y - height - childrenPadding;
        }
    }
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
            actor.setBounds(x,y,width,height);
            x=x+actor.getWidth()+childrenPadding;
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(bg, getX(), getY(), getWidth(), getHeight());
    }
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    @Override
    public void dispose() {
        bg.dispose();
        for (Actor a:children) {
            if (a instanceof Disposable){
                ((Disposable) a).dispose();
            }
        }
    }
}
