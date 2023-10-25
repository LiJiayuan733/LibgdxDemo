package com.thzs.app.datacoplite.util.API;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.math.Vector2;
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
import java.math.BigDecimal;
import java.util.*;

public class Yolo {
    public static Thread Service=null;
    public static Process process;
    public static void RunServer(String ServerPath) {
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
        Yolo.Service=thread;
    }
    public static void Destroy(){
        if(isServerRunning()){
            process.destroy();
            Service.stop();
        }
    }
    public static boolean isServerRunning(){
        return Yolo.Service!=null && Yolo.Service.isAlive();
    }
    public static String host="127.0.0.1";
    public static int port=8888;
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
    public static JsonFind SendRequestDefault(String ImgPath){
        try {
            Map<String, String> body = new HashMap<String, String>();
            body.put("ImgPath",ImgPath);
            String json= JSON.toJSONString(body);
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            return SendRequest(json,headers);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static class YoloResult{
        public Vector2 point1,point2;
        public Vector2 center(){
            return new Vector2((point1.x+point2.x)/2f,(point1.y+point2.y)/2f);
        }
        public float conf;
        public int cls;
    }
    @Null
    public static List<YoloResult> getData(JsonFind result,int type){
        if(result==null){
            return null;
        }
        List<YoloResult> resultList = new ArrayList<YoloResult>();
        JSONObject s=result.jso;
        int code = s.getInteger("Code");
        if(code == 0){
            JSONObject l=s.getJSONObject("Data");
            for(Map.Entry<String, Object> e:l.entrySet()){
                JSONObject jsonObject=((JSONObject) e.getValue());
                JSONArray xyxy=jsonObject.getJSONArray("xyxy");
                BigDecimal per=jsonObject.getBigDecimal("conf");
                int cls=jsonObject.getInteger("cls");
                if(type==cls||type==-1){
                    JSONArray v=xyxy.getJSONArray(0);
                    YoloResult yr=new YoloResult();
                    yr.point1=new Vector2(v.getInteger(0),v.getInteger(1));
                    v=xyxy.getJSONArray(1);
                    yr.point2=new Vector2(v.getInteger(0),v.getInteger(1));
                    yr.conf=per.floatValue();
                    yr.cls=cls;
                    resultList.add(yr);
                }
            }
        }
        return resultList;
    }
}
