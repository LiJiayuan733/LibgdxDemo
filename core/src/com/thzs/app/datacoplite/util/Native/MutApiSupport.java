package com.thzs.app.datacoplite.util.Native;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.thzs.app.datacoplite.util.Native.windows.NativeWindowsSupportUtils;

import java.awt.*;

public class MutApiSupport {
    static {
        NativeSetting.init();
    }
    public static long getWindowHandle(){
        return ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
    }
    public static String SystemFileChoose(){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                return NativeWindowsSupportUtils.FileChooseWindow(((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle());
            }else{
                return "unsupported";
            }
        }else{
            return "unsupported";
        }
    }
    public static String SystemFolderChoose(){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                return NativeWindowsSupportUtils.DirectoryChooseWindow(((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle());
            }else{
                return "unsupported";
            }
        }else{
            return "unsupported";
        }
    }
    public static void SetCursorPos(int x, int y){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                NativeWindowsSupportUtils.SetCursorPos(x,y);
            }else{
                //TODO: other system operations
            }
        }else{
            //TODO: other method
        }
    }
    public static void Click(int x, int y){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                NativeWindowsSupportUtils.Click(x,y);
            }else{
                //TODO: other system operations
            }
        }else{
            //TODO: other method
        }
    }
    public static Point GetCursorPos(){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                int re=NativeWindowsSupportUtils.GetCursorPos();
                int x=re>>16;
                int y=re&0x0000FFFF;
                return new Point(x,y);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    public static void SaveCapture(long window,String path){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                NativeWindowsSupportUtils.SaveCapture(window,path);
            }else{
                //TODO: other system operations
            }
        }else{
            //TODO: other method
        }
    }
    public static long GetLastError(){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                return NativeWindowsSupportUtils.GetLastError();
            }else{
                //TODO: other system operations
            }
        }else{
            //TODO: other method
        }
        return 0;
    }
    public static WinDef.HWND GetHWND(long window){
        if(Gdx.app.getType()== Application.ApplicationType.Desktop){
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                return new WinDef.HWND(new Pointer(NativeWindowsSupportUtils.GetHWND(window)));
            }else{
                //TODO: other system operations
            }
        }else{
            //TODO: other method
        }
        return null;
    }
}
