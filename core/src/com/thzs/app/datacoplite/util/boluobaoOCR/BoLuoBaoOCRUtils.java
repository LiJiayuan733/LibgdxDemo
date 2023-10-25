package com.thzs.app.datacoplite.util.boluobaoOCR;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BoLuoBaoOCRUtils {
    interface ICallBack{
        public boolean callback();
    }
    /**
     * only be used on Windows
     * requires opencv
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * */
    public static Thread ImgTo2(final FileHandle fileDir, final ICallBack callBack) {
        Thread thread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    BoLuoBaoPythonJavaCalled.ImgTo2(fileDir);
                    if(callBack!=null) {
                        callBack.callback();
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }
    /**
     * only be used on Windows
     * requires opencv
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * @param x1,y1,x2,y2 are the area's position of the image that you need. The original position was set to left top of screen.
     * */
    public static Thread ImgCut(final FileHandle fileDir, final int x1, final int y1, final int x2, final int y2, final ICallBack callBack){
        Thread thread=new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    BoLuoBaoPythonJavaCalled.ImgCut(fileDir,x1,y1,x2,y2);
                    if(callBack!=null) {
                        callBack.callback();
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }
    /**
     * only be used on Windows
     * requires easyocr json numpy
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * @param num is used to set the number of result when need to save to file
     * @param count the file's amount that need to ocr
     * @param start the file start serial number
     * @param start_name the file name of the serial
     * @param temp the start serial number for result file
     * <br/><div color="red">如果num不为amount的整倍数则会有遗漏</div>
     * */
    public static Thread ImgOCR(final FileHandle fileDir, final int num, final int count, final int start, final String start_name, final int temp, final ICallBack callBack){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BoLuoBaoPythonJavaCalled.ImgOCR(fileDir,num,count,start,start_name,temp);
                    if(callBack!=null) {
                        callBack.callback();
                    }
                } catch (IOException | InterruptedException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }
    /**
     * 用于将结果文件进行解析合成最后的小说文件
     * @param path the parent dir of the result file
     * @param startName the result file Start signal
     * @param start the start serial number
     * @param count the file count
     * @param x,x2 段落区分
     * */
    public static void ResultHandlerONE(FileHandle path,String startName,int start,int count,int x,int x2) {
        //x=110,x2=880
        try {
            BoLuoBaoPythonJavaCalled.ResultHandlerONE(path,startName,start,count,x,x2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
