package com.thzs.app.datacoplite.util.stm32utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class Led1306Picture {
    public static final int PIC_CREATE_MODE_PAGE=0;
    public static final int PIC_CREATE_MODE_HOR=1;
    public static final int PIC_CREATE_MODE_VER=2;
    public static final int LED_WIDTH=128;
    public static final int LED_HEIGHT=64;
    public static final int LED_PAGE_NUM=8;
    public int picCreateMode=PIC_CREATE_MODE_PAGE;
    protected FileHandle picFile;
    protected Pixmap pictureTemp;                //原图存档
    //TODO: need dispose
    protected Pixmap pictureForWhiteAndBlack;    //黑白位图
    public Led1306Picture(FileHandle PicFile){
        this.picFile=PicFile;
        this.pictureTemp=new Pixmap(PicFile);
        this.pictureForWhiteAndBlack=PicToWhiteAndBlack();
    }
    /**
     * 对图像二进制处理并生成对应的图像
     * */
    public byte[] CreateData(){
        if(picCreateMode==PIC_CREATE_MODE_PAGE){
            return CreatePicturePage();
        }else if(picCreateMode==PIC_CREATE_MODE_HOR){
            return CreatePictureHor();
        }else if(picCreateMode==PIC_CREATE_MODE_VER){
            return CreatePictureVer();
        }
        return null;
    }
    /**
     * 产生页数据
     */
    public byte[] CreatePicturePage(){
        byte[] data=new byte[LED_WIDTH*LED_HEIGHT/8];
        int index=0;
        int y=0,x=0;
        for(int page=0;page<LED_PAGE_NUM;page++){
            y=page*8;
            for(int col=0;col<LED_WIDTH;col++){
                int c=0;
                for(int i=LED_PAGE_NUM-1;i>=0;i--){
                    c=c<<1;
                    c+=PicPixelIsWhite(pictureForWhiteAndBlack,col,y+i);
                }
                data[index]=(byte)c;
                index++;
            }
        }
        return data;
    }
    /**
     * 产生横式数据
     */
    public byte[] CreatePictureHor(){
        byte[] data=new byte[LED_WIDTH*LED_HEIGHT/8];
        //TODO: to finish
        return data;
    }
    /**
     * 产生列式数据
     */
    public byte[] CreatePictureVer(){
        byte[] data=new byte[LED_WIDTH*LED_HEIGHT/8];
        //TODO: to finish
        return data;
    }
    /**
     * 处理为灰白图像
     * */
    public Pixmap PicToWhiteAndBlack(){
        Pixmap pictureTran=new Pixmap(pictureTemp.getWidth(),pictureTemp.getHeight(), Pixmap.Format.RGBA8888);
        pictureTran.setColor(Color.BLACK);
        pictureTran.fill();
        pictureTran.setColor(Color.WHITE);
        for (int i = 0; i < pictureTemp.getWidth(); i++){
            for(int j = 0; j < pictureTemp.getHeight(); j++){
                Color c=new Color(pictureTemp.getPixel(i,j));
                c.sub(0.5f,0.5f,0.5f,0);
                if(c.r+c.g+c.b>0f){
                    pictureTran.drawPixel(i,j);
                }
            }
        }
        return pictureTran;
    }
    public int PicPixelIsWhite(Pixmap pic,int x,int y){
        return pic.getPixel(x,y)==Color.WHITE.toIntBits() ?1:0;
    }
}
