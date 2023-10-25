package com.thzs.app.datacoplite.core;

import com.badlogic.gdx.graphics.Color;

import javax.swing.*;

public class UI {
    public static final String bgTopBarColor = "barTopBgColor";
    public static final String bgSelectGroupColor = "bgSelectGroupColor";
    public static void init(){
        UIManager.put(bgTopBarColor, Color.valueOf("#ffffffff"));
        UIManager.put(bgSelectGroupColor, Color.valueOf("#FFFFFFFF"));
    }
}
