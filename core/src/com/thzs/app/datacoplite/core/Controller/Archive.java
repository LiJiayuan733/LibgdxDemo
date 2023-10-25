package com.thzs.app.datacoplite.core.Controller;

import com.thzs.app.datacoplite.util.position.Position;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Archive implements Serializable {
    /**存档已进行时间(ms)**/
    public long time;
    public Position position;
    public int gold;
    public void print(String message) {
        System.out.println(message);
    }
    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    static{format.setTimeZone(TimeZone.getTimeZone("UTF-0"));}
    public String time(){
//		return null;
        return format.format((Object)time);
    }
}
