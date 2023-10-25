package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kotcrab.vis.ui.widget.VisLabel;

public class SelectLabel extends VisLabel implements SelectGroupSettingSupport{
    public SelectLabel() {
    }

    public SelectLabel(CharSequence text, Color textColor) {
        super(text, textColor);
    }

    public SelectLabel(CharSequence text, int alignment) {
        super(text, alignment);
    }

    public SelectLabel(CharSequence text) {
        super(text);
    }

    public SelectLabel(CharSequence text, LabelStyle style) {
        super(text, style);
    }

    public SelectLabel(CharSequence text, String styleName) {
        super(text, styleName);
    }

    public SelectLabel(CharSequence text, String fontName, Color color) {
        super(text, fontName, color);
    }

    public SelectLabel(CharSequence text, String fontName, String colorName) {
        super(text, fontName, colorName);
    }

    boolean flagFocus=false;
    @Override
    public boolean isFlagFocus() {
        return flagFocus;
    }

    @Override
    public void OnSetFocus(boolean flag) {
        flagFocus=flag;
        if(flag){
            setColor(Color.RED);
        }else {
            setColor(Color.BLACK);
        }
    }

    @Override
    public void SelectInit() {
        setTouchable(Touchable.enabled);
    }
}
