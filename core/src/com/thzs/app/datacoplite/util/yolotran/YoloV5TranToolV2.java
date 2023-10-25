package com.thzs.app.datacoplite.util.yolotran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.ui.widget.Image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class YoloV5TranToolV2 implements Disposable {
    public YoloImgManger imgManger;
    public YoloBackgroundManager backgroundManager;
    public YoloIconManager iconManager;
    public int Count;
    public float timeTemp=0f;
    public String imgOutputPath,labelOutputPath;
    public Image viewer;
    public YoloV5TranToolV2(int createImgWidth,int createImgHeight,String imgOutPutPath,String labelOutPutPath,String iconDirectory,String BackgroundDirectory,int eachCount,Image viewer){
        imgManger=new YoloImgManger(createImgWidth,createImgHeight);
        backgroundManager=new YoloBackgroundManager(BackgroundDirectory);
        iconManager=new YoloIconManager(iconDirectory);
        Count=iconManager.completeCount(eachCount);
        backgroundManager.setCount(eachCount);
        this.imgOutputPath=imgOutPutPath;
        this.labelOutputPath=labelOutPutPath;
        this.viewer=viewer;
    }
    public int index=0;
    public Texture viewTexture=null;
    public void update(float delta){
        timeTemp+=delta;
        if(timeTemp>0.1f&&index<Count){
            timeTemp=0f;
            backgroundManager.toSet(index,imgManger);
            iconManager.toSet(index,imgManger);
            try {
                if(viewTexture!=null)
                    viewTexture.dispose();

                output(imgManger.pixmap,iconManager.label,index+1);

                viewTexture=new Texture(imgManger.pixmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            viewer.setDrawable(new TextureRegionDrawable(viewTexture));
            index++;
        }
    }
    public void output(Pixmap temp, String label, int Count) throws IOException {
        String formatCount="000000000000";
        String count= String.valueOf(Count);
        String format=formatCount.substring(count.length())+count;
        String outI=(imgOutputPath.endsWith("/")?imgOutputPath:imgOutputPath+"/")+format+".png";
        String outL=(labelOutputPath.endsWith("/")?labelOutputPath:labelOutputPath+"/")+format+".txt";
        PixmapIO.writePNG(Gdx.files.absolute(outI),temp);
        FileWriter fw=new FileWriter(new File(outL));
        fw.write(label);
        fw.flush();
        fw.close();
    }

    @Override
    public void dispose() {
        imgManger.dispose();
        iconManager.dispose();
        backgroundManager.dispose();
        if (viewTexture!=null)
            viewTexture.dispose();
    }
}
