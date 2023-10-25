package com.thzs.app.datacoplite.util.API;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.util.JsonFind;
import com.thzs.app.datacoplite.util.Native.MutApiSupport;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class Auto {
    public WinDef.HWND HWND;
    public ScriptEngine engine;
    public ScriptEngineManager manager;
    public Auto(){
        HWND= User32.INSTANCE.FindWindow(null, Game.APP_TITLE);
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("javascript");
        engine.put("Views", Views.views);
        engine.put("Auto", this);
    }
    public void run(FileHandle fileHandle){
        if(fileHandle.exists()){
            String script=fileHandle.readString("UTF-8");
            try {
                engine.eval(script);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<Yolo.YoloResult> Yolo(String imgPath){
        JsonFind jf= Yolo.SendRequestDefault(imgPath);
        if (jf==null){
            return null;
        }else {
            return Yolo.getData(jf,-1);
        }
    }
    public void hidWindow(){
        User32.INSTANCE.ShowWindow(HWND, User32.SW_HIDE);
    }
    public void shwWindow(){
        User32.INSTANCE.ShowWindow(HWND, User32.SW_SHOWNORMAL);
    }
    public void capture(String path){
        MutApiSupport.SaveCapture(MutApiSupport.getWindowHandle(),path);
    }
    public void click(int x,int y){
        MutApiSupport.Click(x,y);
    }
    public void click(Vector2 pos){
        MutApiSupport.Click((int) pos.x, (int) pos.y);
    }
    public void log(String msg){
        Log.i("Script:"+msg);
    }
}
