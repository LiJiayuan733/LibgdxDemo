package com.thzs.app.datacoplite.util.yolotran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.widget.Image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Deprecated
public class YoloV5TranTool implements Disposable {
    public Pixmap temp;
    public Image viewer;
    public Texture viewerTexture;
    public Random random;

    public Pixmap iconImage;
    public String iconDir;
    public HashMap<Integer,String> iconFiles;
    public int iconIndex=0;

    public Pixmap BackgroundPng;
    public String BackgroundDir;
    public ArrayList<String> BackgroundFiles;
    public int PWidth, PHeight;
    public YoloV5TranTool(int width,int height,Image Viewer,int count,String outputImg,String outputLabel,String iconDir,String BackgroundDIr) {
        random=new Random();
        PWidth=width;
        PHeight=height;
        BackgroundDir=BackgroundDIr;
        this.iconDir=iconDir;
        this.count = count;
        this.outputImg = outputImg;
        this.outputLabel = outputLabel;
        this.viewer = Viewer;
        temp = new Pixmap(width,height, Pixmap.Format.RGBA8888);
        temp.setColor(0xFFFFFFFF);
        temp.fill();
        searchBackground();
        searchICON();
    }

    private void searchICON() {
        iconFiles=new HashMap<>();
        File d=new File(iconDir);
        for(File f:d.listFiles()) {
            if (f.isFile()&&isPic(f)){
                int cls=Integer.parseInt(f.getName().split("\\.")[0]);
                iconFiles.put(cls,f.getAbsolutePath());
            }
        }
        if(iconFiles.size()!=0){
            for (Map.Entry<Integer,String> e:iconFiles.entrySet()){
                if (e.getKey()==iconIndex){
                    iconImage=new Pixmap(Gdx.files.absolute(e.getValue()));
                    typeID=e.getKey();
                }
            }
        }
    }

    public void searchBackground(){
        BackgroundFiles=new ArrayList<>();
        File d=new File(BackgroundDir);
        for(File f: d.listFiles()){
            if (f.isFile()&&isPic(f))
                BackgroundFiles.add(f.getAbsolutePath());
        }
        if (BackgroundFiles.size()!=0){
            BackgroundPng=new Pixmap(Gdx.files.absolute(BackgroundFiles.get(BackgroundIndex)));
        }
    }
    public boolean isPic(File file){
        String fileName=file.getName();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg");
    }
    protected float tem=0;
    public int count=0;
    public int fileSum=1;
    public int sum=1;
    public String outputImg,outputLabel;
    public int BackgroundIndex=0;
    public void update(float delta) {
        tem+=delta;
        if((tem>0.1f&&sum<=count)&&(iconIndex<iconFiles.size())) {
            updateImg();
            updateTemp();
            updateViewer();
            tem=0;
            sum++;
            if(sum==count+1){
                Log.i("Yolo:"+fileSum);
                sum=1;
                iconIndex++;
                if(iconIndex<iconFiles.size()){
                    for (Map.Entry<Integer,String> e:iconFiles.entrySet()){
                        if (e.getKey()==iconIndex){
                            iconImage=new Pixmap(Gdx.files.absolute(e.getValue()));
                            typeID=e.getKey();
                            BackgroundIndex=0;
                            BackgroundPng = new Pixmap(Gdx.files.absolute(BackgroundFiles.get(BackgroundIndex)));
                        }
                    }
                }
            }
            fileSum++;
        }
    }
    public void updateImg(){
        if(FGRandomBackGround) {
            int cb = (int) Math.ceil((double) (count-1) / BackgroundFiles.size());
            if (BackgroundIndex < (int) sum / cb) {
                BackgroundIndex++;
                BackgroundPng = new Pixmap(Gdx.files.absolute(BackgroundFiles.get(BackgroundIndex)));
            }
        }
    }
    public boolean FGRandomColor=false;
    public boolean FGRandomBackGround=true;
    public boolean FGRandomSize=true;
    public Vector2 FGRandomSizeRange=new Vector2(0.4f,0.9f);
    public int typeID=0;
    public void updateTemp(){
        if(FGRandomColor){
            Color c=new Color(random.nextFloat(),random.nextFloat(),random.nextFloat(),1);
            temp.setColor(c);
            temp.fill();
        }else if(FGRandomBackGround){
            float x = (float) Math.floor(random.nextInt(BackgroundPng.getWidth()-PWidth));
            float y = (float) Math.floor(random.nextInt(BackgroundPng.getHeight()-PHeight));
            temp.drawPixmap(BackgroundPng, (int) x, (int) y,PWidth,PHeight,0,0,PWidth,PHeight);
        }else {
            temp.setColor(0xFFFFFFFF);
            temp.fill();
        }

        if(FGRandomSize){
            float scale=FGRandomSizeRange.x+random.nextFloat()*(FGRandomSizeRange.y-FGRandomSizeRange.x);
            float height= (float) Math.floor(iconImage.getHeight()*scale);
            float width= (float) Math.floor(iconImage.getWidth()*scale);
            float x = (float) Math.floor(width/2f+random.nextFloat()*(PWidth-width));
            float y = (float) Math.floor(height/2f+random.nextFloat()*(PHeight-height));
            while (x+width>PWidth||y+height>PHeight){
                scale=FGRandomSizeRange.x+random.nextFloat()*(FGRandomSizeRange.y-FGRandomSizeRange.x);
                height= (float) Math.floor(iconImage.getHeight()*scale);
                width= (float) Math.floor(iconImage.getWidth()*scale);
                x = (float) Math.floor(width/2f+random.nextFloat()*(PWidth-width));
                y = (float) Math.floor(height/2f+random.nextFloat()*(PHeight-height));
            }
            String labelString=typeID+" "+(x+width/2)/PWidth+" "+(y+height/2)/PHeight+" "+width/PWidth+" "+height/PHeight;
            temp.drawPixmap(iconImage,0,0,iconImage.getWidth(),iconImage.getHeight(),(int)x,(int)y,(int)width,(int)height);
            try {
                output(temp,labelString,fileSum);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(viewerTexture!=null){
            viewerTexture.dispose();
            viewerTexture=null;
        }
        viewerTexture=new Texture(temp);
    }
    public void updateViewer(){
        viewer.setDrawable(new TextureRegionDrawable(viewerTexture));
    }
    public void output(Pixmap temp,String label,int Count) throws IOException {
        String formatCount="000000000000";
        String count= String.valueOf(Count);
        String format=formatCount.substring(count.length())+count;
        String outI=(outputImg.endsWith("/")?outputImg:outputImg+"/")+format+".png";
        String outL=outputLabel.endsWith("/")?outputLabel:outputLabel+"/"+format+".txt";
        PixmapIO.writePNG(Gdx.files.absolute(outI),temp);
        FileWriter fw=new FileWriter(new File(outL));
        fw.write(label);
        fw.flush();
        fw.close();
    }
    @Override
    public void dispose() {
        temp.dispose();
        if(viewerTexture!=null){
            viewerTexture.dispose();
            viewerTexture=null;
        }
        if(BackgroundPng!=null){
            BackgroundPng.dispose();
        }
        iconImage.dispose();
    }
}
