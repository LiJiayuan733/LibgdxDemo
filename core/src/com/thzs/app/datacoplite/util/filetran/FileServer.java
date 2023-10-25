package com.thzs.app.datacoplite.util.filetran;

import com.thzs.app.datacoplite.core.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
/**
 * 简易文件传输服务器
 * example:
 *  FileServer server = new FileServer(8088);
 *  try {
 *       server.load(System.getProperty("user.dir")+"\\SaveFile");
 *  } catch (IOException e) {
 *      throw new RuntimeException(e);
 *  }
 * */
public class FileServer {
    private ServerSocket server;

    private static DecimalFormat df = null;

    static {
        // 设置数字格式，保留一位有效小数
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }
    public FileServer(int port){
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.i("Server is start on port " + port);
    }
    public void load(String FileSaveDir) throws IOException {
        while (true) {
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            Socket socket = server.accept();
            /**
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
             */
            // 每接收到一个Socket就建立一个新的线程来处理它
            new Thread(new Task(socket,FileSaveDir)).start();
        }
    }
    class Task implements Runnable {

        private Socket socket;

        private DataInputStream dis;

        private FileOutputStream fos;
        private String getFormatFileSize(long length) {
            double size = ((double) length) / (1 << 30);
            if(size >= 1) {
                return df.format(size) + "GB";
            }
            size = ((double) length) / (1 << 20);
            if(size >= 1) {
                return df.format(size) + "MB";
            }
            size = ((double) length) / (1 << 10);
            if(size >= 1) {
                return df.format(size) + "KB";
            }
            return length + "B";
        }
        private String SavePath;
        public Task(Socket socket,String path) {
            this.socket = socket;
            this.SavePath=path;
        }
        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());

                // 文件名和长度
                String fileName = dis.readUTF();
                long fileLength = dis.readLong();
                File directory = new File(SavePath);
                if(!directory.exists()) {
                    directory.mkdir();
                }
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);

                // 开始接收文件
                byte[] bytes = new byte[4096];
                int length = 0;
                while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }
                Log.i("======== 文件接收成功 [File Name：" + fileName + "] [Size：" + getFormatFileSize(fileLength) + "] ========");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fos != null)
                        fos.close();
                    if(dis != null)
                        dis.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}