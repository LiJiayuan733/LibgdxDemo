package com.thzs.app.datacoplite.ui.view.eView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.action.LinkAction;
import com.thzs.app.datacoplite.ui.view.ParameterizableView;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.util.position.Position;
import com.thzs.app.datacoplite.util.grapices.XmlPixmap;


import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 *  通过在map中设置next来传递下一个窗口，对象是class
 * */
public class LoadView extends ParameterizableView {
    public static final String KEY_NEXT="next";
    public static final String KEY_BG_IMG="bg_img";
    public LoadView(Map<String, Object> map) {
        super(map);
    }
    public enum statue{
        loading,finished
    }
    static class LoadAnimation extends LinkAction {
        static class powInShow extends LinkAction{
            public int x,y;

            public powInShow(float duration) {
                super(0,1, duration, Interpolation.pow2Out);
            }

            public powInShow setStartPosition(int x,int y){
                this.x = x;
                this.y = y;
                return this;
            }

            @Override
            protected void begin() {
                super.begin();
                getActor().setPosition(x,y);
            }

            @Override
            protected void update(float percent) {
                super.update(percent);
                getActor().setColor(1,1,1,percent);
            }
        }
        static class powOutShow extends LinkAction{
            public powOutShow(float duration) {
                super(0,1, duration, Interpolation.pow2Out);
            }

            @Override
            protected void update(float percent) {
                super.update(percent);
                getActor().setColor(1,1,1,1-percent);
            }

        }
        public Position targetPosition;
        public Position startPosition;
        public Random random=new Random();
        public LoadAnimation(float start, float end, float duration, Interpolation interpolation,Position startPosition) {
            super(start, end, duration, interpolation);
            this.startPosition = startPosition;
        }

        @Override
        protected void begin() {
            super.begin();
            targetPosition= new Position((int) (Gdx.graphics.getWidth()/2-getActor().getWidth()/2), (int) (Gdx.graphics.getHeight()/2-getActor().getHeight()/2), 0);
        }

        @Override
        protected void update(float percent) {
            getActor().setPosition(startPosition.x+(targetPosition.x-startPosition.x)*percent,startPosition.y+(targetPosition.y-startPosition.y)*percent);
            super.update(percent);
        }

        @Override
        protected void end() {
            super.end();
            if(st==statue.finished){
                getActor().addAction(new powOutShow(0.5f));
                return;
            }
            Position position=new Position(random.nextInt(Gdx.graphics.getWidth()),random.nextInt(Gdx.graphics.getHeight()),0);
            getActor().addAction(new powOutShow(0.4f)
                            .nextAction(new powInShow(0.4f).setStartPosition(position.x,position.y)
                            .nextAction(new LoadAnimation(0,1,2f,Interpolation.smooth,position)
                            )));
        }
    }
    public static statue st=statue.loading;
    XmlPixmap pixmap;
    ArrayList<Texture> list=new ArrayList();
    Image image,image2;
    Class<? extends View> gotoView;
    Image bg;
    @Override
    public void create(Map<String, Object> param) {
        stage= Game.stage();
        if(param.containsKey("next")){
            gotoView=(Class<? extends View>) param.get("next");
        }else{
            Log.e("[LoadView] the param 'next' is Not Defined");
        }
        if (param.containsKey("bg_img")){
            bg=new Image(new Texture(Gdx.files.internal((String) param.get("bg_img"))));
            bg.setBounds(0,0, stage.getWidth(), stage.getHeight());
            stage.addActor(bg);
        }
        pixmap = new XmlPixmap(200,200,"MoreShape.xml","shape_ball");
        list.add(new Texture(pixmap));
        image = new Image(list.get(0));
        image2 = new Image(list.get(0));
        image.setBounds(100,100,100,100);
        image.addAction(new LoadAnimation(0,1,2f,Interpolation.smooth,new Position((int) image.getX(), (int) image.getY(),0)));

        Game.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                Position position=new Position(random.nextInt(Gdx.graphics.getWidth()),random.nextInt(Gdx.graphics.getHeight()),0);
                image2.setBounds(80,80,80,80);
                image2.addAction(new LoadAnimation.powInShow(0.7f).setStartPosition(position.x,position.y)
                        .nextAction(new LoadAnimation(0,1,2f,Interpolation.smooth,position)
                        ));
                stage.addActor(image2);
            }
        },2.4f);

        stage.addActor(image);
    }

    @Override
    public void draw() {

        stage.draw();
    }

    @Override
    public void act() {
        stage.act();
        if(st==statue.finished){
            remove();
            if(gotoView!=null){
                Views.addView(gotoView);
            }else{
                Log.e("[LoadView] the param 'next' is Not Defined");
            }
        }
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Log.i("[LoadView] scrolling to position: " + amountX + " " + amountY);
        return false;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        this.pixmap.dispose();
        this.stage.dispose();
        LoadView.st=statue.loading;
    }

    @Override
    public String toString() {
        return "LoadView{" +
                "gotoView=" + gotoView.toString() +
                '}';
    }
}
