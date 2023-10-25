package com.thzs.app.datacoplite.util.Native.windows;

public class NativeWindowsSupportUtils {
    public native String BoxWindow(String message);
    public static native String FileChooseWindow(long window);
    public static native String DirectoryChooseWindow(long window);
    public static native void LongTest(long window);
    public static native void SetCursorPos(int x,int y);
    public static native void Click(int x,int y);
    public static native int GetCursorPos();
    public static native void SaveCapture(long window,String path);
    public static native long GetLastError();
    public static native long GetHWND(long window);
}
