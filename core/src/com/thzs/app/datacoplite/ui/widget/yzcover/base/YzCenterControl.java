package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.graphics.Color;

public class YzCenterControl {
    //控件基本设置
    public static Color WINDOW_TOP_BAR_FONT_COLOR = Color.valueOf("#999999FF");
    public static Color WINDOW_EDITAREA_FONT_COLOR = Color.valueOf("#999999FF");

    //组件预加载开关，当不使用时可关闭
    public static boolean USE=true;
    /**
     * 其他第三方控件可以通过此接口使其支持从YzGdxQuery聚焦切换
     * */
    public interface YzControl{
        /**
         * @param to 将聚焦的控件
         * */
        public void CancelFocus(YzControl to);
        public void SetFocus(YzControl from);
        public boolean IsFocus();
    }
    //控件焦点
    public static YzControl focusWidget=null;
    public static void ChangeFocus(YzWidget to){
        if(focusWidget!=null){
            focusWidget.CancelFocus(to);
        }
        to.SetFocus(focusWidget);
        focusWidget=to;
    }
    //默认字体配置
    public static int DEFAULT_FONT_SIZE=30;
    public static int DEFAULT_WINDOW_TOP_BAR_FONT_SIZE=30;
}
