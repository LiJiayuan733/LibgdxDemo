package com.thzs.app.datacoplite.util.stm32utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class Led1306Picture {
    public static final int PIC_CREATE_MODE_PAGE=0;
    public static final int LED_WIDTH=128;
    public int picCreateMode=PIC_CREATE_MODE_PAGE;
    protected FileHandle picFile;
    protected Pixmap pictureTemp;
    public Led1306Picture(FileHandle PicFile){
        this.picFile=PicFile;
        this.pictureTemp=new Pixmap(PicFile);
    }
    /**
     * 对图像二进制处理并生成对应的图像
     * */
    public byte[] CreateData(){
        if(picCreateMode==PIC_CREATE_MODE_PAGE){
            return CreatePicturePage();
        }
        return null;
    }
    public byte[] CreatePicturePage(){
        byte[] data=new byte[1024];
        Pixmap pic=PicToWhiteAndBlack();
        int index=0;
        int y=0,x=0;
        for(int page=0;page<8;page++){
            y=page*8;
            for(int col=0;col<128;col++){
                int c=0;
                for(int i=7;i>=0;i--){
                    c=c<<1;
                    c+=PicPixelIsWhite(pic,col,y+i);
                }
                data[index]=(byte)c;
                index++;
            }
        }
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
