package com.thzs.app.datacoplite.util.filetran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileClient {

    private Socket client;

    private InputStream fis;

    private DataOutputStream dos;
    public FileClient(String IP, int port){
        try {
            client=new Socket(IP,port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendFile(String path) throws Exception {
        sendFile(Gdx.files.absolute(path));
    }
    public void sendFile(FileHandle file) throws Exception {
        try {
            if(file.exists()) {
                fis = file.read();
                dos = new DataOutputStream(client.getOutputStream());

                // 文件名和长度
                dos.writeUTF(file.name());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();

                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[4096];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            client.close();
        }
    }
}
