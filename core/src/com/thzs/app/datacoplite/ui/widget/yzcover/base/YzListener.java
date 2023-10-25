package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.util.Native.MutApiSupport;


public class YzListener {
    public static EventListener FileChoose(YzEditArea2 editArea) {
        return new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editArea.setText(MutApiSupport.SystemFileChoose());
                super.clicked(event, x, y);
            }
        };
    }
    public static EventListener FolderChoose(YzEditArea2 editArea) {
        return new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editArea.setText(MutApiSupport.SystemFolderChoose());
                super.clicked(event, x, y);
            }
        };
    }
}
