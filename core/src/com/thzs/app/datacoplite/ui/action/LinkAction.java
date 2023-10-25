package com.thzs.app.datacoplite.ui.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * 链式反应动作
 * */
public class LinkAction extends SimpleAction{
    public Action nextAction=null;

    public LinkAction(float start, float end, float duration, Interpolation interpolation) {
        super(start, end, duration, interpolation);
    }
    /**
     * 设置下一个加入的动作
     * */
    public LinkAction nextAction(Action action) {
        nextAction = action;
        return this;
    }
    @Override
    protected void end() {
        super.end();
        if (nextAction != null) {
            getActor().addAction(nextAction);
        }
    }
}
