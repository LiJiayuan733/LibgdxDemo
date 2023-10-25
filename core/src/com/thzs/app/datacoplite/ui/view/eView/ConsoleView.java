package com.thzs.app.datacoplite.ui.view.eView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.ConsoleText;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;

public class ConsoleView extends View {
    ConsoleText consoleText = new ConsoleText(16);
    YzWindowTopBar topBar;
    YzEditArea editArea;
    @Override
    public void create() {
        stage= Game.stage();
        topBar=new YzWindowTopBar(this);
        stage.addActor(topBar);
        consoleText.setBounds(stage.getWidth()/4,stage.getHeight()/4,stage.getWidth()/2,stage.getHeight()/2);
        stage.addActor(consoleText);
        consoleText.setColor(Color.valueOf("#000000FF"));
        for (int i = 0; i< Log.logs.size(); i++)
            consoleText.addAction(new ConsoleText.AddLog(Log.logs.get(i).message));
        Log.Listener=new Log.LogListener() {
            @Override
            public void newLog(Log.LogLine log) {
                consoleText.addAction(new ConsoleText.AddLog(log.message));
            }
        };
        editArea=new YzEditArea("Edit Area 测试");
        editArea.setBounds(consoleText.getX(),consoleText.getY()-topBar.getHeight(),consoleText.getWidth(),topBar.getHeight());
        stage.addActor(editArea);
        stage.setDebugAll(true);
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
    public boolean keyUp(int keycode) {
        if(keycode== Input.Keys.ESCAPE){
            remove();
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.LEFT){
            editArea.index= Math.max(editArea.index-1, editArea.StartIndex);
        }else if (keycode== Input.Keys.RIGHT){
            editArea.index= Math.min(editArea.index+1, editArea.EndIndex);
        }else if (keycode== Input.Keys.BACKSPACE){
            editArea.delChar(editArea.index);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        consoleText.touchDragged(screenX, screenY, pointer);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        consoleText.touchUp(screenX, screenY, pointer, button);
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        consoleText.touchDown(screenX, screenY, pointer, button);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        consoleText.scrolled(Gdx.input.getX(), Gdx.input.getY(), amountX, amountY);
        return super.scrolled(amountX, amountY);
    }

    @Override
    public void onRemove() {
        consoleText.dispose();
        topBar.dispose();
        editArea.dispose();
        Log.Listener=null;
        consoleText=null;
        topBar=null;
        super.onRemove();
    }
}
