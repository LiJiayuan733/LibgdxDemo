package com.thzs.app.datacoplite.util.grapices;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.Random;

public class MyColorTools {
    private static Random random=new Random();
    /**
     * @param level the start number
     * @param range the level+range is result
     * */
    public static Color RandomColor(float level,float range){
        float ASet=level!=-1?random.nextFloat()*range+level:1;
        return new Color(random.nextFloat(),random.nextFloat(),random.nextFloat(),ASet);
    }
    public static Color RandomColor(){
        return RandomColor(-1,0);
    }
    /**
     *  @param range the range from 0 to 255*3
     * */
    public static boolean ColorSameCheck(Color main,Color t,int range){
        if (range>255*3||range<0){
            return false;
        }else {
            //TODO: change the complete method
            int sum=755*3*(int) (Math.abs(main.r-t.r)+Math.abs(main.g-t.g)+Math.abs(main.b-t.b));
            if (sum<=range){
                return false;
            }else {
                return true;
            }
        }
    }
    /**
     * only use to test
     * */
    public static Pixmap getRandomColorPixmap(){
        Pixmap img=new Pixmap(640,640, Pixmap.Format.RGB888);
        Color a,b;
        a= MyColorTools.RandomColor();
        b=MyColorTools.RandomColor();
        while(!MyColorTools.ColorSameCheck(a,b,16*3)){
            b=MyColorTools.RandomColor();
        }
        img.setColor(a);
        img.fillRectangle(0,0, img.getWidth(), img.getHeight());
        img.setColor(b);
        img.fillCircle(320,320,30);
        return img;
    }
}
