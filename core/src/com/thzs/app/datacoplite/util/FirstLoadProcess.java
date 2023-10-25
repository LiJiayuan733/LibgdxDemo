package com.thzs.app.datacoplite.util;


import com.badlogic.gdx.Gdx;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.core.Path;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FirstLoadProcess {
    public static String SourceDir="RuntimeFiles";
    //要检查的文件夹列表
    public static List<File> dirList=new ArrayList<>();
    //要创建的文件列表
    public static List<File> fileList=new ArrayList<>();
    public Properties cfg;
    public boolean FirstRunCheck(){
        //检查配置文件是否存在
        File config=new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+"Config.properties");
        return !config.exists();
    }
    public void Save(){
        File config=new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+"Config.properties");
        try {
            cfg.store(new FileOutputStream(config),"App Status Config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void createConfigAndLoad() {
        File config=new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+"Config.properties");
        try {
            config.createNewFile();
            cfg=new Properties();
            cfg.load(new FileInputStream(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadCfg(){
        File config=new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+"Config.properties");
        try {
            cfg=new Properties();
            cfg.load(new FileInputStream(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void CreateEnv(){
        //检查文件夹
        for (File dir:dirList){
            if (!dir.exists()){
                File parent=new File(dir.getParent());
                if(parent.exists()){
                    dir.mkdir();
                }else {
                    dir.mkdirs();
                }
            }
            if(Gdx.app!=null) {
                Log.i("Directory: " + dir.getAbsolutePath() + " [CHECK]");
            }
        }
        //检查文件
        for (File file : fileList){
            if(!file.exists()){
                File parent=new File(file.getParent());
                if(parent.exists()){
                    copyTo(file);
                }
            }
            if(Gdx.app!=null) {
                Log.i("File: " + file.getAbsolutePath() + " [CHECK]");
            }
        }
    }
    /**
     * @param path the path don't to add '/' at the beginning
     * */
    public static void addFile(String path){
        fileList.add(new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+path));
    }
    /**
     * @param path the path don't to add '/' at the beginning
     * */
    public static void addDir(String path){
        dirList.add(new File((Path.RUN_PATH.endsWith("/")?Path.RUN_PATH:Path.RUN_PATH+"/")+path));
    }
    public static String getInnerPath(File file){
        return file.getAbsolutePath().replace(Path.RUN_PATH,"");
    }
    public void copyTo(File file){
        InputStream f=getClass().getResourceAsStream("/"+SourceDir+"/"+getInnerPath(file));
        if(f==null){
            return;
        }
        try {
            Files.copy(f, file.toPath());
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
