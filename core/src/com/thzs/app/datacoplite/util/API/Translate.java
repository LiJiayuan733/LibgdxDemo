package com.thzs.app.datacoplite.util.API;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Null;
import com.thzs.app.datacoplite.util.JsonFind;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translate {
    public static Thread Service=null;
    public static Process process;
    public static void RunServer(String ServerPath){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessBuilder pb = new ProcessBuilder("python", ServerPath);
                    pb.directory(new File(ServerPath).getParentFile());
                    process = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    int exitCode = process.waitFor();
                    System.out.println("Python脚本执行完毕，返回值：" + exitCode);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        Translate.Service=thread;
    }
    public static void Destroy(){
        if(isServerRunning()){
            process.destroy();
            Service.stop();
        }
    }
    public static boolean isServerRunning(){
        return Translate.Service!=null && Translate.Service.isAlive();
    }
    public static String host="127.0.0.1";
    public static int port=7777;
    public static String url="/api";
    public static JsonFind SendRequest(String jsonStr, HashMap<String,String> headers) throws IOException {
        String jsonResult="";
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(3*1000);
        // 读取数据超时
        client.getHttpConnectionManager().getParams().setSoTimeout(3*60*1000);
        client.getParams().setContentCharset("UTF-8");
        PostMethod postMethod = new PostMethod("http://"+host+":"+port+url);

        postMethod.setRequestHeader("content-type", headers.get("content-type"));
        if (null != jsonStr && !"".equals(jsonStr)) {
            StringRequestEntity requestEntity = new StringRequestEntity(jsonStr, headers.get("content-type"), "UTF-8");
            postMethod.setRequestEntity(requestEntity);
        }
        int status = client.executeMethod(postMethod);
        if (status == HttpStatus.SC_OK) {
            jsonResult = postMethod.getResponseBodyAsString();
        } else {
            throw new RuntimeException("接口连接失败！");
        }
        return new JsonFind(jsonResult);
    }
    //{'ImagePath':'','Language':''}
    @Null
    public static JsonFind SendRequestDefault(String text){
        try {
            Map<String, String> body = new HashMap<String, String>();
            body.put("Text",text);
            String json= JSON.toJSONString(body);
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            return SendRequest(json,headers);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    @Null
    public static List getData(JsonFind result){
        if(result==null){
            return null;
        }
        JSONObject s=result.jso;
        int code = s.getInteger("Code");
        if(code == 0){
            return s.getJSONArray("Data");
        }else {
            return null;
        }
    }
}
