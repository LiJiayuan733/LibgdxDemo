package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.thzs.app.datacoplite.core.Log;

public class SelectTextButton extends VisTextButton implements SelectGroupSettingSupport {
    public SelectTextButton(String text, String styleName) {
        super(text, styleName);
    }

    public SelectTextButton(String text) {
        super(text);
    }

    public SelectTextButton(String text, ChangeListener listener) {
        super(text, listener);
    }

    public SelectTextButton(String text, String styleName, ChangeListener listener) {
        super(text, styleName, listener);
    }

    public SelectTextButton(String text, VisTextButtonStyle buttonStyle) {
        super(text, buttonStyle);
    }
    boolean flagFocus=false;
    @Override
    public void OnSetFocus(boolean flag) {
        flagFocus=flag;
        if(flag){
            Log.i(this.toString());
        }
    }

    @Override
    public void SelectInit() {

    }

    @Override
    public boolean isFlagFocus() {
        return flagFocus;
    }
}
