package com.thzs.app.datacoplite.util.process;

import com.thzs.app.datacoplite.core.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 从远程添加日志
 * */
public class LogForNet implements Runnable{
    public void exit(){
        stop=true;
        try {
            Socket socket=new Socket("localhost",port);
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            out.write("exit\n".getBytes());
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean stop=false;
    class ClientHandler implements Runnable{
        public Socket socket;
        public DataInputStream in;
        public DataOutputStream out;
        public ClientHandler(Socket socket){
            this.socket = socket;
        }
        public void run(){
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String line;
                while ((line = in.readLine()) != null){
                    if(line.startsWith("exit")){
                        out.writeUTF("exit");
                        out.flush();
                        break;
                    }
                    Log.i("RP Message:"+line);
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static LogForNet instance;
    public ServerSocket serverSocket;
    //Default port
    public int port=39621;
    public LogForNet(int port){
        LogForNet.instance=this;
        this.port=port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (!stop) {
                Socket client=serverSocket.accept();
                Thread thread=new Thread(new ClientHandler(client));
                thread.setDaemon(true);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
