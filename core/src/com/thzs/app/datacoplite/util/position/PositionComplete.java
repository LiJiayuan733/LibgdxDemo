package com.thzs.app.datacoplite.util.position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 用于对齐控件与优化布局
 * */
//TODO: 待完成
public class PositionComplete {
    public static float unit_X= (float) Gdx.graphics.getWidth() /1000;
    public static float unit_Y= (float) Gdx.graphics.getHeight() /1000;
    public static float Y_X=unit_X/unit_Y;
    public static float X_Y=unit_Y/unit_X;
    /**
     * @param T_distance 距离 t 往上边移动
     * @param R_distance 距离 t 往右边移动
     * */
    public static Actor RightTopTRDistance(Actor main,Actor t,float T_distance,float R_distance){
        main.setPosition(t.getX()+R_distance+t.getWidth(),t.getY()+ t.getHeight()+T_distance);
        return main;
    }
    public static Actor RightTopTDistance(Actor main,Actor t,float T_distance){
        return RightTopTRDistance(main,t,T_distance,0);
    }
    public static Actor RightTopRDistance(Actor main,Actor t,float R_distance){
        return RightTopTRDistance(main,t,0,R_distance);
    }
    /**
     * @param T_distance 将窗口分成1000份 距离 t 往上的份数
     * @param R_distance 将窗口分成1000份 距离 t 往右的份数
     * */
    public static Actor UnitRightTopTRDistance(Actor main,Actor t,float T_distance,float R_distance){
        return RightTopTRDistance(main,t,T_distance*unit_Y,R_distance*unit_X);
    }
    public static Actor UnitRightTopTDistance(Actor main,Actor t,float T_distance){
        return RightTopTRDistance(main,t,T_distance*unit_Y,0);
    }
    public static Actor UnitRightTopRDistance(Actor main,Actor t,float R_distance){
        return RightTopTRDistance(main,t,0,R_distance*unit_X);
    }

    public static Actor RightBottomBRDistance(Actor main, Actor t, float B_distance, float R_distance){
        main.setPosition(t.getX()+R_distance+t.getWidth(), t.getY()-main.getHeight()-B_distance);
        return main;
    }
    public static Actor RightBottomBDistance(Actor main, Actor t, float B_distance){
        return RightBottomBRDistance(main, t,B_distance,0);
    }
    public static Actor RightBottomRDistance(Actor main, Actor t, float R_distance){
        return RightBottomBRDistance(main,t,0,R_distance);
    }
    public static Actor UnitRightBottomRBDistance(Actor main, Actor t,float B_distance,float R_distance) {
        return RightBottomBRDistance(main,t,B_distance*unit_Y,R_distance*unit_X);
    }
    public static Actor UnitRightBottomRDistance(Actor main, Actor t, float R_distance){
        return RightBottomRDistance(main,t,R_distance*unit_X);
    }
    public static Actor UnitRightBottomBDistance(Actor main, Actor t,float B_distance){
        return RightBottomBDistance(main, t, B_distance*unit_Y);
    }

    public static Actor LeftTopTLDistance(Actor main, Actor t, float T_distance,float L_distance){
        main.setPosition(t.getX()-main.getWidth()-L_distance, t.getY()+t.getHeight()+T_distance);
        return main;
    }
    public static Actor LeftTopTDistance(Actor main, Actor t,float T_distance){
        return LeftTopTLDistance(main, t, T_distance,0);
    }
    public static Actor LeftTopLDistance(Actor main, Actor t,float L_distance){
        return LeftTopTLDistance(main, t, 0,L_distance);
    }
    public static Actor UnitLeftTopTLDistance(Actor main, Actor t,float T_distance,float L_distance) {
        return LeftTopTLDistance(main, t, T_distance*unit_Y,L_distance*unit_X);
    }
    public static Actor UnitLeftTopTDistance(Actor main, Actor t,float T_distance){
        return LeftTopTDistance(main, t,T_distance*unit_Y);
    }
    public static Actor UnitLeftTopLDistance(Actor main, Actor t,float L_distance){
        return LeftTopLDistance(main, t,L_distance*unit_X);
    }

    public static Actor LeftBottomBLDistance(Actor main,Actor t,float B_distance,float L_distance){
        main.setPosition(t.getX()-main.getWidth()-L_distance,t.getY()-main.getHeight() - B_distance);
        return main;
    }
    public static Actor LeftBottomBDistance(Actor main, Actor t,float B_distance){
        return LeftBottomBLDistance(main,t,B_distance,0);
    }
    public static Actor LeftBottomLDistance(Actor main, Actor t,float L_distance){
        return LeftBottomBLDistance(main,t,0,L_distance);
    }
    public static Actor UnitLeftBottomBLDistance(Actor main, Actor t,float B_distance,float L_distance){
        return LeftBottomBLDistance(main, t,B_distance*unit_Y,L_distance*unit_X);
    }
    public static Actor UnitLeftBottomBDistance(Actor main,Actor t,float B_distance){
        return LeftBottomBDistance(main,t, B_distance*unit_Y);
    }
    public static Actor UnitLeftBottomLDistance(Actor main,Actor t,float L_distance){
        return LeftBottomLDistance(main, t,L_distance*unit_X);
    }

    public static Actor LeftCenterDistance(Actor main, Actor t,float L_distance){
        main.setPosition(t.getX()-main.getWidth()-L_distance,t.getY()+t.getHeight()/2-main.getHeight()/2);
        return main;
    }
    public static Actor RightCenterDistance(Actor main, Actor t,float R_distance){
        main.setPosition(t.getX()+t.getWidth()+R_distance,t.getY()+t.getHeight()/2-main.getHeight()/2);
        return main;
    }
    public static Actor TopCenterDistance(Actor main,Actor t,float T_distance){
        main.setPosition(t.getX()+t.getWidth()/2-main.getWidth()/2,t.getY()+t.getHeight() + T_distance);
        return main;
    }
    public static Actor BottomCenterDistance(Actor main,Actor t,float B_distance){
        main.setPosition(t.getX()+t.getWidth()/2-main.getWidth()/2,t.getY() -main.getHeight()-B_distance);
        return main;
    }
    public static Actor UnitLeftCenterDistance(Actor main,Actor t,float L_distance){
        return LeftCenterDistance(main,t,L_distance*unit_X);
    }
    public static Actor UnitRightCenterDistance(Actor main,Actor t,float R_distance){
        return RightCenterDistance(main,t,R_distance*unit_X);
    }
    public static Actor UnitTopCenterDistance(Actor main,Actor t,float T_distance){
        return TopCenterDistance(main,t,T_distance*unit_Y);
    }
    public static Actor UnitBottomCenterDistance(Actor main,Actor t,float B_distance){
        return BottomCenterDistance(main,t,B_distance*unit_Y);
    }
}