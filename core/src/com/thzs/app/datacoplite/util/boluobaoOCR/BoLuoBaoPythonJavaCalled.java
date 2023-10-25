package com.thzs.app.datacoplite.util.boluobaoOCR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.util.process.RealtimeProcess;
import com.thzs.app.datacoplite.util.process.RealtimeProcessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class BoLuoBaoPythonJavaCalled {
    static class ProcessInterface implements RealtimeProcessInterface{
        @Override
        public void onNewStdoutListener(String newStdout) {
        }

        @Override
        public void onNewStderrListener(String newStderr) {
            Log.e(newStderr);
        }

        @Override
        public void onProcessFinish(int resultCode) {
            Log.i("Process finish exit code:"+resultCode);
        }
    }
    /**
     * 用于测试的文件
     * */
    @Deprecated
    public static void ImageAutoCreate() throws IOException, InterruptedException {
        String pythonCommand = "python";
        String scriptPath = "C:\\Users\\32827\\IdeaProjects\\FileTranServer\\SaveFile\\AutoImage.py";
        ProcessBuilder pb = new ProcessBuilder(pythonCommand, scriptPath,"C:\\Users\\32827\\IdeaProjects\\FileTranServer\\SaveFile\\TestImgs");
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Python脚本执行完毕，返回值：" + exitCode);
    }
    /**
     * only be used on Windows
     * requires opencv
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * */
    public static void ImgTo2(FileHandle fileDir) throws IOException, InterruptedException {
        String pythonCommand = "python";
        String scriptPath = Gdx.files.internal("pythonScript/boluobao/blb_img_to_2.py").file().getAbsolutePath();
        RealtimeProcess rp=new RealtimeProcess(new ProcessInterface());
        rp.addCommand(pythonCommand, scriptPath,fileDir.file().getAbsolutePath());
        rp.start();
    }
    /**
     * only be used on Windows
     * requires opencv
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * @param x1,y1,x2,y2 are the area's position of the image that you need. The original position was set to left top of screen.
     * */
    public static void ImgCut(FileHandle fileDir,int x1,int y1,int x2,int y2) throws IOException, InterruptedException{
        String pythonCommand = "python";
        String scriptPath = Gdx.files.internal("pythonScript/boluobao/blb_img_cut.py").file().getAbsolutePath();
        RealtimeProcess rp=new RealtimeProcess(new ProcessInterface());
        rp.addCommand(pythonCommand, scriptPath,fileDir.file().getAbsolutePath(),x1+"",y1+"",x2+"",y2+"");
        rp.start();
    }
    /**
     * only be used on Windows
     * requires easyocr json numpy
     * python environment needs to create for yourself
     * @param fileDir the image directory
     * @param num is used to set the number of result when need to save to file
     * @param count the file's amount that need to ocr
     * @param start the file start serial number
     * @param start_name the file name of the serial
     * @param temp the start serial number for result file
     * */
    public static void ImgOCR(FileHandle fileDir,int num,int count,int start,String start_name,int temp) throws IOException, InterruptedException, TimeoutException {
        String pythonCommand = "python";
        String scriptPath = Gdx.files.internal("pythonScript/boluobao/blb_img_ocr.py").file().getAbsolutePath();
        RealtimeProcess rp=new RealtimeProcess(new ProcessInterface());
        rp.addCommand(pythonCommand, scriptPath,fileDir.file().getAbsolutePath(),  num+"",count+"",start+"",start_name,temp+"");
        rp.start();
    }
    /**
     * 用于将结果文件进行解析合成最后的小说文件
     * @param path the parent dir of the result file
     * @param startName the result file Start signal
     * @param start the start serial number
     * @param count the file count
     * */
    public static void ResultHandlerONE(FileHandle path,String startName,int start,int count,int x,int x2) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<ArrayList<ResultLine>> temp=new ArrayList<ArrayList<ResultLine>>();
        for (int i = start; i < start + count; i++) {
            Log.i(path.file().getAbsolutePath()+"/"+startName+i+".txt");
            File file = new File(path.file().getAbsolutePath()+"/"+startName+i+".txt");
            if (file.exists()){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    // 解析JSON字符串为JsonNode对象
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String tmp="",line;
                    while ((line = reader.readLine()) != null) {
                        tmp += line;
                    }
                    JsonNode jsonNode = mapper.readTree(tmp);
                    ResultComplete rc=new ResultComplete(jsonNode);
                    rc.Complete();
                    temp.addAll(rc.pagesLevel2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String temp2="";
        for (int i = 0; i < temp.size(); i++) {
            ArrayList<ResultLine> re=temp.get(i);
            for (int j=0;j<re.size();j++){
                if(re.get(j).isHead(x)){
                    if(!temp2.equals("")){
                        lines.add("  "+temp2);
                    }
                    temp2=re.get(j).message;
                }else{
                    temp2+=re.get(j).message;
                }
            }
        }
        if(!temp2.equals("")){
            lines.add("  "+temp2);
        }
        File f=new File(path.file().getAbsolutePath()+"/"+startName+".txt");
        BufferedWriter fw=new BufferedWriter(new FileWriter(f));
        for (String i:lines) {
            fw.write(i+"\n");
        }
        fw.flush();
        fw.close();
    }
}
