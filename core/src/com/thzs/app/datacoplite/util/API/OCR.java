package com.thzs.app.datacoplite.util.API;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpStatus;
import com.thzs.app.datacoplite.util.JsonFind;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台支持，请自行配置 https://github.com/PantsuDango/DangoOCR
 * */
public class OCR {
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
        OCR.Service=thread;
    }
    public static void Destroy(){
        if(isServerRunning()){
            process.destroy();
            Service.stop();
        }
    }
    public static boolean isServerRunning(){
        return OCR.Service!=null && OCR.Service.isAlive();
    }
    public static String host="127.0.0.1";
    public static int port=6666;
    public static String url="/ocr/api";
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
    /**
     * @param filePath 绝对路径 absolute path
     * @param language you can choose range from JAP ENG KOR RU ZH
     * @return the type of result is JsonFind,you can get Json instance in it
     * */
    public static JsonFind SendRequestDefault(String filePath, String language){
        try {
            Map<String, String> body = new HashMap<String, String>();
            body.put("ImagePath",filePath);
            body.put("Language",language);
            String json= JSON.toJSONString(body);
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/json");
            return SendRequest(json,headers);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static List<OCRDataLine> FormatResult(JsonFind Result){
        List<OCRDataLine> result = new ArrayList<OCRDataLine>();
        JSONArray jsonArray;
        if(Result.jso.containsKey("Data")){
            jsonArray= Result.jso.getJSONArray("Data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject map=jsonArray.getJSONObject(i);
                JSONObject points=map.getJSONObject("Coordinate");
                Vector2 LowerLeft=new Vector2(points.getJSONArray("LowerLeft").getFloat(0),points.getJSONArray("LowerLeft").getFloat(1)),LowerRight=new Vector2(points.getJSONArray("LowerRight").getFloat(0),points.getJSONArray("LowerRight").getFloat(1)),UpperLeft=new Vector2(points.getJSONArray("UpperLeft").getFloat(0),points.getJSONArray("UpperLeft").getFloat(1)),UpperRight=new Vector2(points.getJSONArray("UpperRight").getFloat(0),points.getJSONArray("UpperRight").getFloat(1));
                String words=map.getString("Words");
                result.add(new OCRDataLine(words,LowerLeft,LowerRight,UpperLeft,UpperRight,map.getFloat("Score")));
            }
        }
        return result;
    }
}
