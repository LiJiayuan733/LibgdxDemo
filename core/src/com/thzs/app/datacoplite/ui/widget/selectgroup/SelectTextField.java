package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.kotcrab.vis.ui.widget.VisTextField;

public class SelectTextField extends VisTextField implements SelectGroupSettingSupport{
    public SelectTextField() {
    }

    public SelectTextField(String text) {
        super(text);
    }

    public SelectTextField(String text, String styleName) {
        super(text, styleName);
    }

    public SelectTextField(String text, VisTextFieldStyle style) {
        super(text, style);
    }

    public boolean flagFocus=false;
    @Override
    public boolean isFlagFocus() {
        return flagFocus;
    }

    @Override
    public void OnSetFocus(boolean flag) {
        this.flagFocus=flag;
    }

    @Override
    public void SelectInit() {

    }
}
