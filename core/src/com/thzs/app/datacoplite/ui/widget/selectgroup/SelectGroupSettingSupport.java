package com.thzs.app.datacoplite.ui.widget.selectgroup;


public interface SelectGroupSettingSupport {
    public boolean flagSetToMax=true;
    public abstract boolean isFlagFocus();
    public abstract void OnSetFocus(boolean flag);
    /**
     * the method will be called for the end of process.
     * */
    public abstract void SelectInit();
}
