package com.thzs.app.datacoplite.ui.widget.selectgroup;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thzs.app.datacoplite.lib.LazyBitmapFont;


public class SelectBitmapFont {
    public BitmapFont font;
    int size=0;
    public SelectBitmapFont(int size) {
        this.size=size;
        this.font= new LazyBitmapFont(size);
        font.setColor(Color.BLACK);
    }
}
