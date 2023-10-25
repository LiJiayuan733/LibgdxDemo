package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.core.Query.GdxQuery;

/**
 * 对GDX-Query的发展
 * */
public class YzGdxQuery extends GdxQuery {
    public YzGdxQuery() {
    }

    public YzGdxQuery(Object... a) {
        super(a);
    }

    public YzGdxQuery dispose(){
        for(Actor actor:list())
            if (actor.equals(YzCenterControl.focusWidget)) {
                YzCenterControl.focusWidget = null;
                break;
            }
        for(Actor actor:list())
            if (actor instanceof Disposable)
                ((Disposable) actor).dispose();
        return this;
    }
    public YzGdxQuery loadPhysics(){
        for(Actor actor:list())
            if (actor instanceof YzPWidget)
                ((YzPWidget) actor).loadPhysics();
        return this;
    }
    public YzGdxQuery loadYzFocus(){
        for(Actor actor:list())
            if (actor instanceof YzCenterControl.YzControl)
                actor.addListener(new ClickListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(isOver(event.getListenerActor(),x,y)){
                            for(Actor actor:list()){
                                if (actor instanceof YzCenterControl.YzControl)
                                    if(((YzCenterControl.YzControl) actor).IsFocus()) {
                                        ((YzCenterControl.YzControl) actor).CancelFocus((YzWidget) actor);
                                        break;
                                    }
                            }
                            ((YzWidget) event.getListenerActor()).SetFocus(YzCenterControl.focusWidget);
                            return true;
                        }
                        return false;
                    }
                });
        return this;
    }
}
