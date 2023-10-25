package com.thzs.app.datacoplite.util.position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
/**
 * Convert origin position to the bottom left of the screen
 * */
public class PositionConvert {
    /**
     * @return the origin position
     * */
    public static Vector2 mouse2origin(Vector2 position){
        return new Vector2(position.x, Gdx.graphics.getHeight()-position.y);
    }
    /**
     * @return the position reflect where the Pixmap position
     * */
    public static Vector2 origin2Pixmap(Pixmap pixmap,Vector2 position){
        return new Vector2(position.x,pixmap.getHeight()-position.y);
    }
    public static Vector2 origin2Screen(Vector2 position){
        return position;
    }
    public static Vector2 origin2BatchDraw(Vector2 position){
        return position;
    }
}
