package com.thzs.app.datacoplite.util.yolotran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YoloBackgroundManager implements Disposable {
    public List<File> BackgroundFiles;
    public int Count=0;
    public int BackgroundIndex=-1;
    public Pixmap Background;
    public Random random;
    public YoloBackgroundManager(String Directory){
        BackgroundFiles=new ArrayList<File>();
        random=new Random();
        SearchBackground(Directory);
    }
    public void SearchBackground(String Directory){
        BackgroundFiles.clear();
        File d=new File(Directory);
        if(!d.exists()||!d.isDirectory())
            return;
        for(File f: d.listFiles()){
            if (f.isFile()&&isPic(f))
                BackgroundFiles.add(f);
        }
    }
    public boolean isPic(File file){
        String fileName=file.getName();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg");
    }
    public void setCount(int count){
        Count=count;
        BackgroundIndex=-1;
    }
    public void CheckBackground(int index){
        if(BackgroundFiles.size()==0){
            throw new RuntimeException("Background Files Size == 0");
        }
        if(BackgroundIndex==-1){
            BackgroundIndex=0;
            Background=new Pixmap(Gdx.files.absolute(BackgroundFiles.get(BackgroundIndex).getAbsolutePath()));
            return;
        }
        if(BackgroundIndex< (double) (index * BackgroundFiles.size() / Count)){
            BackgroundIndex+=1;
            Background=new Pixmap(Gdx.files.absolute(BackgroundFiles.get(BackgroundIndex).getAbsolutePath()));
        }
    }
    public void toSet(int index,YoloImgManger imgManger){
        index=index%Count;
        CheckBackground(index);
        int needWidth=imgManger.pixmap.getWidth();
        int needHeight=imgManger.pixmap.getHeight();
        int x=random.nextInt(Background.getWidth()-needWidth);
        int y=random.nextInt(Background.getHeight()-needHeight);

        imgManger.pixmap.drawPixmap(Background,x,y,needWidth,needHeight,0,0,needWidth,needHeight);
    }

    @Override
    public void dispose() {
        if(Background!=null){
            Background.dispose();
            Background=null;
        }
    }
}
