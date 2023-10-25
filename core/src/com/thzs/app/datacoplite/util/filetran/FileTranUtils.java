package com.thzs.app.datacoplite.util.filetran;

import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;

public class FileTranUtils {
    public static FileClient clientInstance=null;
    public static FileServer serverInstance=null;
    public static Thread serverThread=null;
    public static Thread RunServer(int port){
        FileTranUtils.serverInstance=new FileServer(port);
        FileTranUtils.serverThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileTranUtils.serverInstance.load(System.getProperty("user.dir")+"\\SaveFile");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        FileTranUtils.serverThread.setDaemon(true);
        FileTranUtils.serverThread.start();
        return FileTranUtils.serverThread;
    }
    public static FileClient initClient(String ip,int port){
        FileTranUtils.clientInstance=new FileClient(ip,port);
        return FileTranUtils.clientInstance;
    }
    public static void sendFile(FileHandle fileHandle){
        try {
            FileTranUtils.clientInstance.sendFile(fileHandle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
