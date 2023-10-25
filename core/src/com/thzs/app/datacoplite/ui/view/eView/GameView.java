package com.thzs.app.datacoplite.ui.view.eView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.util.input.InputProcessorEx;
import com.thzs.app.datacoplite.util.position.Position;
import com.thzs.app.datacoplite.util.grapices.XmlPixmap;

import java.util.HashMap;


public class GameView extends View {
    Stage stage;
    Image img;
    Position imgPos=new Position(0,0,1);
    VisLabel label1;
    @Override
    public String toString() {
        return "GameView";
    }

    @Override
    public void create() {
        stage= Game.stage();
        img=new Image(new Texture(new XmlPixmap(700,300,"InformationPanel.xml","info")));
        addProcessor(new InputProcessorEx(){

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                imgPos=new Position(screenX, screenY, 1);
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(screenX<100&&screenY<100){
                    remove();
                    HashMap<String ,Object> param=new HashMap<String ,Object>();
                    param.put("next",ConsoleView.class);
                    Views.addView(LoadView.class,param);
                    Game.timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            LoadView.st= LoadView.statue.finished;
                        }
                    },3);
                }
                img.addAction(Actions.moveTo(imgPos.x, Gdx.graphics.getHeight()-imgPos.y,0.5f));
                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
        addProcessor(stage);
        stage.addActor(img);
        label1 = new VisLabel("View Test 1 WindowsView");
        label1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                remove();
                HashMap<String ,Object> param=new HashMap<>();
                param.put("next",WindowsView.class);
                Views.addView(LoadView.class,param);
                Game.timer.scheduleTask(new Timer.Task(){
                    @Override
                    public void run() {
                        LoadView.st= LoadView.statue.finished;
                    }
                },5);
            }
        });
        label1.setAlignment(Align.right);
        stage.addActor(label1);
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void act() {
        stage.act();
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void onRemove() {
        super.onRemove();
    }
}
