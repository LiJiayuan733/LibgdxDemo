package com.thzs.app.datacoplite.util.API;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class OCRDataLine {
    public String Words;
    public Vector2 LowerLeft,LowerRight,UpperLeft,UpperRight;
    public float Score;
    public OCRDataLine(String Words,Vector2 LowerLeft,Vector2 LowerRight,Vector2 UpperLeft,Vector2 UpperRight,float Score){
        this.Words=Words;
        this.LowerLeft=LowerLeft;
        this.LowerRight=LowerRight;
        this.UpperLeft=UpperLeft;
        this.UpperRight=UpperRight;
        this.Score=Score;
    }
    public Rectangle getRectangle(){
        return new Rectangle(LowerLeft.x,LowerLeft.y,LowerRight.x-LowerLeft.x,UpperLeft.y-LowerLeft.y);
    }
}
