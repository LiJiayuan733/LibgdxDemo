package com.thzs.app.datacoplite.util.grapices;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.XmlReader;
/**
 * XmlPixmap 的绘制部分拆分
 * */
public class XmlPixmapF {
    public XmlReader.Element element;
    public XmlPixmapF(){
    }
    public static String ATTR_COLOR="color";
    public static String ATTR_FILL="fill";
    public static String ATTR_X="x";
    public static String ATTR_Y="y";
    public static String ATTR_X1="x1";
    public static String ATTR_Y1="y1";
    public static String ATTR_X2="x2";
    public static String ATTR_Y2="y2";
    public static String ATTR_X3="x3";
    public static String ATTR_Y3="y3";
    public static String ATTR_HEIGHT="height";
    public static String ATTR_WIDTH="width";
    public static String ATTR_ATTACH_WIDTH="AttachWidth"; //将高度设置为与宽度相同
    public static String ATTR_ATTACH_HEIGHT="AttachHeight";
    public static String ATTR_RADIUS="radius";
    public static String ATTR_TYPE="type";
    public static String ATTR_SRC="src";
    public Color BGColor=Color.BLACK;
    public Boolean FGFill=true,FGAW=false,FGAH=false;
    public String name="";
    public int x,y,x1,y1,x2,y2,x3,y3,height,width,radius;
    public FileHandle imgFile;
    /**
     * 解析参数
     * */
    public XmlPixmapF parse(XmlReader.Element element){
        this.element = element;
        this.FGFill = true;
        this.FGAW = false;
        this.FGAH = false;
        this.name = "";
        //Color
        if(element.hasAttribute(ATTR_COLOR)){
            String attr_color = element.getAttribute(ATTR_COLOR);
            if (attr_color.startsWith("#")) {
                Color.valueOf(attr_color, BGColor);
            } else {
                BGColor = Colors.get(attr_color);
            }
        }
        //Fill
        if(element.hasAttribute(ATTR_FILL)) {
            String attr_fill = element.getAttribute(ATTR_FILL);
            FGFill = attr_fill.toLowerCase().equals("true");
        }
        if(element.hasAttribute(ATTR_ATTACH_WIDTH)) {
            String attr_aw = element.getAttribute(ATTR_ATTACH_WIDTH);
            FGAW = attr_aw.toLowerCase().equals("true");
        }
        if(element.hasAttribute(ATTR_ATTACH_HEIGHT)){
            String attr_ah = element.getAttribute(ATTR_ATTACH_HEIGHT);
            FGAH = attr_ah.toLowerCase().equals("true");
        }

        if(element.hasAttribute(ATTR_X)){
            x=Integer.parseInt(element.getAttribute(ATTR_X));
        }
        if (element.hasAttribute(ATTR_Y)) {
            y=Integer.parseInt(element.getAttribute(ATTR_Y));
        }
        if (element.hasAttribute(ATTR_X1)) {
            x1=Integer.parseInt(element.getAttribute(ATTR_X1));
        }
        if (element.hasAttribute(ATTR_Y1)){
            y1=Integer.parseInt(element.getAttribute(ATTR_Y1));
        }
        if (element.hasAttribute(ATTR_X2)){
            x2=Integer.parseInt(element.getAttribute(ATTR_X2));
        }
        if(element.hasAttribute(ATTR_Y2)){
            y2=Integer.parseInt(element.getAttribute(ATTR_Y2));
        }
        if (element.hasAttribute(ATTR_X3)){
            x3=Integer.parseInt(element.getAttribute(ATTR_X3));
        }
        if (element.hasAttribute(ATTR_Y3)){
            y3=Integer.parseInt(element.getAttribute(ATTR_Y3));
        }
        if (element.hasAttribute(ATTR_HEIGHT)){
            height=Integer.parseInt(element.getAttribute(ATTR_HEIGHT));
        }
        if(element.hasAttribute(ATTR_WIDTH)){
            width=Integer.parseInt(element.getAttribute(ATTR_WIDTH));
        }
        if(element.hasAttribute(ATTR_RADIUS)){
            radius=Integer.parseInt(element.getAttribute(ATTR_RADIUS));
        }
        if(element.hasAttribute(ATTR_TYPE)&&element.hasAttribute(ATTR_SRC)){
            String type=element.getAttribute(ATTR_TYPE);
            String path=element.getAttribute(ATTR_SRC);
            switch (type){
                case "INTERNET":
                    imgFile = Gdx.files.internal(path);
                    break;
                case "ABSOLUTE":
                    imgFile = Gdx.files.absolute(path);
                    break;
                case "INTERNAL":
                    imgFile = Gdx.files.internal(path);
                    break;
                case "EXTERNAL":
                    imgFile = Gdx.files.external(path);
                    break;
                default:
                    imgFile = null;
                    break;
            }
        }
        this.name = element.getName();
        return this;
    }
    /**
     * 应用绘制
     * */
    public void set(XmlPixmap pixmap,float unit_X,float unit_Y){
        if(BGColor!=null){
            pixmap.setColor(BGColor);
        }
        switch (name){
            case "rectangle":
                if(FGFill){
                    pixmap.fillRectangle((int) (unit_X * x), (int) (unit_Y * y), FGAH?(int) (unit_Y * height):(int) (unit_X * width),FGAW?(int) (unit_X * width):(int) (unit_Y * height));
                }else {
                    pixmap.drawRectangle((int) (unit_X * x), (int) (unit_Y * y), FGAH?(int) (unit_Y * height):(int) (unit_X * width),FGAW?(int) (unit_X * width):(int) (unit_Y * height));
                }
                break;
            case "line":
                pixmap.drawLine((int) (unit_X*x1), (int) (unit_Y*y1), (int) (unit_X*x2), (int) (unit_Y*y2));
                break;
            case "circle":
                if (FGFill){
                    pixmap.fillCircle((int) (unit_X * x), (int) (unit_Y * y), (int) (unit_Y * radius));
                }else {
                    pixmap.drawCircle((int) (unit_X * x), (int) (unit_Y * y), (int) (unit_Y*radius));
                }
                break;
            case "triangle":
                pixmap.fillTriangle((int) (unit_X*x1), (int) (unit_Y*y1), (int) (unit_X*x2), (int) (unit_Y*y2), (int) (unit_X*x3), (int) (unit_Y*y3));
                break;
            case "img":
                if (imgFile==null){
                    break;//file is null
                }
                Pixmap pix=new Pixmap(imgFile);
                pixmap.drawPixmap(pix,0,0, pix.getWidth(), pix.getHeight(), (int) (unit_X * x), (int) (unit_Y * y),FGAH?(int) (unit_Y * height):(int) (unit_X * width),FGAW?(int) (unit_X * width):(int) (unit_Y * height));
                pix.dispose();
                break;
            default:
                break;
        }
    }
}
