package com.thzs.app.datacoplite.util.yolotran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.io.File;
import java.util.*;

public class YoloIconManager implements Disposable {
    public Map<Integer, List<File>> icons;
    public Pixmap ICON;
    public int DefaultWidth=192, DefaultHeight=192;
    public Random random;
    public YoloIconManager(String Directory) {
        icons= new HashMap<>();
        random=new Random();
        SearchIcon(Directory);
    }
    public void SearchIcon(String Directory){
        icons.clear();
        File d=new File(Directory);
        if(!d.exists()||!d.isDirectory())
            return;
        for(File f: d.listFiles()){
            if (f.isFile()&&isPic(f)){
                String name=f.getName().split("\\.")[0];
                if(name.contains("_")){
                    int typeID=Integer.parseInt(name.split("_")[0]);
                    if(icons.containsKey(typeID)){
                        icons.get(typeID).add(f);
                    }else {
                        List<File> fileList=new ArrayList<File>();
                        fileList.add(f);
                        icons.put(typeID,fileList);
                    }
                }else {
                    List<File> fileList=new ArrayList<File>();
                    fileList.add(f);
                    int typeID=Integer.parseInt(name);
                    icons.put(typeID,fileList);
                }
            }
        }
        TypeID=-1;
    }
    public boolean isPic(java.io.File file){
        String fileName=file.getName();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg");
    }
    public int completeCount(int eachCount){
        int sum=0;
        for (Map.Entry<Integer,List<File>> m:icons.entrySet()) {
            List<File> fileList=m.getValue();
            sum+=fileList.size()*eachCount;
        }
        this.eachCount=eachCount;
        return sum;
    }
    public int TypeID=-1;
    public int eachCount=0;
    public Map<Integer,Integer> typeStart;
    public void CheckIcon(int index){
        if (icons.size()==0)
            throw new RuntimeException("No icons found");
        //if start update
        if(TypeID==-1){
            typeStart=new HashMap<>();
            int u=0;
            for (Map.Entry<Integer,List<File>> e: icons.entrySet()) {
                typeStart.put(e.getKey(),u);
                u+=e.getValue().size()*eachCount;
            }
            TypeID=0;
            ICON=new Pixmap(DefaultWidth,DefaultHeight, Pixmap.Format.RGBA8888);
            updateIcon(index);
            return;
        }
        //if typeStart to update
        for (Map.Entry<Integer,Integer> e:typeStart.entrySet()) {
            if (index==e.getValue()){
                TypeID=e.getKey();
                updateIcon(index);
                return;
            }
        }
        //if eachCount is enough to update
        if(index%eachCount==0){
            updateIcon(index);
        }
    }
    public void updateIcon(int index){
        ICON=new Pixmap(DefaultWidth,DefaultHeight, Pixmap.Format.RGBA8888);
        File icon=icons.get(TypeID).get((index-typeStart.get(TypeID))%eachCount);
        Pixmap pixmap=new Pixmap(Gdx.files.absolute(icon.getAbsolutePath()));
        ICON.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,DefaultWidth,DefaultHeight);
        pixmap.dispose();
    }
    public String label="";
    public Vector2 scaleRange=new Vector2(0.4f,1.0f);
public void toSet(int index,YoloImgManger imgManger){
        CheckIcon(index);
        float imW=imgManger.pixmap.getWidth();
        float imH=imgManger.pixmap.getHeight();
        float scale=scaleRange.x+random.nextFloat()*(scaleRange.y-scaleRange.x);
        float needWidth= (float) Math.floor(ICON.getWidth()*scale);
        float needHeight=(float) Math.floor(ICON.getHeight()*scale);
        float x=(float) Math.floor(random.nextFloat()*(imW-needWidth));
        float y= (float) Math.floor(random.nextFloat()*(imH-needHeight));
        label=TypeID+" "+(x+needWidth/2f)/imW+" "+(y+needHeight/2f)/imH+" "+needWidth/imW+" "+needHeight/imH;
        imgManger.pixmap.drawPixmap(ICON, 0, 0,ICON.getWidth(),ICON.getHeight(), (int) x, (int) y, (int) needWidth, (int) needHeight);
    }

    @Override
    public void dispose() {
        if (ICON==null)
            return;
        ICON.dispose();
        ICON=null;
    }
}
