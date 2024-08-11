package com.thzs.app.datacoplite.util.stm32utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.fazecast.jSerialComm.SerialPort;

import java.io.DataOutput;
import java.io.IOException;

public class Led9341Picture {
    public static final int PIC_CREATE_MODE_HOR=1;
    public static final int PIC_CREATE_MODE_VER=2;//图片被转动90度
    public static final int LED_WIDTH=320;
    public static final int LED_HEIGHT=320;
    public static final int LED_WIDTH_PAGE_SIZE=20;
    public static final int LED_HEIGHT_PAGE_SIZE=20;

    public int picCreateMode=PIC_CREATE_MODE_HOR;
    protected FileHandle picFile;
    protected Pixmap pictureTemp;                //原图存档
    protected byte[][][] pictureFor565=new byte[LED_WIDTH][LED_HEIGHT][2];
    public Led9341Picture(FileHandle PicFile){
        this.picFile=PicFile;
        this.pictureTemp=new Pixmap(LED_WIDTH,LED_HEIGHT,Pixmap.Format.RGBA8888);
        AdaptedPicture(picFile);
        //TODO: 完成图片数据转换
    }
    public void AdaptedPicture(FileHandle PicFile){
        Pixmap pic=new Pixmap(PicFile);
        float picWidth=pic.getWidth();
        float picHeight=pic.getHeight();
        boolean wh=(picWidth/picHeight>LED_WIDTH/LED_HEIGHT);
        if(wh){
            float mw=picWidth/LED_WIDTH;
            float mHeight=picHeight/mw;
            pictureTemp.drawPixmap(pic,0,0,(int) picWidth,(int) picHeight,0,(int) (LED_HEIGHT-mHeight)/2,LED_WIDTH,(int) mHeight);
        }else{
            float mh=picHeight /LED_HEIGHT;
            float mWidth=picWidth/mh;
            pictureTemp.drawPixmap(pic,0,0,(int) picWidth,(int) picHeight,(int) (LED_WIDTH-mWidth)/2,0,(int) mWidth,LED_HEIGHT);
        }
    }
    public void PictureConvert(){
        byte[] rgb=new byte[3];
        for(int i=0;i<pictureTemp.getWidth();i++){
            for (int j=0;j<pictureTemp.getHeight();j++){
                Color c=new Color(pictureTemp.getPixel(i,j));
                rgb[0]= (byte) (0b11111*c.b);
                rgb[1]= (byte) (0b111111*c.g);
                rgb[2]= (byte) (0b11111*c.r);
                pictureFor565[i][j][0]= (byte) ((rgb[0]&0b11111)|((0b111111&rgb[1])<<5));
                pictureFor565[i][j][1]= (byte) (((rgb[1]&0b111111)>>3)|((0b11111&rgb[2])<<3));
            }
        }
    }
    public void PictureSend(SerialPort port, int index){
        int unitX=LED_WIDTH/LED_WIDTH_PAGE_SIZE,unitY=LED_HEIGHT/LED_HEIGHT_PAGE_SIZE;
        for(int j=(index/unitX)*20;j<(index/unitX+1)*20;j++){
            for(int i=(index%unitX)*20;i<(index%unitX+1)*20;i++){
                port.writeBytes(pictureFor565[i][j],2);
            }
        }
    }
    public void PictureToFile(DataOutput writer, int index) throws IOException {
        int m=index*256;
        for(int i=0;i<256;i++){
            writer.write(pictureFor565[(i+m)%LED_WIDTH][(i+m)/LED_WIDTH],0,2);
        }
    }
    public void PictureWrite(SerialPort port, int index){
        int m=index*256;
        for(int i=0;i<256;i++){
            port.writeBytes(pictureFor565[(i+m)%LED_WIDTH][(i+m)/LED_WIDTH],2);
        }
    }
    public void dispose(){
        pictureTemp.dispose();
    }
}
