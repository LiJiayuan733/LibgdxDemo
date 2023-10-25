package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

public interface SelectListSettingSupport {
    /**
     * setData()
     * getPreSize()
     * refresh()
     * */
    public static String DATA_INDEX="INDEX";
    public static String DATA_FRAME_INDEX="FRAME_INDEX";
    public Pixmap refresh(SelectList selectGroupSettingSupport,Pixmap pixmap);
    public void setData(SelectList selectListSettingSupport,Map<String,Object> data);
    public void setData(SelectList selectListSettingSupport,String key,Object value);
    public Rectangle getPreSize(SelectList selectListSettingSupport);
    public int getIndex(SelectList selectListSettingSupport);
    public Object getData(SelectList selectListSettingSupport,String key);
}
